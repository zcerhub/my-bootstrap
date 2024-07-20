package com.dap.paas.console.basic.enums;

public enum ConsoleClient {
    BASIC("dap-console-basic"),
    CACHE("dap-console-cache"),
    CONFIG_PORTAL("dap-paas-console-config-portal"),
    CONFIG_SERVER("dap-paas-console-config-server"),
    MQ("dap-console-mq");

    private final String name;
    ConsoleClient(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
