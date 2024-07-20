package com.dap.paas.console.seq.enums;

import java.util.Objects;

/**
 * @className NodeRunState
 * @description 节点运行状态
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public enum NodeRunState {

    /**
     * cluster info state: ok
     */
    RUN(1, "运行中"),

    /**
     * cluster info state not ok
     * can't get cluster info
     */
    BAD(2, "异常"),

    /**
     * node not good
     */
    STOP(0, "停止"),


    UNKNOWN(-1, "unknown");

    private int key;

    private String value;

    NodeRunState() {
    }

    NodeRunState(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

    public static NodeRunState value(String value) {
        for (NodeRunState nodeRole : values()) {
            if (Objects.equals(nodeRole.getValue(), value)) {
                return nodeRole;
            }
        }
        return UNKNOWN;
    }

    public static NodeRunState key(int key) {
        for (NodeRunState nodeRole : values()) {
            if (Objects.equals(nodeRole.getKey(), key)) {
                return nodeRole;
            }
        }
        return UNKNOWN;
    }

    public static String keyVal(int key) {
        for (NodeRunState nodeRole : values()) {
            if (Objects.equals(nodeRole.getKey(), key)) {
                return nodeRole.getValue();
            }
        }
        return null;
    }
}