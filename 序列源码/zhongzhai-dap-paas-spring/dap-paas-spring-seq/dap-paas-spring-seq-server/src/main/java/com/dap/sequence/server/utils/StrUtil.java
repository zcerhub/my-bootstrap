package com.dap.sequence.server.utils;

/**
 * @description:  利用日期格式拼接日期替换符
 * @author: zhangce
 * @create: 7/7/2024 4:23 PM
 **/
public class StrUtil {


    public static String getDatePlaceholder(String dateFormat) {
        StringBuffer datePlaceholder = new StringBuffer("{");
        datePlaceholder.append("2");
        datePlaceholder.append(",");
        datePlaceholder.append(dateFormat);
        datePlaceholder.append("}");
        return datePlaceholder.toString();
    }

}
