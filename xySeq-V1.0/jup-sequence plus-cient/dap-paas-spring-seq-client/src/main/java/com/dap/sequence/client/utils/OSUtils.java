package com.dap.sequence.client.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @className OSUtils
 * @description OS工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class OSUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSUtils.class);

    public static String hostIp = "";

    /**
     * 中文字符
     */
    private static final Pattern PTN_CN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 获取WorkId
     *
     * @return int
     */
    public static int getWorkId(){
        try {
            return getHostId(Inet4Address.getLocalHost().getHostAddress(),63);
        } catch (UnknownHostException e) {
            LOGGER.error("getWorkId error.msg = {}", e.getMessage(), e);
           return new Random().nextInt(32);
        }
    }


    /**
     * 获取Dataid
     *
     * @return int
     */
    public static int getDataId(){
        try {
            return getHostId(Inet4Address.getLocalHost().getHostName(),31);
        } catch (UnknownHostException e) {
            LOGGER.error("getDataId error.msg = {}", e.getMessage(), e);
            return new Random().nextInt(32);
        }
    }


    /**
     *  项目启动只获取一次IP
     */
    static {
        hostIp = getHostIp();
    }

    /**
     * 获取当前环境IP地址
     *
     * @return String
     */
    public static String getHostIp()  {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                // 排除奇安新网关获取实际ip 排除虚拟网卡
                if (netInterface.getDisplayName().contains("Gateway")  || netInterface.getDisplayName().contains("VMware")) {
                    continue;
                }
                if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()) {
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
            LOGGER.error("获取IP信息异常:{}", e.getMessage());
        }
        return "";

    }

    /**
     * 获取主机ID
     *
     * @param str str
     * @param max max
     * @return int
     */
    public static int getHostId(String str, int max) {
        // 中文字段过滤
        str = PTN_CN.matcher(str).replaceAll("-").trim();
        byte[] bytes = str.getBytes();
        int sums = 0;
        for (int b:bytes) {
            sums +=b;
        }
        return sums %(max +1);
    }
}
