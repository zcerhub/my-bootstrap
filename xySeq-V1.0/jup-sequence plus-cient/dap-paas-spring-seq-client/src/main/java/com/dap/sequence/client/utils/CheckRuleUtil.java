package com.dap.sequence.client.utils;


import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.util.RandomUtil;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dap.sequence.api.exception.ExceptionEnum.CHECK_WEIGHT_VALUE_ERROR;

/**
 * @className CheckRuleUtil
 * @description 替换生成校验位
 * @author renle
 * @date 2023/10/24 15:23:43 
 * @version: V23.06
 */
public class CheckRuleUtil {

    private CheckRuleUtil() {
        throw new IllegalStateException("CheckRuleUtil class");
    }

    private static final Pattern PATTERN = Pattern.compile("(\\[([1-9][0-9]*),[0-1]])");

    private static final int INT_0 = 0;

    private static final int INT_1 = 1;

    private static final int MAX_LENGTH = 13;

    private static final String REGEX = "\\%s";

    private static final String MATCH_REGEX = "^[0-9]*$";


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
     * @param seqNo 原始序列
     * @param length 校验位长度
     * @param position 补位 规则 0-左|1-右
     * @return String
     */
    public static String getCheck(String seqNo, int length, int position) {
        seqNo = StringUtils.trim(seqNo);
        int parity = 0 ;
        int totalNumber = 0 ;
        if( MAX_LENGTH <= seqNo.length()){
            throw new SequenceException(CHECK_WEIGHT_VALUE_ERROR,seqNo,MAX_LENGTH);
        }
        // 账号中含有字符转换成ASCII处理
        if(rpxMatch(MATCH_REGEX, seqNo) == 1 && !seqNo.isEmpty()) {
            StringBuilder seqNoTmp = new StringBuilder();
            char[] seqNoArray = seqNo.toCharArray();
            for (char c : seqNoArray) {
                if(rpxMatch(MATCH_REGEX,String.valueOf(c)) == 1) {
                    seqNoTmp.append((int) c);
                } else {
                    seqNoTmp.append(c);
                }
            }
            seqNo = seqNoTmp.toString();
        }

        String sJyw = "3189673719737";
        for (int i = 0; i < seqNo.length(); i++) {
            int tmpNumber = Integer.parseInt(String.valueOf(seqNo.charAt(i))) * Integer.parseInt(sJyw.substring(i, i + 1));
            totalNumber += tmpNumber;
        }
        String sTotal = String.valueOf(totalNumber);
        String strTmp = "";
        if(sTotal.length() > length) {
            strTmp = sTotal.substring(sTotal.length() - length);
        } else {
            strTmp = sTotal;
        }
        int i10 = (int) Math.pow(10,length);

        if(0 != Integer.parseInt(strTmp)) {
            parity = i10 - Integer.parseInt(strTmp);
        }
        return 0 == position ? StringUtils.leftPad(String.valueOf(parity), length, "0") : StringUtils.rightPad(String.valueOf(parity), length, "0");
    }

    private static String replace(String str, CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(str).replaceFirst(Matcher.quoteReplacement(replacement.toString()));
    }

    /**
     * 功能说明：合法串匹配检查
     *
     * @param sRpx 合法正则串
     * @param sData 待核查串
     * @return 如果匹配合法返回0，否则返回1
     */
    public static int rpxMatch(String sRpx, String sData) {
        Pattern p = Pattern.compile(sRpx);
        Matcher m = p.matcher(sData);
        boolean b = m.matches();
        return b ? 0 : 1;
    }
}
