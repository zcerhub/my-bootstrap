package com.dap.sequence.api.dto;


import com.dap.sequence.api.dto.base.BaseRuleInfo;


/**
 * @className StringRuleInfo
 * @description 字符串规则DTO
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class StringRuleInfo extends BaseRuleInfo {
    private  String initData;

    public StringRuleInfo(){}
    public StringRuleInfo(String initData ){

            this.initData = initData;
    }

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }
}
