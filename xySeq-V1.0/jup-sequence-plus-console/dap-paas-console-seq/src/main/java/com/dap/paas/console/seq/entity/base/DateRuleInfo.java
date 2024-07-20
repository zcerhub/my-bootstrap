package com.dap.paas.console.seq.entity.base;

/**
 * @className DateRuleInfo
 * @description 时间规则信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class DateRuleInfo extends BaseRuleInfo {

    private String dateFormat;

    public DateRuleInfo() {
    }

    public DateRuleInfo(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String toString() {
        return "{" + "\"dateFormat\":\"" +
                dateFormat + '\"' +
                '}';
    }
}
