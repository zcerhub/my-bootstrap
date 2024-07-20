package com.dap.sequence.client.utils;


import com.dap.sequence.api.util.RandomUtil;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className CheckRuleUtil
 * @description 替换生成校验位
 * @author renle
 * @date 2023/10/24 15:23:43 
 * @version: V23.06
 */
public class CheckRuleUtil {

    private static final Pattern PATTERN = Pattern.compile("(\\[([1-9][0-9]*),[0-1]])");

    private static final int INT_0 = 0;

    private static final int INT_1 = 1;

    private static final String REGEX = "\\%s";

    /**
     * @param seqValue 包含校验位表达式的序列号
     * @return 处理后的序列号
     */
    public static String checkRuleHandle(String seqValue) {
        Matcher m = PATTERN.matcher(seqValue);
        while (m.find()) {
            String checkInfo = m.group(INT_1);
            String[] info = checkInfo.substring(INT_1, checkInfo.length() - INT_1).split(",");

            // 获取当前校验位需要使用的序列段
            String checkSeq = seqValue.split(String.format(REGEX, checkInfo))[INT_0];

            // 生成校验位
            String checkValue = getCheck(checkSeq, Integer.parseInt(info[INT_0]), Integer.parseInt(info[INT_1]));

            // 替换校验位配置
            seqValue = replace(seqValue, checkInfo, checkValue);
        }
        return seqValue;
    }

    /**
     * 获取校验位
     *
     * @param seqValue 原始序列
     * @param length 校验位长度
     * @param position 补位 规则 0-左|1-右
     * @return String
     */
    public static String getCheck(String seqValue, int length, int position) {
        String result = String.valueOf(RandomUtil.getRandomLong(2));
        // 使用0补位
        result = position == INT_0 ? StringUtils.leftPad(result, length, "0") : StringUtils.rightPad(result, length, "0");
        return result.substring(INT_0, length);
    }

    private static String replace(String str, CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(str).replaceFirst(Matcher.quoteReplacement(replacement.toString()));
    }
}
