package com.dap.sequence.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

/**
 * Check port
 * Ping
 *
 * @author zpj
 * @date 7/19/2023
 */
public class NetworkUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);

    private static final int TIMEOUT = 2000;

    private NetworkUtil() {
    }

    /**
     * 检测IP端口是否可用
     *
     * @param ip ip
     * @param port port
     * @return boolean
     */
    public static boolean checkFreePort(String ip, int port) {
        try {
            return !connect(ip, port);
        } catch (Exception e) {
            LOGGER.info("{}:{} is free.", ip, port);
            return true;
        }
    }

    /**
     * 检测IP端口连通性
     *
     * @param ip ip
     * @param port port
     * @return boolean
     */
    public static boolean telnet(String ip, int port) {
        try {
            return connect(ip, port);
        } catch (Exception e) {
            LOGGER.info("{}:{} can't access.", ip, port);
            return false;
        }
    }

    private static boolean connect(String ip, int port) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), TIMEOUT);
            return socket.isConnected();
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {
                LOGGER.error("socket close.msg = {}", ignore.getMessage(), ignore);
            }
        }
    }
}
