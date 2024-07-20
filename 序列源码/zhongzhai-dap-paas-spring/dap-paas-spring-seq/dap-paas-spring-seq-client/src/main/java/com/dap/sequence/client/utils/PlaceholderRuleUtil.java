package com.dap.sequence.client.utils;

import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className PlaceholderRuleUtil
 * @description 占位符工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class PlaceholderRuleUtil {

    /**
     * @param seqValue 包含占位符表达式的序列号
     *                 占位符表达式:
     *                 {0,} 表示按参数的实际长度替换对应占位表达式
     *                 {1,固定长度} 表示按固定长度截取参数并替换对应占位表达式
     * @param params   替换参数
     * @return 处理后的序列号
     */
    public static String placeholderHandle(String seqValue, String... params) {
        if (!ObjectUtils.isEmpty(params)) {
            Pattern p = Pattern.compile("((\\{[1],([1-9][0-9]*)})|(\\{[0],}))");
            Matcher m = p.matcher(seqValue);
            ArrayBlockingQueue<String> paramsQueue = new ArrayBlockingQueue<>(params.length, true, Arrays.asList(params));
            while (m.find()) {
                String placeholderContext = m.group(1);
                if ("{0,}".equals(placeholderContext)) {
                    // 实际长度
                    seqValue = replace(seqValue, placeholderContext, Objects.requireNonNull(paramsQueue.poll()));
                } else {
                    // 固定长度
                    String[] split = placeholderContext.split(",");
                    int placeholderLength = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
                    String paramValue = paramsQueue.poll();
                    String result = seqValue;
                    seqValue = Optional.ofNullable(paramValue)
                            .filter(value -> value.length() > placeholderLength)
                            .map(value -> {
                                String paramValueAfterSplit = (String) paramValue.subSequence(0, placeholderLength);
                                return result.replace(placeholderContext, paramValueAfterSplit);
                            })
                            .orElseThrow(() -> new SequenceException(ExceptionEnum.PLACE_DIGITS_LESS_THAN_FIXED));
                }
            }
        }

        // 添加校验位
        return CheckRuleUtil.checkRuleHandle(seqValue);
    }

    private static String replace(String str, CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(str).replaceFirst(Matcher.quoteReplacement(replacement.toString()));
    }
}
