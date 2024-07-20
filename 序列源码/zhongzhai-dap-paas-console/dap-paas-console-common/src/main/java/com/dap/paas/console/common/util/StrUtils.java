/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dap.paas.console.common.util;

import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class StrUtils {

    private final static long KB_IN_BYTES = 1024;

    private final static long MB_IN_BYTES = 1024 * KB_IN_BYTES;

    private final static long GB_IN_BYTES = 1024 * MB_IN_BYTES;

    private final static long TB_IN_BYTES = 1024 * GB_IN_BYTES;

    private final static DecimalFormat df = new DecimalFormat("0.00");

    private static String SYSTEM_ENCODING = System.getProperty("file.encoding");

    static {
        if (SYSTEM_ENCODING == null) {
            SYSTEM_ENCODING = "UTF-8";
        }
    }

    private StrUtils() {
    }

    /** Formatter byte to kb,mb or gb etc. */
    public static String stringify(long byteNumber) {
        if (byteNumber / TB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) TB_IN_BYTES) + "TB";
        } else if (byteNumber / GB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) GB_IN_BYTES) + "GB";
        } else if (byteNumber / MB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) MB_IN_BYTES) + "MB";
        } else if (byteNumber / KB_IN_BYTES > 0) {
            return df.format((double) byteNumber / (double) KB_IN_BYTES) + "KB";
        } else {
            return byteNumber + "B";
        }
    }

    /** Formatter byte to kb,mb or gb etc. */
    public static JSONObject stringifyByObject(long byteNumber) {
        JSONObject object = new JSONObject();
        if (byteNumber / TB_IN_BYTES > 0) {
            object.put("size", df.format((double) byteNumber / (double) TB_IN_BYTES));
            object.put("type", "TB");
            return object;
        } else if (byteNumber / GB_IN_BYTES > 0) {
            object.put("size", df.format((double) byteNumber / (double) GB_IN_BYTES));
            object.put("type", "GB");
            return object;
        } else if (byteNumber / MB_IN_BYTES > 0) {
            object.put("size", df.format((double) byteNumber / (double) MB_IN_BYTES));
            object.put("type", "MB");
            return object;
        } else if (byteNumber / KB_IN_BYTES > 0) {
            object.put("size", df.format((double) byteNumber / (double) KB_IN_BYTES));
            object.put("type", "KB");
            return object;
        } else {
            object.put("size", String.valueOf(byteNumber));
            object.put("type", "B");
            return object;
        }
    }

    /**
     * Formmatter number.
     */
    public static String numberic(double number) {
        DecimalFormat formatter = new DecimalFormat("###.##");
        return formatter.format(number);
    }

    /**
     * Formmatter number.
     */
    public static double numberic(String number) {
        DecimalFormat formatter = new DecimalFormat("###.##");
        return Double.parseDouble(formatter.format(Double.valueOf(number)));
    }

    /**
     * Formmatter number.
     */
    public static double numberic(Double number, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        return Double.parseDouble(formatter.format(number));
    }

    /**
     * Formmatter number.
     */
    public static double numberic(String number, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        return Double.parseDouble(formatter.format(Double.valueOf(number)));
    }

    /**
     * Convert string number to double.
     */
    public static long integer(double number) {
        return Math.round(number);
    }

    /** Assembly number to string. */
    public static String assembly(String number) {
        return stringify(integer(numberic(number)));
    }

    /** whether it is empty. */
    public static boolean isNull(String value) {
        return value == null || value.length() == 0 || "".equals(value);
    }

    /** Convert null to string. */
    public static String convertNull(String value) {
        if (isNull(value)) {
            return "";
        }
        return value;
    }

    /**
     * 字符串根据分隔符后转换成list
     *
     * @param str  字符串
     * @param split 分割符
     * @return  生成分割后的集合
     */
    public static List<String> str2List(String str, String split){

        return Arrays.asList(str.split(split));
    }


    /**
     *  对象集合转换成数组
     * @param list 字符串集合
     * @return
     */
    public static String[] list2Array(List<String> list){
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }


}
