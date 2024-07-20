package com.dap.sequence.client.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className DatePlaceholderUtil
 * @description 日期替换符工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class DatePlaceholderUtil {

    private static Pattern datePattern = Pattern.compile("(\\{[2],(.+?)})");
    /**
     * @param seqValue 包含占位符表达式的序列号
     *                 占位符表达式:
     *                 {2,日期格式}
     * @param date   待格式化的日期
     * @return 处理后的序列号
     */
    public static String datePlaceholderHandle(String seqValue, Date date) {
        if (ObjectUtil.isNotNull(date)) {
            Matcher m = datePattern.matcher(seqValue);
            while (m.find()) {
                String placeholderContext = m.group(1);
                String dateFormatter = m.group(2);
                String formattedDate = DateUtil.format(date, dateFormatter);
                seqValue = replace(seqValue, placeholderContext, formattedDate);
            }
        }
        return seqValue;
    }


    private static String replace(String str, CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(str).replaceFirst(Matcher.quoteReplacement(replacement.toString()));
    }
}
