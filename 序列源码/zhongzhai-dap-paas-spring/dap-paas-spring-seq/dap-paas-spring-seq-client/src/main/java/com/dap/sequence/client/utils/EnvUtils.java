package com.dap.sequence.client.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @className EnvUtils
 * @description 环境工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class EnvUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvUtils.class);

    /**
     * 获取当前环境IP地址
     *
     * @return String
     */
    public static String getAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                if (!netInterface.getDisplayName().contains("VMware") && !netInterface.getDisplayName().contains("Realtek")) {
                    continue;
                }
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("getAddress error.msg = {}", e.getMessage(), e);
        }
        return "";
    }
}
