package com.dap.paas.console.seq.util.jsch;

import com.base.api.exception.ServiceException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;

/**
 * @className JschOperator
 * @description Jsch操作
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class JschOperator {

    private final Logger logger = LoggerFactory.getLogger(JschOperator.class);

    public static final String CHANNEL_SESSION = "session";

    public static final String CHANNEL_SHELL = "shell";

    public static final String CHANNEL_EXEC = "exec";

    public static final String CHANNEL_X11 = "x11";

    public static final String CHANNEL_AUTH_OPEN_SSH = "auth-agent@openssh.com";

    public static final String CHANNEL_DIRECT_TCP_IP = "direct-tcpip";

    public static final String CHANNEL_FORWARDED_TCPIP = "forwarded-tcpip";

    public static final String CHANNEL_SFTP = "sftp";

    public static final String CHANNEL_SUBSYSTEM = "subsystem";

    /**
     * 当前操作系统的换行符
     */
    public static final String SYS_LINE_SEPARATOR = System.lineSeparator();

    public static final Integer CONS_BYTE_UNIT = 1024;

    private final String host;

    private final String userName;

    private Session session;

    private UserInfo userInfo;

    private Channel channel;

    private final static int TIME_OUT = 180000;

    private final static long INTERVAL = 3000L;

    private int port = 22;

    public Channel getChannel() {
        return this.channel;
    }

    public JschOperator(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.userInfo = new LinuxUserInfo(password);
    }

    public JschOperator(String host, String userName, String password, int port) {
        this.port = port;
        this.host = host;
        this.userName = userName;
        this.userInfo = new LinuxUserInfo(password);
    }

    public void openSession() throws JSchException {
        JSch jsch = new JSch();
        this.session = jsch.getSession(this.userName, this.host, port);
        this.session.setUserInfo(this.userInfo);
        session.setTimeout(TIME_OUT);
        Properties sshConfig = new Properties();
        sshConfig.put("userauth.gssapi-with-mic", "no");
        sshConfig.put("StrictHostKeyChecking", "no");
        this.session.setConfig(sshConfig);
        this.session.connect();
        logger.info("connect to {}@{} success.", this.userName, this.host);
    }

    public void openChannel(String channelType) throws JSchException {
        this.channel = this.session.openChannel(channelType);
        if (JschOperator.CHANNEL_SFTP.equals(channelType)) {
            this.channel.connect();
        }
    }

    public void connectChannel() throws JSchException {
        this.channel.connect();
    }

    /**
     * 执行shell命令
     *
     * @param command 执行的shell命令
     * @return 返回shell执行的状态码和shell的标准输出
     */
    public ShellResult execute(final String command) {
        int shellExitStatus = 0;
        ShellResult shellResult;
        try {
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setCommand(command);

            channelExec.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            BufferedReader errorInput = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));

            channelExec.connect();
            logger.info("The remote command is :{}", command);

            // 接收远程服务器执行命令的输出信息
            StringBuilder shellOutput = new StringBuilder();
            String temp;
            while (StringUtils.isNotBlank(temp = input.readLine())) {
                shellOutput.append(temp).append(SYS_LINE_SEPARATOR);
            }
            while ((temp = errorInput.readLine()) != null) {
                shellOutput.append(temp).append(SYS_LINE_SEPARATOR);
            }
            input.close();

            // 得到returnCode
            if (channelExec.isClosed()) {
                shellExitStatus = channelExec.getExitStatus();
            }
            shellResult = new ShellResult();
            shellResult.setExitStatus(shellExitStatus);
            shellResult.setShellOutPut(shellOutput.toString());
        } catch (Exception e) {
            logger.error("执行远程shell脚本已异常,command:{},异常信息:", command, e);
            throw new ServiceException(e.getMessage());
        }
        return shellResult;
    }

    /**
     * executeExec
     *
     * @param command 执行的shell命令
     * @return 返回shell执行的状态码和shell的标准输出和标准错误
     * @throws Exception Exception
     */
    public ShellResult executeExec(String command) throws Exception {
        StringBuffer shellOutput = new StringBuffer();
        int shellExitStatus;
        ShellResult shellResult = new ShellResult();
        try {
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setCommand(command);
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err);
            InputStream in = channelExec.getInputStream();
            channelExec.connect();
            byte[] tmp = new byte[CONS_BYTE_UNIT];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, CONS_BYTE_UNIT);
                    if (i < 0) {
                        break;
                    }
                    shellOutput.append(new String(tmp, 0, i));
                }
                if (channelExec.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    shellExitStatus = channelExec.getExitStatus();
                    break;
                }
            }
            logger.info("shell command:{} exitStatus is:{} output is:{}", command, shellExitStatus, shellOutput);
            shellResult.setExitStatus(shellExitStatus);
            shellResult.setShellOutPut(shellOutput.toString());
        } catch (JSchException e) {
            logger.error("执行远程shell脚本已异常,command:{},异常信息:", command, e);
        } catch (IOException e) {
            logger.error("执行远程shell脚本已异常,command:{},异常信息:", command, e);
            throw e;
        }
        return shellResult;
    }

    /**
     * 下载文件-sftp协议.
     *
     * @param sourceFile 下载的数据源文件
     * @param targetFile 存在本地的路径
     * @return 文件
     * @throws Exception 异常
     */
    public File download(final String sourceFile, final String targetFile) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) this.channel;
        FileOutputStream os = null;
        File file = new File(targetFile);
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                    throw new Exception("创建文件失败，file:" + parentFile.getPath());
                }
                if (!file.createNewFile()) {
                    throw new Exception("创建文件失败，file:" + targetFile);
                }
            }
            os = new FileOutputStream(file);
            channelSftp.get(sourceFile, os);

        } catch (Exception e) {
            channelSftp.exit();
            throw e;
        } finally {
            if (null != os) {
                os.close();
            }
        }
        return file;
    }

    /**
     * 上传文件-sftp协议.
     *
     * @param srcFile  源文件
     * @param dir      保存路径
     * @param fileName 保存文件名
     * @throws Exception 异常
     */
    private void uploadFile(final String srcFile, final String dir, final String fileName) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) this.channel;

        logger.info("srcFile:{},dir:{},fileName:{},sftp:{}", srcFile, dir, fileName, channelSftp);
        mkDir(dir);
        channelSftp.cd(dir);
        channelSftp.put(srcFile, fileName);
    }

    /**
     * 上传文件-sftp协议.
     *
     * @param sourceFileAbsolutePath 源文件绝对路径，/xxx/xxx.zip 或 x:/xxx/xxx.zip;
     * @param targetPath             目标绝对路径，/xxx/xxx 或 x:/xxx/xxx.zip;
     * @throws Exception 异常
     */
    public void uploadFile(final String sourceFileAbsolutePath, final String targetPath) throws Exception {
        try {
            File file = new File(sourceFileAbsolutePath);
            if (file.exists()) {
                String fileName = file.getName();
                uploadFile(sourceFileAbsolutePath, targetPath, fileName);
            } else {
                logger.error("{},文件不存在！", sourceFileAbsolutePath);
                throw new Exception(sourceFileAbsolutePath + "文件不存在！");
            }
        } catch (Exception e) {
            logger.error("上传文件异常，file:{},异常信息:{}", sourceFileAbsolutePath, e);
            throw e;
        }
    }

    /**
     * 根据路径创建文件夹.
     *
     * @param dir 路径 必须是 /xxx/xxx/xxx/ 不能就单独一个/
     * @throws Exception 异常
     */
    public boolean mkDir(final String dir) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) this.channel;
        try {
            if (StringUtils.isEmpty(dir)) {
                return false;
            }
            if (!StringUtils.startsWith(dir, "/") || dir.length() == 1) {
                return false;
            }
            mkdirs(dir);
        } catch (Exception e) {
            channelSftp.exit();
            throw e;
        }
        return true;
    }

    /**
     * 递归创建文件夹.
     *
     * @param dir 路径
     * @throws SftpException 异常
     */
    public void mkdirs(final String dir) throws SftpException {
        ChannelSftp channelSftp = (ChannelSftp) this.channel;
        String dirs = dir.substring(1);
        String[] dirArr = StringUtils.split(dirs, "/");
        String base = "";
        for (String d : dirArr) {
            base += "/" + d;
            if (!dirExist(base)) {
                channelSftp.mkdir(base);
            }
        }
    }

    /**
     * 判断文件夹是否存在.
     *
     * @param dir 文件夹路径， /xxx/xxx/
     * @return 是否存在
     */
    private boolean dirExist(final String dir) {
        ChannelSftp channelSftp = (ChannelSftp) this.channel;
        try {
            Vector<?> vector = channelSftp.ls(dir);
            return null != vector;
        } catch (SftpException e) {
            return false;
        }
    }

    /**
     * 格式化路径.
     *
     * @param srcPath 原路径. /xxx/xxx/xxx.yyy 或 X:/xxx/xxx/xxx.yy
     * @return list, 第一个是路径（/xxx/xxx/）,第二个是文件名（xxx.yy）
     */
    private static List<String> formatPath(final String srcPath) {
        List<String> list = new ArrayList<>(2);
        String dir;
        String fileName;
        String repSrc = srcPath.replaceAll("\\\\", File.separator);
        int firstP = repSrc.indexOf(File.separator);
        int lastP = repSrc.lastIndexOf(File.separator);
        fileName = repSrc.substring(lastP + 1);
        dir = repSrc.substring(firstP, lastP);
        dir = dir.length() == 1 ? dir : (dir + File.separator);
        list.add(dir);
        list.add(fileName);
        return list;
    }

    /**
     * 截取文件名
     *
     * @param sourceFileAbsolutePath 文件绝对路径. /xxx/xxx/xxx.yyy 或 X:/xxx/xxx/xxx.yy
     * @return 文件名（xxx.yy）
     */
    private static String getFileName(final String sourceFileAbsolutePath) {
        String fileName;
        String repSrc = sourceFileAbsolutePath.replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
        int lastP = repSrc.lastIndexOf(File.separator);
        fileName = repSrc.substring(lastP + 1);
        return fileName;
    }

    public void downloadFileProgress(String src, String dst, ChannelSftp channelSftp) throws Exception {
        channelSftp.get(src, dst, new FileProgressMonitor(), ChannelSftp.OVERWRITE);
        channelSftp.quit();
    }

    private class FileProgressMonitor implements SftpProgressMonitor {

        @Override
        public void init(int op, String src, String dest, long max) {
        }

        @Override
        public boolean count(long count) {
            return false;
        }

        @Override
        public void end() {
        }
    }

    public void close() {
        closeSession();
        closeChannel();
    }

    public void closeSession() {
        if (this.session != null) {
            this.session.disconnect();
        }
    }

    public void closeChannel() {
        if (this.channel != null) {
            this.channel.disconnect();
        }
    }
}
