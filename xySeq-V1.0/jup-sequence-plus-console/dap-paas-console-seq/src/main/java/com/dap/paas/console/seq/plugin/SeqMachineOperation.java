package com.dap.paas.console.seq.plugin;


import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.utils.SSH2Util;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.dap.paas.console.common.util.DapUtil;
import com.dap.paas.console.common.util.NetUtils;
import com.dap.paas.console.seq.dto.SeqMachineNodeDTO;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.enums.NodeRunState;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.util.jsch.JschOperator;
import com.dap.paas.console.seq.util.jsch.ShellResult;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static javax.management.timer.Timer.ONE_MINUTE;
import static javax.management.timer.Timer.ONE_SECOND;

/**
 * @className SeqMachineOperation
 * @description 序列机器操作
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Primary
@Component
public class SeqMachineOperation {

    double MIN_MEMORY_FREE = 0.5;

    public static final long FIVE_SECONDS = 5 * ONE_SECOND;

    public static final long TEN_SECONDS = 10 * ONE_SECOND;

    public static final long FIVE_MINUTES = 5 * ONE_MINUTE;

    public static final long TEN_MINUTES = 2 * FIVE_MINUTES;

    public static final long HALF_HOUR = 3 * TEN_MINUTES;

    private static final Logger logger = LoggerFactory.getLogger(SeqMachineOperation.class);

    protected static ExecutorService threadPool = new ThreadPoolExecutor(4, 10, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("seq-image-pool-thread-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());


    @Autowired
    private MachineService machineService;

    @Lazy
    @Autowired
    private SeqServiceNodeService seqServiceNodeService;

    @Autowired
    Environment environment;

    /**
     * 目标机器安装上传临时目录
     */
    public static final String REDIS_CONF = "redis.conf";

    public static final String SENTINEL_CONF = "sentinel.conf";

    public static final String BIND = "bind";

    public static final String PORT = "port";

    public static final String DEPLOY_PATH = "deploy_path";

    public static final String DAEMONIZE = "daemonize";

    protected static final int TIMEOUT = 180;


    public void checkPorts(String ip, Integer port) {
        // 如果端口能通，则认为该端口被占用
        if (!NetUtils.telnet(ip, port)) {
            throw new DapWebServerException("主机：" + ip + "端口：" + port + " ，已占用请更换后重试");
        }
    }


    /**
     * 从本机copy到目标机器上
     *
     * @param cluster cluster
     * @param machineNodeDTOList machineNodeDTOList
     * @return boolean
     */
    public boolean pullImage(SeqServiceCluster cluster, SeqMachineNodeDTO... machineNodeDTOList) {

        // 获取安装包名称
        String imageName = "seq-server.tar.gz";
        String localDeployPath = DapUtil.getProjectPath() + "/package/sequence";
        String localFile = localDeployPath + "/" + imageName;
        if (!new File(localFile).exists()) {
            logger.error("部署包不存在：{}", localFile);
            throw new DapWebServerException("部署包不存在：{}" + localFile);
        }

        List<Future<Boolean>> resultFutureList = new ArrayList<>(machineNodeDTOList.length);
        for (SeqMachineNodeDTO machineNode : machineNodeDTOList) {
            resultFutureList.add(threadPool.submit(() -> {
                Machine machine = machineNode.getMachine();

                // 目标机器上要部署的文件夹
                String tarDeployPath = machineNode.getNodes().get(0).getDeployPath();

                JschOperator jr = null;
                try {
                    jr = new JschOperator(machine.getHostIp(), machine.getHostSshAccount(), machine.getHostSshPassword(), machine.getHostPort());
                    jr.openSession();
                    jr.openChannel(JschOperator.CHANNEL_SFTP);

                    // 上传文件到目标服务器
                    jr.uploadFile(localFile, tarDeployPath);

                    logger.info("上传部署包到机器:{} , 上传到机器的地址:{} ", machine.getHostIp() + "@" + machine.getHostSshAccount(), tarDeployPath);
                } catch (Exception e) {
                    throw new DapWebServerException("上传部署包到机器:" + machine.getHostIp() + "失败 ");
                } finally {
                    Optional.ofNullable(jr).ifPresent(JschOperator::close);
                }

                // 上传到机器的部署包地址
                String tarDeployFile = tarDeployPath + "/" + imageName;

                // 逐个解压到各个节点
                for (SeqServiceNode node : machineNode.getNodes()) {
                    String nodePathName = "seq-server-" + node.getPort();

                    // 当前节点准备部署的目录
                    String nodeDeployPath = node.getDeployPath() + "/" + nodePathName;
                    String nodeConfigPath = nodeDeployPath + "/config/application.properties";
                    String nodeLogfilePath = nodeDeployPath + "/logs/stdout.log";

                    node.setHostIp(machine.getHostIp());
                    node.setDeployPath(nodeDeployPath);
                    node.setLogfilePath(nodeLogfilePath);
                    node.setConfigPath(nodeConfigPath);

                    StringBuilder shell = new StringBuilder();

                    // 创建文件夹脚本
                    shell.append(String.format("mkdir -p %s;", nodeDeployPath));

                    // 解压文件脚本
                    shell.append(String.format(" tar -xzvf %s -C %s --strip-components 1;", tarDeployFile, nodeDeployPath));

                    // 修改配置脚本
                    shell.append(updateConfig(cluster, node));

                    // shell.append(String.format("chmod u+x %s/* ;" , nodeDeployPath));  授权脚本 启动脚本
                    shell.append(String.format("source /etc/profile; sh %s/bin/start.sh ;", nodeDeployPath));

                    // 删除部署包
                    shell.append(String.format("rm -rf  %s ;", tarDeployFile));
                    try {
                        jr = new JschOperator(machine.getHostIp(), machine.getHostSshAccount(), machine.getHostSshPassword(), machine.getHostPort());
                        jr.openSession();
                        jr.openChannel("exec");
                        ShellResult result4 = jr.executeExec(shell.toString());
                        logger.info("解压并修改配置返回结果  配置路径:{} , 返回状态码：{}返回结果：{}", nodeConfigPath, result4.getExitStatus(), result4.getShellOutPut());
                    } catch (Exception e) {
                        throw new DapWebServerException("解压并修改配置失败 配置路径:" + nodeConfigPath);
                    } finally {
                        Optional.of(jr).ifPresent(JschOperator::close);
                    }
                }
                return true;
            }));
        }

        for (Future<Boolean> resultFuture : resultFutureList) {
            try {
                if (!resultFuture.get(TIMEOUT, TimeUnit.SECONDS)) {
                    return false;
                }
            } catch (Exception e) {
                logger.error("SeqTimeJob job monitor error ", e);

                throw new DapWebServerException("部署失败:" + e.getMessage());
            }
        }
        return true;

    }


    /**
     * 修改配置 datasource, port 等变量赋值
     *
     * @param cluster cluster
     * @param node node
     * @return String
     */
    public String updateConfig(SeqServiceCluster cluster, SeqServiceNode node) {
        StringBuffer commands = new StringBuffer();

        Map<String, String> configs = getBaseConfigs(cluster, node);

        for (Map.Entry<String, String> entry : configs.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 删除原来的行数据 '/^spring.datasource.url=.*/d'
            commands.append("sed -i '/^").append(key).append("=.*/d' ").append(node.getConfigPath()).append(";");

            // 追加配置 echo 'spring.datasource.url=jdbc:mysql://10.114.14.65:3306/dap_paas_console?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8' >> /home/dap3/seq-server/seq-server-10005/config/application.properties;
            commands.append("echo '").append(key).append("=").append(value).append("' >> ").append(node.getConfigPath()).append(";");
        }
        // 向配置文件写入ip和端口
        commands.append("sed -i 's/dap.sequence.serviceNodeIp=/dap.sequence.serviceNodeIp=").append(node.getHostIp()).append(":").append(node.getPort()).append("/' ").append(node.getConfigPath()).append(";");
        return commands.toString();
    }

    private final List<String> configKeys = new ArrayList<String>() {
        {
            add("spring.datasource.driver-class-name");
            add("spring.datasource.url");
            add("spring.datasource.username");
            add("spring.datasource.password");
        }
    };

    private Map<String, String> getBaseConfigs(SeqServiceCluster cluster, SeqServiceNode node) {
        Map<String, String> configs = new HashMap<>();
        if ("1".equals(cluster.getDbType())) {
            configs.put("spring.datasource.driver-class-name", cluster.getDbDriver());
            configs.put("spring.datasource.url", cluster.getDbUrl());
            configs.put("spring.datasource.username", cluster.getDbUser());
            configs.put("spring.datasource.password", cluster.getDbPassword());
        } else {
            for (String config : configKeys) {
                configs.put(config, environment.getProperty(config));
            }
        }
        configs.put("server.port", node.getPort() + "");
        return configs;
    }

    /**
     * 启动集群
     *
     * @param cluster cluster
     * @return boolean
     */
    public boolean start(SeqServiceCluster cluster) {

        SeqServiceNode queryPo = new SeqServiceNode();
        queryPo.setClusterId(cluster.getId());
        List<SeqServiceNode> list = seqServiceNodeService.queryList(queryPo);
        List<Future<Result>> resultFutureList = new ArrayList<>(list.size());

        for (SeqServiceNode node : list) {
            resultFutureList.add(threadPool.submit(() -> start(node)));
        }
        boolean result = false;
        for (Future<Result> resultFuture : resultFutureList) {
            try {
                Result startResult = resultFuture.get(TIMEOUT, TimeUnit.SECONDS);
                if (startResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                    result = true;
                }
            } catch (Exception e) {
                logger.error("seq-server start fail " , e);
            }
        }
        return result;
    }

    /**
     * 启动普通节点
     *
     * @param clusterNode clusterNode
     * @return Result
     */
    public Result start(SeqServiceNode clusterNode) {
        Machine machine = machineService.getObjectById(clusterNode.getMachineId());
        if (machine == null) {
            return ResultUtils.error("机器不能为空");
        }
        int port = clusterNode.getPort();
        String host = machine.getHostIp();

        String command = String.format("source /etc/profile; sh %s/bin/start.sh ;", clusterNode.getDeployPath());
        String execute = "";
        try {

            execute = SSH2Util.execute(machine, command);

            if (StringUtils.isNotBlank(execute) && checkPidExist(execute)) {
                clusterNode.setStatus(NodeRunState.RUN.getKey() + "");
                seqServiceNodeService.update(clusterNode);
                return ResultUtils.success();
            } else {
                return ResultUtils.error(execute);
            }
        } catch (Exception e) {
            String message = "Start failed, host: " + host + ", port: " + port + " " + e.getMessage();
            logger.error(message,e);
            return ResultUtils.error(message);
        }
    }

    private boolean checkPidExist(String res) {
        String[] split = res.split("\n");
        for (String row : split) {
            if (row.contains("PIDS")) {
                String[] splitRow = row.split(":");
                // pid不为空
                if (splitRow.length > 1 && StringUtils.isNotBlank(splitRow[1])) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * 停止集群
     *
     * @param cluster cluster
     * @return boolean
     */
    public boolean stop(SeqServiceCluster cluster) {
        SeqServiceNode queryPo = new SeqServiceNode();
        queryPo.setClusterId(cluster.getId());
        List<SeqServiceNode> list = seqServiceNodeService.queryList(queryPo);

        List<Future<Result>> resultFutureList = new ArrayList<>(list.size());

        for (SeqServiceNode node : list) {
            resultFutureList.add(threadPool.submit(() -> stop(node)));
        }
        boolean result = false;
        for (Future<Result> resultFuture : resultFutureList) {
            try {
                Result startResult = resultFuture.get(TIMEOUT, TimeUnit.SECONDS);
                if (startResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
                    result = true;
                }
            } catch (Exception e) {
                logger.error("seq-server stop fail " , e);
            }
        }
        return result;
    }

    /**
     * 停止节点
     *
     * @param node node
     * @return Result
     */
    public Result stop(SeqServiceNode node) {
        Machine machine = machineService.getObjectById(node.getMachineId());
        if (machine == null) {
            return ResultUtils.error("机器不能为空");
        }
        int port = node.getPort();
        String host = machine.getHostIp();

        String command = String.format("cd %s;sh %s/bin/stop.sh ;", node.getDeployPath(), node.getDeployPath());
        String execute = "";
        try {

            execute = SSH2Util.execute(machine, command);

            if (StringUtils.contains(execute, "已停止") || StringUtils.contains(execute, "未启动") || !NetUtils.telnet(host, port)) {
                node.setStatus(NodeRunState.STOP.getKey() + "");
                seqServiceNodeService.update(node);
                return ResultUtils.success();
            } else {
                return ResultUtils.error(execute);
            }
        } catch (Exception e) {
            String message = "Start failed, host: " + host + ", port: " + port + " " + e.getMessage();
            logger.error(message,e);
            return ResultUtils.error(message);
        }
    }

    /**
     * 节点重启
     *
     * @return boolean
     */
    public boolean restart(SeqServiceNode node) {
        stop(node);
        start(node);
        return true;
    }

    /**
     * 删除节点
     *
     * @param node node
     * @param machine machine
     * @return boolean
     */
    public boolean remove(SeqServiceNode node, Machine machine) {
        stop(node);
        int port = node.getPort();
        String host = machine.getHostIp();
        String command = " rm -rf " + node.getDeployPath();
        String execute = "";
        try {
            execute = SSH2Util.execute(machine, command);
            return StringUtils.isBlank(execute) || !NetUtils.telnet(host, port);
        } catch (Exception e) {
            String message = "remove node  failed, host: " + host + ", port: " + port + " " + e.getMessage();
            logger.error(message,e);
            return false;
        }
    }
}
