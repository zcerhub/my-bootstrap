package com.dap.sequence.api.util;


import java.util.Date;

import com.dap.sequence.api.constant.SequenceConstant;


/**
 * @className SequenceUtils
 * @description 序列工具具
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceUtils {


    /**
     * 获取回拨模式的 序列日期
     *
     * @param callbackMode callbackMode
     * @return String
     */
    public static String getSeqDay(String callbackMode , Date date) {
        String result = "";
        switch (callbackMode) {
            case SequenceConstant.CALLBACK_MODE_GLOBAL:
                result = SequenceConstant.DAY_BASE;
                break;
            case SequenceConstant.CALLBACK_MODE_DAY:
                result = DateUtils.convertToString(date, DateUtils.DATE_FORMAT);
                break;
            case SequenceConstant.CALLBACK_MODE_MONTH:
                result = DateUtils.convertToString(date, "yyyyMM");
                break;
            case SequenceConstant.CALLBACK_MODE_YEAR:
                result = DateUtils.convertToString(date, "yyyy");
                break;
            default:
                result = SequenceConstant.DAY_BASE;
        }
        return result;
    }

}
