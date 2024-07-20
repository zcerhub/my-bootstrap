package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.ValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @className SeqTemplateRule
 * @description 序列模板规则实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqTemplateRule extends BaseEntity {

    /**
     * 规则名称       db_column: rule_name
     */
    @NotBlank(message = "规则名称不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 32, message = "规则名称长度范围为1~32")
    private String ruleName;

    /**
     * 规则类型       db_column: rule_type
     */
    @LegalValue(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "规则类型非法", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String ruleType;

    /**
     * 规则描述       db_column: rule_desc
     */
    @Size(max = 64, message = "规则描述长度范围为1~64", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String ruleDesc;

    /**
     * 规则信息       db_column: rule_info
     */
    @NotBlank(message = "规则信息不能为空！", groups = {ValidGroup.Valid.RuleConfig.class})
    @Size(min = 1, max = 200, message = "规则信息长度范围为1~200")
    private String ruleInfo;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(String ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    @Override
    public String toString() {
        return "SeqTemplateRule{" +
                "ruleName='" + ruleName + '\'' +
                ", ruleType='" + ruleType + '\'' +
                ", ruleDesc='" + ruleDesc + '\'' +
                ", ruleInfo='" + ruleInfo + '\'' +
                '}';
    }
}


