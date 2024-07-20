package com.dap.paas.console.basic.utils;

import ch.ethz.ssh2.*;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.vo.ReadFileVo;
import com.dap.paas.console.common.constants.BootConstant;
import com.dap.paas.console.common.constants.CharsetConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * SSH部署
 */
@Slf4j
public class SshGanymedDeployServerUtil {
    public static Connection getConnection(String host, int port, String username, String password) throws IOException {

        Connection connection = new Connection(host, port);
        connection.connect();
        boolean isAuthed = connection.authenticateWithPassword(username,password);
        if(!isAuthed){
            throw new IllegalArgumentException("remote authenticate error...");
        }
        return connection;
    }

    public static ExecResult  deploy(String logId, EchoFunction echoFunction, String source, Machine machine, String packagePath, String deployPath) throws IOException {

        StringBuffer bufferLog = new StringBuffer();
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

        packagePath = packagePath + "/package/";
        Session session = connection.openSession();
        session.execCommand("[ -d " + packagePath + " ] && echo 'find folder' || mkdir -p "
                + packagePath, CharsetConsts.DEFAULT_ENCODING);
        session.close();
        session = connection.openSession();
        session.execCommand("[ -d " + deployPath + " ] && echo 'find folder' || mkdir -p "
                + deployPath, CharsetConsts.DEFAULT_ENCODING);
        session.close();
        session = connection.openSession();
        session.execCommand("[ -d " + deployPath + " ] && echo 'find folder' || echo 'cant find folder' ", CharsetConsts.DEFAULT_ENCODING);
        String result = readStdLine(session.getStdout());
        session.close();
        if (result.equals("cant find folder")) {
            log(logId,">>> 部署失败无该目录权限-" + machine.getHostIp() + "-" + packagePath,echoFunction,bufferLog,true);
            connection.close();
            return new ExecResult(ExecResult.ERROR, bufferLog.toString());
        }
        else {
            File sourceFile = new File(source);
            String log = ">>> 开始部署至 "+ machine.getHostIp();
            log(logId,log,echoFunction,bufferLog,true);
            if(sourceFile.length() <= 0) {
                log(logId,">>>文件不存在--" + source,echoFunction,bufferLog,true);
                return new ExecResult(ExecResult.ERROR, bufferLog.toString());
            }
            session = connection.openSession();
            session.execCommand("cksum " + packagePath + sourceFile.getName() + " | awk '{print $2}'", CharsetConsts.DEFAULT_ENCODING);
            result = readStdLine(session.getStdout());
            session.close();
            if(!StringUtils.isEmpty(result) && result.equals(String.valueOf(sourceFile.length()))) {
                log = ">>> 部署文件已存在 "+ machine.getHostIp();
                log(logId,log,echoFunction,bufferLog,true);
            } else {
                log = ">>> 正在复制部署文件至"+ machine.getHostIp() + " 请耐心等待 ...";
                log(logId,log,echoFunction,bufferLog,true);
                SCPClient scpClient = connection.createSCPClient();
                SCPOutputStream scpOutputStream = scpClient.put(sourceFile.getName(), sourceFile.length(),
                        packagePath, "0644");
                FileInputStream inputStream = new FileInputStream(sourceFile);
                int i;
                byte [] bytes = new byte[4096];
                while((i = inputStream.read(bytes)) != -1) {
                    scpOutputStream.write(bytes, 0, i);
                }
                try {
                    scpOutputStream.flush();
                } catch(Exception e){
                    e.printStackTrace();
                } finally {
                    scpOutputStream.close();
                    inputStream.close();
                }

                log(logId,">>> 复制文件至 " + machine.getHostIp() + " 完成 ...",echoFunction,bufferLog,true);
            }

            synchronized (echoFunction) {
                session = connection.openSession();
                if(FileUtil.suffixFromFileName(sourceFile.getName()).equals("gz")) {
                    session.execCommand("tar -zvxf " + packagePath + sourceFile.getName() +" -C " + deployPath);
                } else if(FileUtil.suffixFromFileName(sourceFile.getName()).equals("zip")){
                    session.execCommand("unzip " + packagePath + sourceFile.getName() +" -d " + deployPath);
                }
                InputStream stdout = session.getStdout();
                dealInputStream(stdout,logId,echoFunction,bufferLog,false);

                session.close();
            }

            log(logId,">>> " + machine.getHostIp() + " 解压文件完成，部署完毕。",echoFunction,bufferLog,true);
            connection.close();
            return new ExecResult(ExecResult.SUCCESS, bufferLog.toString());
        }
    }

    public synchronized static ExecResult start(String logId, EchoFunction echoFunction, Machine machine,
                                   String configPath, String configName, String config, String command) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

        log(logId,">>> 正在主机" + machine.getHostIp() + "启动服务 ...",echoFunction,bufferLog,true);

