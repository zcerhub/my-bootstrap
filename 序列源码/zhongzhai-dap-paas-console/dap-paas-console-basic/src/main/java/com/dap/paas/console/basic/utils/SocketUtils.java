package com.dap.paas.console.basic.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: socket操作工具类
 * @author: Fesine
 * @createTime:2018/7/6
 * @update:修改内容
 * @author: Fesine
 * @updateTime:2018/7/6
 */
public class SocketUtils {

    private static final Logger logger = LoggerFactory.getLogger(SocketUtils.class);

    private SocketUtils(){}

    public static boolean testPort(String ip,Integer port){
        Socket client;
        try {
            client = new Socket(ip, port);
            client.close();
            return true;
        } catch (Exception e) {
            logger.warn("{}:{} 连接失败．",ip,port);
            return false;
        }
    }

    /**
     * 检查ip与端口是否可以访问，设置超时时间
     * @param ip
     * @param port
     * @return
     */
    public static boolean checkHostPort(String ip,Integer port) {
        Socket socket = new Socket();
        try {
            //设置超时3000毫秒
            socket.connect(new InetSocketAddress(ip, port), 3000);
            socket.close();
            return true;
        } catch (IOException e) {
            logger.warn("{}:{} 连接失败．",ip,port);
            return false;
        }
    }

    /**
     * 判断某服务能否连通
     *
     * @param host host
     * @param port port
     * @return boolean
     */
    public static boolean isRunning(String host, int port) {
        Socket sClient = null;
        try {
            SocketAddress saAdd = new InetSocketAddress(host.trim(), port);
            sClient = new Socket();
            sClient.connect(saAdd, 1000);
        } catch (UnknownHostException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (sClient != null) {
                    sClient.close();
                }
            } catch (Exception e) {
                logger.error("host={}, port={}", host, port, e);
            }
        }
        return true;
    }

}
