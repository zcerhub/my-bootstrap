package com.dap.sequence.client.utils;

import cn.hutool.core.util.StrUtil;
import com.dap.sequence.api.constant.SequenceConstant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: zhangce
 * @create: 6/4/2024 10:29 AM
 **/
public class DateUtils {

    public static DateTimeFormatter DAY_CALLBACK_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static LocalDate getLocalDateBy(String day,String callbackMode) {
        if (StrUtil.isEmpty(day) || StrUtil.isEmpty(callbackMode)) {
            return null;
        }

        LocalDate localDate=null;
        if (StrUtil.equals(SequenceConstant.CALLBACK_MODE_DAY, callbackMode)) {
            localDate = LocalDate.parse(day, DAY_CALLBACK_DATETIME_FORMATTER);
        }

        return localDate;
    }

}
