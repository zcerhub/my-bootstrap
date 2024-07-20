package com.dap.paas.console.basic.service;

public interface EndpointService<T> {
    T request(String host, int port, String contextPath);
    String endpoint();
}
