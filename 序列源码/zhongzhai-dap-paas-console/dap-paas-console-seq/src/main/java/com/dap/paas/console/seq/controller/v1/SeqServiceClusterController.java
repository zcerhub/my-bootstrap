package com.dap.paas.console.seq.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.auth.log.AuditLog;
import com.base.sys.auth.log.OperateTypeAspect;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.utils.SSH2Util;
import com.dap.paas.console.basic.utils.SshDeployServerUtil;
import com.dap.paas.console.common.constants.EnvLibConstant;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.dap.paas.console.common.util.NetUtils;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dto.ClusterConfigInfo;
import com.dap.paas.console.seq.dto.SeqInstallationDTO;
import com.dap.paas.console.seq.dto.SeqMachineNodeDTO;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.plugin.SeqMachineOperation;
import com.dap.paas.console.seq.service.SeqMulticenterNodeService;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @className SeqServiceClusterController
 * @description 序列服务集群
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列服务集群")
@RestController
@RequestMapping("/seq/service/cluster")
@Validated
public class SeqServiceClusterController {

    private final static Logger LOG = LoggerFactory.getLogger(SeqServiceClusterController.class);

    @Autowired
    private SeqServiceClusterService seqClusterService;

    @Autowired
    private SeqServiceNodeService seqNodeService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private SeqMachineOperation seqMachineOperation;

    @Autowired
    private SeqMulticenterNodeService multicenterNodeService;

