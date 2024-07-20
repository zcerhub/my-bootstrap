package com.dap.paas.console.basic.utils;

import com.base.api.exception.ServiceException;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.vo.ReadFileVo;
import com.dap.paas.console.common.constants.BootConstant;
import com.dap.paas.console.common.exception.DapWebServerException;
import com.dap.paas.console.common.util.jsch.JschOperator;
import com.dap.paas.console.common.util.jsch.ShellResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * SSH部署
 */
public class SshDeployServerUtil {
    protected final static Logger LOGGER = LoggerFactory.getLogger(SshDeployServerUtil.class);
    public static JschOperator getConnection(String host, int port, String username, String password) throws Exception {
        JschOperator jr = new JschOperator(host, username, password, port);
        jr.openSession();
        return jr;
    }

    public static String execCommand(Machine machine, String command) throws IOException {
        JschOperator jr = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            ShellResult result = jr.executeExec(command);

            return result.getShellOutPut();
        } catch (Exception e) {
            LOGGER.error("error", e);
            return null;
        } finally {
            if (jr != null) {
                jr.close();
            }
        }
    }

    public static boolean checkDeploy(Machine machine, String deployPath) {
        try {
            String command = "[ -d " + deployPath + "/bin ] && echo 'find folder' || echo 'cant find folder'";
            String result = SshDeployServerUtil.execCommand(machine, command);
            if (Objects.nonNull(result) && result.contains("cant find folder")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("error", e);
            return false;
        }
    }


    public static ExecResult deploy(String logId, EchoFunction echoFunction, String source, Machine machine, String packagePath, String deployPath) throws IOException {

        StringBuffer bufferLog = new StringBuffer();

        packagePath = packagePath + "/package/";


        JschOperator jr = new JschOperator(machine.getHostIp(), machine.getHostSshAccount(), machine.getHostSshPassword(), machine.getHostPort());
        try {
            jr.openSession();
            // jr.openChannel(JschOperator.CHANNEL_SFTP);
            //jr.openChannel(JschOperator.CHANNEL_EXEC);
            /*jr.mkdirs(deployPath);
            logger.info("创建部署目录:{}", deployPath );*/
            //上传文件到目标服务器
            //jr.uploadFile(localFile , tarDeployPath);

            // logger.info("上传部署包到机器:{} , 上传到机器的地址:{} ", machine.getHostIp() +"@"+ machine.getHostSshAccount() , tarDeployPath );

            StringBuilder shell = new StringBuilder();
            shell.append("[ -d " + packagePath + " ] && echo 'find folder' || mkdir -p " + packagePath);
            shell.append("[ -d " + deployPath + " ] && echo 'find folder' || mkdir -p " + deployPath);

            jr.executeExec(shell.toString());

            shell = new StringBuilder();
            shell.append("[ -d " + deployPath + " ] && echo 'find folder' || echo 'cant find folder' ");
            ShellResult result = jr.executeExec(shell.toString());
            if (Objects.nonNull(result) && result.getShellOutPut().contains("cant find folder")) {
                log(logId, ">>> 部署失败无该目录权限-" + machine.getHostIp() + "-" + packagePath, echoFunction, bufferLog, true);
                return new ExecResult(ExecResult.ERROR, bufferLog.toString());
            } else {
                File sourceFile = new File(source);
                String log = ">>> 开始部署至 " + machine.getHostIp();
                log(logId, log, echoFunction, bufferLog, true);
                if (sourceFile.length() <= 0) {
                    log(logId, ">>>文件不存在--" + source, echoFunction, bufferLog, true);
                    return new ExecResult(ExecResult.ERROR, bufferLog.toString());
                }

                shell = new StringBuilder();
                shell.append("cksum " + packagePath + sourceFile.getName() + " | awk '{print $2}'");
                ShellResult result_cksum = jr.executeExec(shell.toString());
                if (!StringUtils.isEmpty(result_cksum.getShellOutPut()) && result_cksum.getShellOutPut().equals(String.valueOf(sourceFile.length()))) {
                    log = ">>> 部署文件已存在 " + machine.getHostIp();
                    log(logId, log, echoFunction, bufferLog, true);
                } else {
                    log = ">>> 正在复制部署文件至" + machine.getHostIp() + " 请耐心等待 ...";
                    log(logId, log, echoFunction, bufferLog, true);

                    jr.openChannel(JschOperator.CHANNEL_SFTP);
                    jr.uploadFile(source, packagePath);

                    log(logId, ">>> 复制文件至 " + machine.getHostIp() + " 完成 ...", echoFunction, bufferLog, true);
                }

                synchronized (echoFunction) {
                    shell = new StringBuilder();
                    if (FileUtil.suffixFromFileName(sourceFile.getName()).equals("gz"))
                        shell.append("tar -zvxf " + packagePath + sourceFile.getName() + " -C " + deployPath);
                    else if (FileUtil.suffixFromFileName(sourceFile.getName()).equals("zip")) {
                        shell.append("unzip " + packagePath + sourceFile.getName() + " -d " + deployPath);
                    }

                    jr.openChannel(JschOperator.CHANNEL_EXEC);
                    ShellResult result_tar = jr.executeExec(shell.toString());
                    log(logId, result_tar.getShellOutPut(), echoFunction, bufferLog, true);

                }

                log(logId, ">>> " + machine.getHostIp() + " 解压文件完成，部署完毕。", echoFunction, bufferLog, true);

                return new ExecResult(ExecResult.SUCCESS, bufferLog.toString());
            }

        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new DapWebServerException("上传部署包到机器:" + machine.getHostIp() + "失败 " + e.getMessage());
        } finally {
            jr.close();
        }

    }

    public synchronized static ExecResult start(String logId, EchoFunction echoFunction, Machine machine,
                                                String configPath, String configName, String config, String command) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        JschOperator jr = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            log(logId, ">>> 正在主机" + machine.getHostIp() + "启动服务 ...", echoFunction, bufferLog, true);

            if (!StringUtils.isEmpty(configPath)) {
                ShellResult shellResult = jr.executeExec("[ -d " + configPath + " ] && echo 'find folder' || mkdir -p " + configPath);
//                if (shellResult.getExitStatus() != -1){
//                    jr.executeExec("[ -d " + configPath + " ] ; and echo 'find folder' ; or mkdir -p " + configPath);
//                }
            }
            log(logId, ">>> 部署主机" + machine.getHostIp() + "配置文件 ...", echoFunction, bufferLog, true);

            List<String> configs = Arrays.asList(config.split(BootConstant.LINE_SEPARATOR));
            StringBuffer cmd = new StringBuffer();
            for (String s : configs) {
                if (cmd.length() == 0) {
                    cmd.append("echo '" + s + "' > " + configPath + configName + ";");
                } else {
                    cmd.append("echo '" + s + "' >> " + configPath + configName + ";");
                }
            }

            ShellResult result = jr.executeExec(cmd.toString());

            log(logId, result.getShellOutPut(), echoFunction, bufferLog, true);

            result = jr.executeExec(command);

            log(logId, ">>> 主机" + machine.getHostIp() + "启动命令发送成功 ...", echoFunction, bufferLog, true);
            log(logId, result.getShellOutPut(), echoFunction, bufferLog, true);

            return new ExecResult(ExecResult.SUCCESS, bufferLog.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log(logId, ">>> 实例启动失败," + e.getMessage(), echoFunction, bufferLog, true);
            log(logId, "command:" + command, echoFunction, bufferLog, true);
            LOGGER.error("error", e);
            return new ExecResult(ExecResult.ERROR, e.getMessage());
        } finally {
            jr.close();
        }

    }

    public synchronized static ExecResult stop(String logId, EchoFunction echoFunction, Machine machine, String command) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        JschOperator jr = null;
        ShellResult result = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            log(logId, ">>> 发送停止命令 ...", echoFunction, bufferLog, true);

            result = jr.executeExec(command);

            log(logId, ">>> 发送停止命令完毕。", echoFunction, bufferLog, true);
            log(logId, result.getShellOutPut(), echoFunction, bufferLog, true);
            return new ExecResult(ExecResult.SUCCESS, bufferLog.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("error", e);
            log(logId, ">>> 实例停止失败," + e.getMessage(), echoFunction, bufferLog, true);
            log(logId, "command:" + command, echoFunction, bufferLog, true);
            return new ExecResult(ExecResult.ERROR, e.getMessage());
        } finally {
            jr.close();
        }

    }

    public static ExecResult uninstall(String logId, EchoFunction echoFunction, Machine machine, String path) throws IOException {
        if (StringUtils.isBlank(path)) {
            //防止直接删除整个文件夹
            throw new IllegalArgumentException("路径不能为空....");
        }
        StringBuffer bufferLog = new StringBuffer();

        JschOperator jr = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            log(logId, ">>> 移除文件夹 ...", echoFunction, bufferLog, true);

            ShellResult result = jr.executeExec("rm -rf " + path + "/");

            log(logId, result.getShellOutPut(), echoFunction, bufferLog, true);

            log(logId, ">>> uninstall success...", echoFunction, bufferLog, true);
            return new ExecResult(ExecResult.SUCCESS, bufferLog.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("error", e);
            return new ExecResult(ExecResult.ERROR, e.getMessage());
        } finally {
            jr.close();
        }
    }


    public static ReadFileVo readFile(Machine machine, String path) throws IOException {
        int maxLength = 33990;
        return readLogs(machine, String.format("tail -c %d %s", maxLength, path));
    }

    /**
     * @param machine
     * @param path
     * @param pageNum 第1页 0
     * @return
     * @throws IOException
     */
    public static ReadFileVo readPageFile(Machine machine, String path, int pageNum) throws IOException {
        int maxLength = 500;
        return readLogs(machine, String.format("sed -n '%d,%dp' %s", maxLength * pageNum + 1, maxLength * (pageNum + 1), path));

    }


    public static ReadFileVo readLogs(Machine machine, String command) throws IOException {

        ReadFileVo vo = new ReadFileVo();
        JschOperator jr = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            ShellResult result = jr.executeExec(command);
            String readString = result.getShellOutPut();
            vo.setContent(readString.replace("\n", "\r\n"));
            return vo;
        } catch (Exception e) {
            LOGGER.error("error", e);
            vo.setContent("读取文件失败" + e.getMessage());
            return vo;
        } finally {
            jr.close();
        }

    }

    public static void createPath(Machine machine, String path) throws IOException {

        JschOperator jr = null;
        try {
            jr = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

            jr.mkDir(path);
        } catch (Exception e) {
            LOGGER.error("error", e);
        } finally {
            jr.close();
        }
    }

    private static void log(String logId, String logStr, EchoFunction echoFunction, StringBuffer stringBuffer, boolean append) {
        LOGGER.info(logStr);
        echoFunction.print(logId, logStr);
        if (append) {
            stringBuffer.append(logStr + "\r\n");
        }
    }

    private static void dealInputStream(InputStream inputStream, String logId, EchoFunction echoFunction, StringBuffer stringBuffer, boolean append) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log(logId, line, echoFunction, stringBuffer, append);
        }
        try {
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
    }

    public static String readStdLine(InputStream inputStream) {
        String line = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String appender = "";
            while ((appender = bufferedReader.readLine()) != null) {
                line = line + appender;
            }
            inputStream.close();
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
        return line;
    }

    public static String readStdFirstLine(InputStream inputStream) {
        String line = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            line = bufferedReader.readLine();
            inputStream.close();
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
        return line;
    }

    public static void checkDisk(Machine machine, float maxPercent, int minMem) throws IOException {
        //   Connection connection = SshDeployServerUtil.getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
        try {
            //    Session session = connection.openSession();
            synchronized (machine.getId().intern()) {
//                   session.execCommand("df -h ./|awk '{print $5}';", CharsetConsts.DEFAULT_ENCODING);
//                   String line = SshDeployServerUtil.readStdLine(session.getStdout());
                String command = "df -h ./|awk '{print $5}';";
                String line = SshDeployServerUtil.execCommand(machine, command);

                float percent = Float.parseFloat(line.split("%")[1]);
                // session.close();
                if (percent > maxPercent) {
                    throw new ServiceException("机器磁盘容量不足, 已使用" + percent + "%");
                }
                // session = connection.openSession();
                //  session.execCommand("free -m | grep Mem|awk '{print $2,$3}';", CharsetConsts.DEFAULT_ENCODING);
                //  line = SshDeployServerUtil.readStdLine(session.getStdout());

                command = "free -m | grep Mem|awk '{print $2,$3}';";
                line = SshDeployServerUtil.execCommand(machine, command);

                String[] mem = StringUtils.split(line.trim(), " ");
                long all = Long.parseLong(mem[0]);
                long used = Long.parseLong(mem[1]);
                long freeMem = all - used;
                //  session.close();
                if (freeMem < minMem) {
                    throw new ServiceException("机器内存不足, 剩余" + freeMem + "MB");
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        } finally {
            //   connection.close();
        }
    }

    public static boolean checkJavaHome(Machine machine) {
        boolean javaHome = false;
        try {
            synchronized (machine.getId().intern()) {
                String command = "grep JAVA_HOME= /etc/profile;";
                String line = SshDeployServerUtil.execCommand(machine, command);

                if (!"".equals(line) && line.contains("jdk")) {
                    javaHome = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }

        return javaHome;
    }

    public static boolean checkJavaHomeExtent(Machine machine) {
        boolean javaHomeIsOk = true;
        try {
            synchronized (machine.getId().intern()) {
                String shPath = "/home/" + machine.getHostSshAccount();
                String shFile = shPath + "/javaHomeCheck.sh";
                StringBuffer command = new StringBuffer("[ -d \" " + shPath + "\" ] && echo 'find folder' || mkdir -p " + shPath + ";");
                command.append("touch " + shFile + ";");
                command.append("echo '#!/bin/bash' >   " + shFile + "; ");
                command.append("echo '[ ! -e \"$JAVA_HOME/bin/java\" ] && JAVA_HOME=$HOME/jdk/java' >>   " + shFile + "; ");
                command.append("echo '[ ! -e \"$JAVA_HOME/bin/java\" ] && JAVA_HOME=/usr/java' >>   " + shFile + "; ");
                command.append("echo '[ ! -e \"$JAVA_HOME/bin/java\" ] && echo \"JAVA_HOME not exist!\"' >> " + shFile + "; ");
                command.append("chmod -R 777 " + shFile + "; ");
                command.append("sh  " + shFile + "; ");
                String cmd = command.toString();
                String line = SshDeployServerUtil.execCommand(machine, cmd);
                if (!"".equals(line) && line.contains("JAVA_HOME not exist")) {
                    LOGGER.error("checkJavaHomeExtent{}", cmd);
                    javaHomeIsOk = false;
                }
                SshDeployServerUtil.execCommand(machine, "rm  rf " + shFile);
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }
        return javaHomeIsOk;
    }

//    public static void main(String[] args) {
//        Machine machine = new Machine();
//        machine.setId("1234123413");
//        machine.setHostSshAccount("root");
//        machine.setHostIp("10.64.15.96");
//        machine.setHostPort(22);
//        machine.setHostSshPassword("Gientech2022!");
//        boolean b = checkJavaHomeExtent(machine);
//        System.out.println(b);
//    }

    /**
     * 检测java环境
     * @param machine
     * @return
     */
    public static boolean checkJavaEnv(Machine machine) {
        boolean javaHome = false;
        try {
            synchronized (machine.getId().intern()) {
                String command = "which java;";
                String line = SshConnectionManager.executeCommands(machine, command);

                if (!"".equals(line) && (line.contains("jdk") || line.contains("java"))) {
                    javaHome = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }

        return javaHome;
    }

    public static boolean checkDisk(Machine machine, float maxPercent) {
        boolean diskFlag = false;
        try {
            synchronized (machine.getId().intern()) {
                String command = "df -h ./|awk '{print $5}';";
                String line = SshDeployServerUtil.execCommand(machine, command);

                float percent = Float.parseFloat(line.split("%")[1]);
                if (percent > maxPercent) {

                    diskFlag = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }
        return diskFlag;
    }

    public static boolean checkMemory(Machine machine, int minMem) {
        boolean memoryFlag = false;
        try {
            synchronized (machine.getId().intern()) {

                String command = "free -m | grep Mem|awk '{print $2,$3}';";
                String line = SshDeployServerUtil.execCommand(machine, command);

                String[] mem = StringUtils.split(line.trim(), " ");
                long all = Long.parseLong(mem[0]);
                long used = Long.parseLong(mem[1]);
                long freeMem = all - used;
                if (freeMem < minMem) {
                    memoryFlag = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }
        return memoryFlag;
    }

    /**
     * 检测内存，取值（完全未被使用的内存）
     *
     * @param machine
     * @param minMem
     * @return true-内存不足，false-内存足
     */
    public static boolean checkMemoryFree(Machine machine, int minMem) {
        boolean memoryFlag = false;
        try {
            synchronized (machine.getId().intern()) {
                String command = "free -m | grep Mem|awk '{print $2,$3,$4}';";
                String line = SshDeployServerUtil.execCommand(machine, command);
                String[] mem = StringUtils.split(line.trim(), " ");
                //long all = Long.parseLong(mem[0]);
                //long used = Long.parseLong(mem[1]);
                //long freeMem = all - used;
                long free = Long.parseLong(mem[2]);
                if (free < minMem) {
                    memoryFlag = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            throw new ServiceException(e.getMessage());
        }
        return memoryFlag;
    }

}
