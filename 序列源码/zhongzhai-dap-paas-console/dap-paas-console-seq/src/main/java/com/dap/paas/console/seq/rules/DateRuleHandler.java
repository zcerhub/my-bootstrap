package com.dap.paas.console.seq.rules;


import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.DateRuleInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className DateRuleHandler
 * @description 序列时间规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class DateRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        DateRuleInfo dateRuleInfo = (DateRuleInfo) ruleInfo;
        SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
        return sdf.format(new Date());
    }

    /**
     * 规则类型  3 日期
     *
     * @return String
     */
    @Override
    public String getRuleName() {
        return "3";
    }
}
