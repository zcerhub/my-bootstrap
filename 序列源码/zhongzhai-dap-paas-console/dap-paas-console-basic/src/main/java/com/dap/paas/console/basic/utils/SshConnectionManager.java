package com.dap.paas.console.basic.utils;

import com.dap.paas.console.basic.entity.Machine;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

public class SshConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(SshConnectionManager.class);
    private static Session session;
    private static ChannelShell channel;
    private static Session getSession(String hostname,int port,String username,String password){
        if(session == null || !session.isConnected()){
            session = connect(hostname,port,username,password);
        }
        return session;
    }
    private static Channel getChannel(String host, int port, String username, String password){
        if(channel == null || !channel.isConnected()){
            try{
                channel = (ChannelShell)getSession(host,port,username,password).openChannel("shell");
                channel.connect();
            }catch(Exception e){
                logger.error("服务器连接异常",e);
            }
        }
        return channel;
    }
    private static Session connect(String hostname, int port,String username, String password){
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(username, hostname, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionCharset", "UTF-8");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
        }catch(Exception e){
            logger.error("命令执行异常",e);
        }
        return session;
    }
    public static String executeCommands(Machine machine, String command){
        String returnStr = "";
        try{
            Channel channel= getChannel(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
            sendCommands(channel, command);
            returnStr = readChannelOutput(channel, command);
        }catch(Exception e){
            logger.error("命令执行异常",e);
        }finally {
            channel.disconnect();
            session.disconnect();
        }
        return returnStr;
    }
    private static void sendCommands(Channel channel, String command){
        try{
            PrintStream out = new PrintStream(channel.getOutputStream());
            out.println("chsh -s /bin/bash");//服务器安装了fish会出现问题，临时禁用fish
            out.println("#!/bin/bash");
            out.println(command);
            out.println("exit");
            out.flush();
        }catch(Exception e){
            logger.error("命令执行异常sendCommands ",e);
        }
    }
    private static String readChannelOutput(Channel channel,String command){
        String specificLine = "";
        try{
            InputStream in = channel.getInputStream();
            StringBuffer resultExec = new StringBuffer();
            String line = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                resultExec.append(line).append("\n");
            }
            String resultExecStr = resultExec.toString();
            resultExecStr = resultExecStr.substring(resultExecStr.lastIndexOf(command));
            int lineNumber = 2; // 要获取的行号
            String[] lines = resultExecStr.split("\\r?\\n");
            if (lineNumber >= 1 && lineNumber <= lines.length) {
                specificLine = lines[lineNumber - 1];
            } else {
                logger.error("Invalid line number");
            }
        }catch(Exception e){
            logger.error("获取执行结果异常 ",e);
        }
        return specificLine;
    }
    /*public static void main(String[] args){
        String command = "which java";
        Machine machineCheck = new Machine();
        machineCheck.setId("112329923291");
        machineCheck.setHostIp("10.114.10.222");
        machineCheck.setHostPort(22);
        machineCheck.setHostSshAccount("root");
        machineCheck.setHostSshPassword("Pactera2020$$++222");
        String result = executeCommands(machineCheck,command);
        System.out.println("result----"+result);

        Machine machineCheck2 = new Machine();
        machineCheck2.setId("2222998911");
        machineCheck2.setHostIp("10.64.13.63");
        machineCheck2.setHostPort(22);
        machineCheck2.setHostSshAccount("root");
        machineCheck2.setHostSshPassword("Gientech2022!");
        String result2 = executeCommands(machineCheck2,command);
        System.out.println("result----"+result2);
    }*/
}
