package com.dap.paas.console.basic.pojo;

/**
 * key
 * value
 * version: 5
 * mode: 0=普通节点; 1=cluster
 */
public class RedisConfig {

    /**
     *
     */
    private boolean enable;

    private String configKey;

    private String configValue;

    private Integer mode;

    private String desc;

    public RedisConfig() {
    }

    public RedisConfig(String configKey, String configValue, Integer mode) {
        this(true, configKey, configValue, mode, null);
    }

    public RedisConfig(boolean enable, String configKey, String configValue, int mode) {
        this(enable, configKey, configValue, mode, null);
    }

    public RedisConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;

    }

    public RedisConfig(boolean enable, String configKey, String configValue, int mode, String desc) {
        this.enable = enable;
        this.configKey = configKey;
        this.configValue = configValue;
        this.mode = mode;
        this.desc = desc;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RedisConfig{" +
                "enable=" + enable +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", mode=" + mode +
                ", desc='" + desc + '\'' +
                '}';
    }
}