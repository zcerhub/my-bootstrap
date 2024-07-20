package com.dap.paas.console.basic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.common.util.jsch.JschOperator;
import com.dap.paas.console.common.util.jsch.ShellResult;

import ch.ethz.ssh2.Connection;
import org.apache.commons.lang.StringUtils;

/**
 * @author zpj
 * @date 9/15/2021
 */
public class SSH2Util {

    private SSH2Util() {
    }

    public static void wget(Machine machine, String targetPath, String fileName, String remoteUrl, boolean sudo) throws Exception {
        rm(machine, targetPath + fileName, sudo);
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        String template = "/usr/bin/wget -P %s %s";
        command.append(String.format(template, targetPath, remoteUrl));
        String execute = execute(machine, command.toString());
    }

    /**
     * copy file
     *
     * @param machine
     * @param originalPath
     * @param targetPath
     * @throws Exception
     */
    public static String copy(Machine machine, String originalPath, String targetPath, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        String template = "cp %s %s";
        command.append(String.format(template, originalPath, targetPath));
        return execute(machine, command.toString());
    }

    /**
     * copy file
     *
     * @param machine
     * @param originalPath
     * @param targetPath
     * @throws Exception
     */
    public static String copy2(Machine machine, String originalPath, String targetPath, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        // 删除旧的数据，如果有旧数据的话
        String rmTemplate = "rm -rf %s;";
        command.append(String.format(rmTemplate, targetPath));
        if (sudo) {
            command.append("sudo ");
        }
        // create directory
        String mkdirTemplate = "mkdir -p %s;";
        command.append(String.format(mkdirTemplate, targetPath));
        if (sudo) {
            command.append("sudo ");
        }
        String template = "cp %s %s";
        command.append(String.format(template, originalPath, targetPath));
        return execute(machine, command.toString());
    }

    public static String copyFileToRemote(Machine machine, String tempPath, String url, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        // 删除旧的数据，如果有旧数据的话
        String rmTemplate = "rm -rf %s;";
        command.append(String.format(rmTemplate, tempPath));
        if (sudo) {
            command.append("sudo ");
        }
        // create directory
        String mkdirTemplate = "mkdir -p %s;";
        command.append(String.format(mkdirTemplate, tempPath));
        if (sudo) {
            command.append("sudo ");
        }
        // 本机拷贝至安装机器节点
        String wgetTemplate = "/usr/bin/wget -P %s %s;";
        command.append(String.format(wgetTemplate, tempPath, url));
        return SSH2Util.execute(machine, command.toString());
    }

    public static String rm(Machine machine, String filePath, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        String template = "rm -rf %s";
        command.append(String.format(template, filePath));
        return execute(machine, command.toString());
    }

    public static String unzipToTargetPath(Machine machine, String filePath, String targetPath, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        String template = "tar -xzf %s -C %s --strip-components 1";
        command.append(String.format(template, filePath, targetPath));
        return execute(machine, command.toString());
    }
    
    public static String targzToTargetPath(Machine machine, String filePath, String targetPath, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        String template = "tar -zxvf %s -C %s --strip-components 1";
        command.append(String.format(template, filePath, targetPath));
        return execute(machine, command.toString());
    }

    /**
     * mkdir -p
     *
     * @param machine
     * @param path
     * @throws Exception
     */
    public static String mkdir(Machine machine, String path, boolean sudo) throws Exception {
        StringBuilder command = new StringBuilder();
        if (sudo) {
            command.append("sudo ");
        }
        command.append("mkdir -p ").append(path);
        return execute(machine, command.toString());
    }

    /*public static void createFile(Machine machine, String path, boolean sudo) throws Exception {
        rm(machine, path + REDIS_CONF, sudo);
        StringBuffer command = new StringBuffer();
        command.append("cd ").append(path).append(";");
        if (sudo) {
            command.append("sudo ");
        }
        command.append("touch ").append("redis.conf;");
        if (sudo) {
            command.append("sudo ");
        }
        command.append("sh -c \"echo '#redis config' > redis.conf; echo 'bind 101.1.2.2' >> redis.conf\";");
        System.err.println(command.toString());
        String execute = execute(machine, command.toString());
        System.err.println(execute);
    }*/

    /**
     * 执行Shell脚本或命令
     *
     * @param machine
     * @param commands
     * @return
     */
    public static String execute(Machine machine, String commands) throws Exception {
    	JschOperator jr = null;
		try {
			jr = new JschOperator(machine.getHostIp(), machine.getHostSshAccount() , machine.getHostSshPassword() , machine.getHostPort());
	        jr.openSession();
			ShellResult result = jr.execute(commands);
            return result.getShellOutPut();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        	jr.close();
        }
  }

    @Deprecated
    public static String localExecute(String command) throws Exception {
        String result;
        String[] cmds = {"/bin/sh", "-c", command};
        Process ps = Runtime.getRuntime().exec(cmds);
        InputStream in = ps.getInputStream();
        result = processStandardOutput(in);
        InputStream errorIn = ps.getErrorStream();
        result += processStandardOutput(errorIn);
        return result;
    }

    /**
     * 解析流获取字符串信息
     *
     * @return
     */
    private static String processStandardOutput(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static Connection getConnection(Machine machine) throws Exception {
        String userName = machine.getHostSshAccount();
        String password = machine.getHostSshPassword();
        String host = machine.getHostIp();
        Connection connection;
        Integer sshPort = machine.getHostPort();
        if (sshPort == null) {
            connection = new Connection(host);
        } else {
            connection = new Connection(host, machine.getHostPort());
        }
        connection.connect();
        boolean success = connection.authenticateWithPassword(userName, password);
        if (!success) {
            throw new RuntimeException(host + " login failed.");
        }
        return connection;
    }

    private static void close(Connection connection) {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 检查库是否存在
     * @param machine 机器
     * @param libName 库名
     * @return true:存在 false:不存在
     * @throws Exception 异常
     */
    public static boolean checkLibExist(Machine machine, String libName) throws Exception {
        if (StringUtils.isBlank(libName)) {
            return false;
        }
        String execute = execute(machine, String.format("yum list installed | grep '\\<%s\\>'", libName));
        return StringUtils.isNotBlank(execute) && execute.contains(libName);
    }

    /**
     * 检查库是否存在
     * @param machine 机器
     * @param libName 库名
     * @return true:存在 false:不存在
     * @throws Exception 异常
     */
    public static boolean checkLinuxLibExist(Machine machine, String libName) throws Exception {
        boolean isLib = false;
        if (StringUtils.isBlank(libName)) {
            return false;
        }
        String execute = SshDeployServerUtil.execCommand(machine, String.format("which %s",libName));
        if (!"".equals(execute) && execute.contains("/"+libName)) {
            isLib = true;
        }
        return isLib;
    }
}
