package com.dap.paas.console.common.util;


import com.dap.paas.console.common.constants.BootConstant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;


public class DapUtil {
    public static List<String> getFilePath(String basePath) {
        File baseFile = new File(basePath);
        List<String> filePaths = new ArrayList<>();
        getFilePath(baseFile, "", filePaths);
        return filePaths;
    }


    public static void getFilePath(File file, String path, List<String> filePathList) {
        if (file.isFile()) {
            path += (File.separator + file.getName());
            filePathList.add(path);
        } else {
            File[] files = file.listFiles();
            path += (File.separator + file.getName());
            if (null == files){
                return;
            }
            for (File f : files) {
                getFilePath(f, path, filePathList);
            }
        }
    }

    public static String getProjectHomePath() {
        String path = DapUtil.class.getResource(BootConstant.FILE_SEPARATOR).getPath();
        path = path.endsWith("/classes") || path.endsWith("/classes/")
                ? replaceLast(path, "classes", "").replace("//", "/") : path;
        path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR));
        if (path.lastIndexOf(BootConstant.FILE_SEPARATOR) > path.length()) {
            path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR) + 1);
        }
        return path;
    }
    public static String getProjectPath() {
        String path = DapUtil.class.getResource(BootConstant.FILE_SEPARATOR).getPath();

        String[] replaces = new String[] {"/config" , "/config/" , "/classes" , "/classes/"};
        path = StringUtils.endsWithAny(path, replaces)
                ? StringUtils.replaceEach(path, replaces, new String[] {"" , "" , "" , ""}) : path;
        path = path.replace("//", "/");
        path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR));
        if (path.lastIndexOf(BootConstant.FILE_SEPARATOR) > path.length()) {
            path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR) + 1);
        }
        return path;
    }

    public static String getRegisterPath() {
        String path = DapUtil.class.getResource(BootConstant.FILE_SEPARATOR).getPath();
        path = path.endsWith("/config") || path.endsWith("/config/")
                ? replaceLast(path, "config", "").replace("//", "/") : path;
        path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR));
        if (path.lastIndexOf(BootConstant.FILE_SEPARATOR) > path.length()) {
            path = path.substring(0, path.lastIndexOf(BootConstant.FILE_SEPARATOR) + 1);
        }
        return path;
    }
    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }

    public static String getLocalHost() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        Matcher isNum = BootConstant.NUMERIC_PATTERN.matcher(str);
        return isNum.matches();
    }

    public static String replaceLast(String text, String strToReplace, String replaceWithThis) {
        return text.replaceFirst("(?s)" + strToReplace + "(?!.*?" + strToReplace + ")", replaceWithThis);
    }
//public static void main(String[] args) {
//	String path = "D:\\apps\\works_j2ee/config\\dap4\\dap4-source/config";
//
//	if(!OsUtils.isLinux()) {
//		String[] replaces = new String[] {"/config" , "/config/" , "/classes" , "/classes/"};
//	    path = StringUtils.endsWithAny(path, replaces)
//	            ? StringUtils.replaceEach(path, replaces, new String[] {"" , "" , "" , ""}) : path;
//	            
//	            System.err.println(path);
//	}
//    
//}
}
