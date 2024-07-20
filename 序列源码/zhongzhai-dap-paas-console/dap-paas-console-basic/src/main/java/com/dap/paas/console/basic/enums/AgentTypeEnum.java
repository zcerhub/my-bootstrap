package com.dap.paas.console.basic.enums;

/**
 *
 */
public enum AgentTypeEnum {

    /**
     * mq_rocketmq
     */
    MQ_ROCKETMQ("mq_rocketmq","1"),

    /**
     * mq_kafka
     */
    MQ_KAFKA( "mq_kafka","2"),

    /**
     * redis
     */
    REDIS( "redis","3");

    private String key;
    private String value;
    AgentTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
		return key;
	}
}
