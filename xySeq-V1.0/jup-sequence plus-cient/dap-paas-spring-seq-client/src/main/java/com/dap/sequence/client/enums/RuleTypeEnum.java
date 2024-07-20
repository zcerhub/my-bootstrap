package com.dap.sequence.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列规则类型枚举
 *
 * @author lyf
 * @date 2024/3/25
 */
public enum RuleTypeEnum {

    /**
     * 固定字符串
     */
    FIXED_STR_RULE("1","固定字符串"),

    /**
     * 枚举
     */
    ENUM_RULE("2","枚举类型"),

    /**
     * 日期
     */
    DATE_RULE("3","日期类型"),

    /**
     * 数字类型
     */
    NUM_RULE("4","数字类型"),

    /**
     * 特殊字符
     */
    SPECIAL_STR_RULE("5","特殊字符"),

    /**
     * 自选规则
     */
    SELF_OPTION_RULE("6","自选规则"),

    /**
     * 校验位规则
     */
    VALIDATE_RULE("7","校验位规则"),

    /**
     * 错误规则，抛出异常
     */
    WRONG_RULE("-1","错误规则");

    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 规则描述
     */
    private String ruleDesc;

    private static final Map<String, RuleTypeEnum> RULE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        RULE_TYPE_ENUM_MAP.put(FIXED_STR_RULE.ruleType, FIXED_STR_RULE);
        RULE_TYPE_ENUM_MAP.put(ENUM_RULE.ruleType, ENUM_RULE);
        RULE_TYPE_ENUM_MAP.put(DATE_RULE.ruleType, DATE_RULE);
        RULE_TYPE_ENUM_MAP.put(NUM_RULE.ruleType, NUM_RULE);
        RULE_TYPE_ENUM_MAP.put(SPECIAL_STR_RULE.ruleType, SPECIAL_STR_RULE);
        RULE_TYPE_ENUM_MAP.put(SELF_OPTION_RULE.ruleType, SELF_OPTION_RULE);
        RULE_TYPE_ENUM_MAP.put(VALIDATE_RULE.ruleType, VALIDATE_RULE);
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

    RuleTypeEnum(String ruleType, String ruleDesc) {
        this.ruleType = ruleType;
        this.ruleDesc = ruleDesc;
    }

    /**
     * 获取配置规则枚举信息
     *
     * @param ruleType 枚举值
     * @return 返回枚举
     */
    public static RuleTypeEnum getRuleTypeEnum(String ruleType){
        RuleTypeEnum ruleTypeEnum = RULE_TYPE_ENUM_MAP.get(ruleType);
        return ruleTypeEnum != null ?ruleTypeEnum : WRONG_RULE;
    }

}
