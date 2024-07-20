package com.dap.sequence.api.dto;


import com.dap.sequence.api.dto.base.BaseRuleInfo;


/**
 * @className DateRuleInfo
 * @description 时间规则类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class DateRuleInfo extends BaseRuleInfo {

    private String dateFormat;

    private String simpleDateFormat;

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

    public String getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(String simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
}
