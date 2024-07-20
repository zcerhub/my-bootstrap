package com.dap.sequence.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renle
 * @className NumberCircleEnum
 * @description 序列增长到最大值策略
 * @date 2023/09/16 10:07:53
 * @version: V23.06
 */
public enum NumberCircleEnum {

    /**
     * 继续增长
     */
    CONTINUING_TO_GROW(0, "继续增长"),

    /**
     * 重头开始
     */
    STARTING_FROM_SCRATCH(1, "重头开始"),

    /**
     * 最大值
     */
    MAXIMUM_VALUE(2, "最大值"),

    /**
     * 抛出异常
     */
    THROW_AN_EXCEPTION(3, "抛出异常");

    private int sign;

    private String desc;

    NumberCircleEnum(int sign, String desc) {
        this.sign = sign;
        this.desc = desc;
    }

    public int getSign() {
        return sign;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 缓存序列到最大值处理枚举
     */
    private static final Map<Integer, NumberCircleEnum> NUM_CIRCLE_ENUM_MAP = new HashMap<>();

    static {
        NUM_CIRCLE_ENUM_MAP.put(CONTINUING_TO_GROW.sign, CONTINUING_TO_GROW);
        NUM_CIRCLE_ENUM_MAP.put(STARTING_FROM_SCRATCH.sign, STARTING_FROM_SCRATCH);
        NUM_CIRCLE_ENUM_MAP.put(MAXIMUM_VALUE.sign, MAXIMUM_VALUE);
    }

    /**
     * 根据sign,获取对应枚举
     *
     * @param sign
     * @return
     */
    public static NumberCircleEnum sign(Integer sign) {
        NumberCircleEnum circleEnum = NUM_CIRCLE_ENUM_MAP.get(sign);
        return circleEnum != null ? circleEnum : NumberCircleEnum.THROW_AN_EXCEPTION;
    }
}
