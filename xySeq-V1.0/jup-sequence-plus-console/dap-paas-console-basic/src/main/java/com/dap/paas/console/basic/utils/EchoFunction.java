package com.dap.paas.console.basic.utils;

public interface EchoFunction {
    void print(String logId,String line);
    void close(String logId);
}
