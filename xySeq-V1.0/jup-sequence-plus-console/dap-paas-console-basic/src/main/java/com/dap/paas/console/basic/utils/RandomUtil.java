package com.dap.paas.console.basic.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 随机数工具 * * @author QuJing * @date 2019/9/10 14:17
 */
public class RandomUtil {

    /**
     * 生成随机字符串     *     * @return
     */
    public static String getRandomNumString() {
        // length表示生成字符串的长度
        int length = 11;
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成随机整数     *     * @return
     * @return
     */
    public static int getRandomBrokerIdForSlave() {
        int max = 1999999999;
        int min = 10000000;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        System.out.println(s);
        return s;
    }

    public static String generateSecureAccessKey(int length){
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
