package com.base.api.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class OSUtil {

    public static long ip;
    public static long mac;
    static {
        ip = Long.parseLong((getIpAddress().split("\\."))[3]) ;
        mac = charToAscii(getMacAddress());
    }


    /**
     * 获取ip地址
     * @return
     */
    private static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            byte[] mac = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    mac = netInterface.getHardwareAddress();
                    if (mac != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        }
                        if (sb.length() > 0) {
                            return sb.toString();
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 获取IP 地址
     * @return
     */
    private static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     *  出入MAC地址，获取最后两位转换成数字
     * @param mac
     * @return
     */
    public static long charToAscii(String mac){
        mac = mac.substring(15,17);
        StringBuffer sb = new StringBuffer();
        char[] chars = mac.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if(aChar >='0' && aChar<='9') {
                sb.append(aChar);
            }
            else  if(aChar >='A' && aChar<='Z'){
                sb.append(((int) aChar)-62);
            }
        }
        return  Integer.valueOf(sb.toString());
    }
}
