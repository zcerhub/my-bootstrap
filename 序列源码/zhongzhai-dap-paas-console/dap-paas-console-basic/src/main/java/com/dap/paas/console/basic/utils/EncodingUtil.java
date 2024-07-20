package com.dap.paas.console.basic.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodingUtil {
    /**
     * 转换为文件名
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String convertToFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        // 针对IE或者以IE为内核的浏览器：
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return fileName;
    }
}
