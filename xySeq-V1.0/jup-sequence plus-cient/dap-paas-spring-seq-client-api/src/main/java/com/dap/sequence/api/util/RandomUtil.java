package com.dap.sequence.api.util;

import java.security.SecureRandom;
import java.util.Random;


/**
 * @className RandomUtil
 * @description 随机数工具
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class RandomUtil {
    private static SecureRandom random = new SecureRandom();

    /**
     * 生成随机字符串
     *
     * @return String
     */
    public static String getRandomNumString() {
        // length表示生成字符串的长度
        int length = 11;
        String base = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成随机数
     *
     * @return int
     */
    public static int getRandomNum() {
        int max = 1999999999;
        int min = 10000000;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    /**
     * 生成随机long
     *
     * @param length length
     * @return long
     */
    public static long getRandomLong(int length) {
        // 补位最大值
        Double paddindMaxVal = Math.pow(10, length);
        Double s = random.nextLong() % paddindMaxVal;
        return Math.abs(s.longValue());
    }
}