        Session session = connection.openSession();
        if(!StringUtils.isEmpty(configPath)) {
            session.execCommand("[ -d " + configPath + " ] && echo 'find folder' || mkdir -p "
                    + configPath, CharsetConsts.DEFAULT_ENCODING);
            session.close();
        }
        log(logId,">>> 部署主机" + machine.getHostIp() + "配置文件 ...",echoFunction,bufferLog,true);
        session = connection.openSession();
        List<String> configs = Arrays.asList(config.split(BootConstant.LINE_SEPARATOR));
        StringBuffer cmd = new StringBuffer();
        for(String s : configs) {
            if(cmd.length() == 0) {
                cmd.append("echo '" + s + "' > " + configPath + configName + ";");
            } else {
                cmd.append("echo '" + s + "' >> " + configPath + configName + ";");
            }
        }
        session.execCommand(cmd.toString(), CharsetConsts.DEFAULT_ENCODING);
        InputStream stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog,true);
        session.close();

        session = connection.openSession();
        session.execCommand(command, CharsetConsts.DEFAULT_ENCODING);
        log(logId,">>> 主机" + machine.getHostIp()  + "启动命令发送成功 ...",echoFunction,bufferLog,true);
        stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog,true);

        session.close();
        connection.close();
        return new ExecResult(ExecResult.SUCCESS,bufferLog.toString());
    }

    public synchronized static ExecResult stop(String logId, EchoFunction echoFunction, Machine machine, String command) throws IOException {
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());

        log(logId,">>> 发送停止命令 ...",echoFunction,bufferLog,true);

        Session session = connection.openSession();
        session.execCommand(command);
        log(logId,">>> 发送停止命令完毕。",echoFunction,bufferLog,true);
        InputStream stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog,true);
        session.close();
        connection.close();
        return new ExecResult(ExecResult.SUCCESS,bufferLog.toString());
    }

    public static ExecResult uninstall(String logId, EchoFunction echoFunction, Machine machine, String path) throws IOException {
        if(StringUtils.isBlank(path)){
            //防止直接删除整个文件夹
            throw new IllegalArgumentException("路径不能为空....");
        }
        StringBuffer bufferLog = new StringBuffer();
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
        log(logId,">>> 移除文件夹 ...",echoFunction,bufferLog,true);
        Session session = connection.openSession();
        session.execCommand("rm -rf " + path + "/");
        InputStream stdout = session.getStdout();
        dealInputStream(stdout,logId,echoFunction,bufferLog,true);
        log(logId,">>> uninstall success...",echoFunction,bufferLog,true);
        session.close();
        connection.close();
        return new ExecResult(ExecResult.SUCCESS,bufferLog.toString());
    }

    public static ReadFileVo readFile(Machine machine, String path) throws IOException {
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
        ReadFileVo vo = new ReadFileVo();
        try {
            String readString = "";
            SFTPv3Client client = new SFTPv3Client(connection);
            client.setRequestParallelism(1);
            SFTPv3FileAttributes attributes = client.lstat(path);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            long time = attributes.mtime * 1000L;
            date.setTime(time);
            vo.setDate(sdf.format(date));
            SFTPv3FileHandle fh = client.openFileRO(path);
            int numread;
            int length = 0;
            int maxLength = 33990;
            Long len = attributes.size;
            int readSize = len.compareTo(Long.valueOf(maxLength)) >= 0 ? maxLength : len.intValue();
            byte[] bytes = new byte[readSize];
            while (readSize > 0 && (numread = client.read(fh, length, bytes, 0, readSize)) != -1) {
                length += numread;
                readSize = len.compareTo(Long.valueOf(length + maxLength)) >= 0 ? maxLength : (int) (len - length);
                readString = readString + new String(bytes, StandardCharsets.UTF_8);
                bytes = new byte[readSize];
            }
            vo.setContent(readString.replace("\n", "\r\n"));
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            vo.setContent("读取文件失败" + e.getMessage());
            return vo;
        } finally {
            connection.close();
        }
    }

    public static ReadFileVo readPageFile(Machine machine, String path, int pageNum) throws IOException {
        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
        ReadFileVo vo = new ReadFileVo();
        try {
            String readString = "";
            SFTPv3Client client = new SFTPv3Client(connection);
            client.setRequestParallelism(1);
            SFTPv3FileAttributes attributes = client.lstat(path);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            long time = attributes.mtime * 1000L;
            date.setTime(time);
            vo.setDate(sdf.format(date));
            SFTPv3FileHandle fh = client.openFileRO(path);
            int maxLength = 33990;
            Long length = Long.valueOf(maxLength * pageNum);
            Long len = attributes.size;
            Long pageTotal = len/Long.valueOf(maxLength);
            vo.setPageTotal(pageTotal.intValue() + 1);
            if(len.intValue() <= length) {
                throw new ServerException("没有更多的内容。");
            }
            len = len - length;
            int readSize = len.compareTo(Long.valueOf(maxLength)) >= 0 ? maxLength : len.intValue();
            byte[] bytes = new byte[readSize];
            if (readSize > 0 && client.read(fh, length, bytes, 0, readSize) != -1) {
                readString = readString + new String(bytes, StandardCharsets.UTF_8);
            }
            vo.setContent(readString.replace("\n", "\r\n"));
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            vo.setContent("读取文件失败" + e.getMessage());
            return vo;
        } finally {
            connection.close();
        }
    }

    public static void createPath(Machine machine, String path) throws IOException {

        Connection connection = getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
        Session session = connection.openSession();
        session.execCommand("[ -d " + path + " ] && echo 'find folder' || mkdir -p "
                + path, CharsetConsts.DEFAULT_ENCODING);
        session.close();
        connection.close();
    }

    private static void log(String logId,String logStr,EchoFunction echoFunction,StringBuffer stringBuffer,boolean append){
        log.info(logStr);
        echoFunction.print(logId,logStr);
        if(append) {
            stringBuffer.append(logStr+"\r\n");
        }
    }

    private static void dealInputStream(InputStream inputStream,String logId,EchoFunction echoFunction,StringBuffer stringBuffer,boolean append) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = bufferedReader.readLine()) != null){
            log(logId,line,echoFunction,stringBuffer,append);
        }
        try {
            bufferedReader.close();
            inputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String readStdLine(InputStream inputStream) {
        String line = "";
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String appender = "";
            while((appender = bufferedReader.readLine()) != null){
                line = line + appender;
            }
            inputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return line;
    }

    public static String readStdFirstLine(InputStream inputStream) {
        String line = "";
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            line = bufferedReader.readLine();
            inputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return line;
    }
}