    /**
     * 序列服务集群新增
     *
     * @param seqServiceCluster 对象
     * @return 成功/失败
     */
    @ApiOperation(value = "序列服务集群新增")
    @PostMapping("/insert")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "服务序列服务集群新增", componentType = OperateTypeAspect.SEQ)
    public Result insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqServiceCluster seqServiceCluster) {
        LOG.info("序列服务集群新增信息 = {}", seqServiceCluster);
        return seqClusterService.insert(seqServiceCluster);
    }

    /**
     * 根据序列服务集群ID查询当前对象
     *
     * @param id 序列服务集群ID
     * @return 序列服务集群对象
     */
    @ApiOperation(value = "序列服务集群ID查询")
    @GetMapping("/query/{id}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "序列服务集群详情查询", componentType = OperateTypeAspect.SEQ)
    public Result query(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String id) {
        SeqServiceCluster seqServiceCluster = seqClusterService.getObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS, seqServiceCluster);
    }

    /**
     * 根据序列服务集群对象属性查询
     *
     * @param param 序列服务集群对象
     * @return 带有分页的序列服务集群查询结果
     */
    @ApiOperation(value = "序列服务集群对象分页")
    @PostMapping("/queryPage")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列服务集群对象分页", componentType = OperateTypeAspect.SEQ)
    public Result queryPage(@RequestBody PageInDto<SeqServiceClusterVo> param, HttpServletRequest request) {
        SeqServiceClusterVo requestObject = param.getRequestObject();
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(requestObject::setTenantId);
        Page seqServiceCluster = seqClusterService.queryPage(param.getPageNo(), param.getPageSize(), requestObject);
        return ResultUtils.success(ResultEnum.SUCCESS, seqServiceCluster);

    }

    /**
     * 序列服务集群修改
     *
     * @param seqServiceCluster 序列服务集群对象
     * @return 成功/失败
     */
    @ApiOperation(value = "序列服务集群修改")
    @PutMapping("/update")
    @AuditLog(operateType = OperateTypeAspect.PUT, modelName = "序列服务集群修改", componentType = OperateTypeAspect.SEQ)
    public Result update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqServiceCluster seqServiceCluster) {
        LOG.info("序列服务集群修改信息 = {}", seqServiceCluster);
        return seqClusterService.update(seqServiceCluster);
    }

    /**
     * 序列服务集群删除-单选
     *
     * @param id 序列服务集群ID
     * @return 成功/失败
     */
    @ApiOperation(value = "序列服务集群删除-单选")
    @DeleteMapping("/delete/{id}")
    @AuditLog(operateType = OperateTypeAspect.DELETE, modelName = "序列服务集群删除-单选", componentType = OperateTypeAspect.SEQ)
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String id) {
        LOG.info("序列服务集群删除id = {}", id);
        SeqServiceNodeVo node = new SeqServiceNodeVo();
        node.setClusterId(id);
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(node::setTenantId);
        List<SeqServiceNodeVo> registerInstanceVos = seqNodeService.queryNodes(node);
        if (!registerInstanceVos.isEmpty()) {
            return ResultUtils.error(ResultEnum.INSTANCE_EXIST);
        } else {
            return seqClusterService.delete(id);
        }
    }

    /**
     * 初始化前检查环境
     *
     * @param installationParam installationParam
     * @return Result
     */
    @ApiOperation(value = "初始化前检查环境", httpMethod = "POST")
    @PostMapping(value = "checkEnv")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "初始化前检查环境", componentType = OperateTypeAspect.SEQ)
    public Result checkEnv(@RequestBody @Validated(value = {ValidGroup.Valid.ClusterEnvCheck.class}) SeqInstallationDTO installationParam) {
        StopWatch stopWatch = new StopWatch();

        // 检测端口占用
        stopWatch.start();

        boolean javaHomeFlag = false;
        boolean tarFlag = false;
        boolean netToolsFlag = false;
        String checkLibName = EnvLibConstant.TAR;
        String netTolls = EnvLibConstant.NETTOOLS;
        String resMsg = "";
        try {
            // 存储机器信息，去除重复日志
            Map<String, String> ipsMap = new HashMap<>();
            for (SeqServiceNode clusterNode : installationParam.getSeqServiceNodeList()) {
                String machineId = Optional.ofNullable(clusterNode.getMachineId()).orElseThrow(() -> new ServiceException("机器不能为空"));
                // 检查java环境变量是否配置
                Machine machine = machineService.getObjectById(machineId);
                String ip = machine.getHostIp();
                boolean javaHome = SshDeployServerUtil.checkJavaHome(machine);
                // 没有配置JAVA_HOME
                if (!javaHome && null == ipsMap.get(ip + "javaHome")) {
                    ipsMap.put(ip + "javaHome", ip);
                    if (resMsg.isEmpty()) {
                        resMsg = ">>>服务器" + ip + "没有配置JAVA_HOME环境变量\n";
                    } else {
                        resMsg = resMsg + ">>>服务器" + ip + "没有配置JAVA_HOME环境变量\n";
                    }
                    javaHomeFlag = true;
                }

                if (!SSH2Util.checkLibExist(machine, checkLibName) && null == ipsMap.get(ip + "tarFlag")) {
                    ipsMap.put(ip + "tarFlag", ip);
                    if (resMsg.isEmpty()) {
                        resMsg = ">>>服务器" + ip + "，未安装" + checkLibName + "包 \n";
                    } else {
                        resMsg = resMsg + ">>>服务器" + ip + "，未安装" + checkLibName + "包\n";
                    }
                    tarFlag = true;
                }

                if (!SSH2Util.checkLibExist(machine, netTolls) && null == ipsMap.get(ip + "netTollsFlag")) {
                    ipsMap.put(ip + "netTollsFlag", ip);
                    if (resMsg.isEmpty()) {
                        resMsg = ">>>服务器" + ip + "，未安装" + netTolls + "包\n";
                    } else {
                        resMsg = resMsg + ">>>服务器" + ip + "，未安装" + netTolls + "包\n";
                    }
                    netToolsFlag = true;
                }
            }
        } catch (Exception e) {
            return ResultUtils.error(ResultEnum.EXIST.getCode(), "基础环境校验异常" + e.getMessage());
        }
        // 存在没有配置JAVA_HOME环境变量或者没有tar命令或者没有net-tools
        if (javaHomeFlag || tarFlag || netToolsFlag) {
            return ResultUtils.error(ResultEnum.EXIST.getCode(), resMsg);
        }

        Set<String> set = new HashSet<>();
        for (SeqServiceNode clusterNode : installationParam.getSeqServiceNodeList()) {
            String ip = clusterNode.getHostIp();
            int port = clusterNode.getPort();
            // 检测端口号不能小于1000
            if (port < 1000) {
                return ResultUtils.error("端口号不能小于1000");
            }
            // 如果端口能通，则认为该端口被占用
            if (NetUtils.telnet(ip, port)) {
                return ResultUtils.error("主机：" + ip + "端口：" + port + " ，已占用请更换后重试");
            }

            String hostIpPort = ip + ":" + port;
            if (!set.add(hostIpPort)) {
                return ResultUtils.error(ResultEnum.EXIST.getCode(), "选择的主机端口:" + hostIpPort + "出现重复 ");
            }
        }

        stopWatch.stop();
        LOG.info("检测端口占用检测耗时：{}", stopWatch.getTotalTimeMillis());
        return ResultUtils.success();
    }

    /**
     * 序列服务集群节点初始化
     *
     * @param installationParam installationParam
     * @param servletRequest servletRequest
     * @return Result
     */
    @ApiOperation(value = "序列服务集群节点初始化", httpMethod = "POST")
    @PostMapping(value = "nodeInit")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列服务集群节点初始化", componentType = OperateTypeAspect.SEQ)
    public Result nodeInit(@RequestBody @Validated(value = {ValidGroup.Valid.ClusterEnvCheck.class}) SeqInstallationDTO installationParam, ServletRequest servletRequest) {
        SeqServiceCluster cluster = seqClusterService.getObjectById(installationParam.getClusterId());
        if (cluster == null) {
            return ResultUtils.error("序列服务集群不存在");
        }

        Map<String, List<SeqServiceNode>> map = installationParam.getSeqServiceNodeList().stream()
                .collect(Collectors.groupingBy(SeqServiceNode::getMachineId));
        List<SeqMachineNodeDTO> machineNodeDTOList = new ArrayList<>();
        map.forEach((k, v) -> {
            Machine machine = machineService.getObjectById(k);
            if (machine == null) {
                throw new DapWebServerException("主机：" + k + ",机器不存在");
            }
            v.forEach(node -> node.setHostIp(machine.getHostIp()));
            SeqMachineNodeDTO machineNodeDTO = new SeqMachineNodeDTO();
            machineNodeDTO.setMachine(machine);
            machineNodeDTO.setNodes(v);
            machineNodeDTOList.add(machineNodeDTO);
        });

        StopWatch stopWatch = new StopWatch();

        //复制安装包到机器并修改配置
        stopWatch.start();
        boolean check = seqMachineOperation.pullImage(cluster, machineNodeDTOList.toArray(new SeqMachineNodeDTO[0]));
        stopWatch.stop();
        LOG.info("推送部署包到目标机器耗时：{}", stopWatch.getTotalTimeMillis());
        if (!check) {
            return ResultUtils.error("复制安装包到机器未通过");
        }

        // 序列服务集群初始化

        //持久化节点信息到数据库
        seqNodeService.saveClusterNode(installationParam.getSeqServiceNodeList(), cluster.getId());

        /*
         * cluster.setClusterMonitorStatus(ClusterState.HEALTH.getKey());
         * cluster.setClusterRunStatus(RedisNodeInfoConsts.STATUS_RUNNING);
         * cluster.setTotalNodes(redisNodeList.size());
         * cluster.setRunNodeNum(redisNodeList.size());
         * cluster.setNodes(RedisUtil.getNodeString(redisNodeList));
         */
        cluster.setUpdateDate(new Date());
        cluster.setStatus(SeqConstant.SEQ_SERVER_ONE);
        seqClusterService.updateObject(cluster);
        return ResultUtils.success();
    }

    /**
     * 序列服务集群
     *
     * @return Result
     */
    @ApiOperation(value = "序列服务集群 List")
    @GetMapping("/queryList")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列服务集群列表查询", componentType = OperateTypeAspect.SEQ)
    public Result queryList() {
        return ResultUtils.success(ResultEnum.SUCCESS, seqClusterService.queryList(new SeqServiceCluster()));
    }

    /**
     * 两地三中心-序列集群
     *
     * @param multiClusterId multiClusterId
     * @return Result
     */
    @ApiOperation(value = "两地三中心-序列集群 List")
    @GetMapping("/multiCenterSeqList/{multiClusterId}")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "两地三中心-序列集群List", componentType = OperateTypeAspect.SEQ)
    public Result multiCenterSeqList(@PathVariable("multiClusterId") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String multiClusterId) {
        SeqMulticenterNode node = new SeqMulticenterNode();
        node.setMultiClusterId(multiClusterId);
        List<SeqMulticenterNode> registerInstances = multicenterNodeService.queryNodes(node);
        ArrayList<String> clusterIds = new ArrayList<>();
        registerInstances.forEach(multiCenter -> clusterIds.add(multiCenter.getSeqClusterId()));
        HashMap<String, Object> map = new HashMap<>();
        map.put("clusterIds", clusterIds);
        map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ONE);
        List<SeqServiceCluster> seqServiceClusters = seqClusterService.queryListByMap(map);
        return ResultUtils.success(ResultEnum.SUCCESS, seqServiceClusters);
    }

    /**
     * 序列服务集群启动
     *
     * @param cluster cluster
     * @return Result
     */
    @ApiOperation(value = "序列服务集群启动")
    @PostMapping("/start")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列服务集群启动", componentType = OperateTypeAspect.SEQ)
    public Result start(@RequestBody SeqServiceCluster cluster) {
        LOG.info("序列服务集群启动信息 = {}", cluster);
        return seqClusterService.start(cluster);
    }

    /**
     * 序列服务集群停止
     *
     * @param cluster cluster
     * @return Result
     */
    @ApiOperation(value = "序列服务集群停止")
    @PostMapping("/stop")
    @AuditLog(operateType = OperateTypeAspect.POST, modelName = "序列服务集群停止", componentType = OperateTypeAspect.SEQ)
    public Result stop(@RequestBody SeqServiceCluster cluster) {
        LOG.info("序列服务集群停止信息 = {}", cluster);
        return seqClusterService.stop(cluster);
    }

    /**
     * 集群详情查询
     *
     * @param cluster cluster
     * @return Result
     */
    @ApiOperation(value = "集群详情查询")
    @GetMapping("/getClusterInfo")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "集群详情查询", componentType = OperateTypeAspect.SEQ)
    public Result getClusterInfo(@RequestBody SeqServiceCluster cluster) {
        SeqServiceClusterVo clusterInfo = seqClusterService.getClusterInfo(cluster);
        return ResultUtils.success(clusterInfo);
    }

    /**
     * 通过集群id获取集群信息以及结点信息
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "通过集群id获取集群信息以及结点信息")
    @GetMapping("/getClusterConfig/{id}")
    @AuditLog(operateType = OperateTypeAspect.GET, modelName = "集群对接示例", componentType = OperateTypeAspect.SEQ)
    public Result getClusterConfig(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String id) {
        StringBuilder str = new StringBuilder();
        ClusterConfigInfo clusterConfigInfo = new ClusterConfigInfo();
        SeqServiceCluster seqServiceCluster = seqClusterService.getObjectById(id);
        if (Objects.nonNull(seqServiceCluster)) {
            clusterConfigInfo.setSdkName(seqServiceCluster.getName());
            clusterConfigInfo.setServerName(seqServiceCluster.getName());
            Map<String, String> map = new HashMap<>();
            map.put("clusterId", seqServiceCluster.getId());
            map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ONE);
            seqNodeService.queryList(map).forEach(node -> {
                str.append(node.getHostIp()).append(":").append(node.getPort()).append(",");
            });
            clusterConfigInfo.setServerUrls(str.length() == 0 ? str.toString() : str.deleteCharAt(str.length() - 1).toString());
        }
        return ResultUtils.success(clusterConfigInfo);
    }
}
