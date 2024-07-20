package com.dap.paas.console.basic.entity;

import lombok.Data;

@Data
public class ConfigEntity {

    private String configKey;

    private Object configValue;
}
