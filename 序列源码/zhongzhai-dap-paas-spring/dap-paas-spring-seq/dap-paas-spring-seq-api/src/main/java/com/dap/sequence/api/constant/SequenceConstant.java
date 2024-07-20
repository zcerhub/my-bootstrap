package com.dap.sequence.api.constant;

/**
 * @className SequenceConstant
 * @description 序列常量类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceConstant {

    /**
     * 设计状态为完成
     */
    public static final String DESIGN_STATE_ON = "2";

    /**
     * 规则类型  1 固定字符串
     */
    public static final String STRING_RULE = "1";

    /**
     * 规则类型  2 枚举
     */
    public static final String ENUM_RUlE = "2";

    /**
     * 规则类型  3 日期
     */
    public static final String DATE_RUlE = "3";

    /**
     * 规则类型  4 数字
     */
    public static final String NUMBER_RULE = "4";

    /**
     * 占位符 5
     * SpecialCharRule
     */
    public static final String SPECIAL_RULE = "5";

    /**
     * 自选序号 6
     * SpecialCharRule
     */
    public static final String OPTIONAL_RULE = "6";

    /**
     * 校验位 7
     * CheckRule
     */
    public static final String CHECK_RULE = "7";

    public static final String DAY_SWITCH_SPLIT = "###";

    public static final String DAY_BASE = "base";

    /**
     * 租户后缀标识
     */
    public static final String TENANT_POSTFIX = "@";

    public static final String CALLBACK_MODE_GLOBAL = "0";

    public static final String CALLBACK_MODE_DAY = "1";

    public static final String CALLBACK_MODE_MONTH = "2";

    public static final String CALLBACK_MODE_YEAR = "3";

    /**
     * 自选记录 已使用状态
     */
    public static final String OPTIONAL_STATUS_USE = "1";

    /**
     * 自选记录 k可使用
     */
    public static final String OPTIONAL_STATUS_UN_USE = "2";

    /**
     * 未过滤
     */
    public static final String OPTIONAL_STATUS_UN_FILTER = "0";

    /**
     * 已过滤
     */
    public static final String OPTIONAL_STATUS_FILTER = "1";

    /**
     * 回收
     */
    public static final String RECOVERY_SWITCH_ON = "1";

    /**
     * 未使用
     */
    public static final String RECOVERY_STATUS_UN_USE = "0";

    /**
     * 已使用
     */
    public static final String RECOVERY_STATUS_USE = "1";

    /**
     * 自选补位策略 自定义
     */
    public static final int OPTIONAL_PADDINDSTATEGY_DIY = 3;

    /**
     * 最大值报错
     */
    public static final int NUMBER_MAX_STRATEGY_EXCEPTION = 3;

    public static final int INT_8 = 8;


    public static final int SEQ_CACHE_CLEANING_CACHE_DAY_START =2;
}
