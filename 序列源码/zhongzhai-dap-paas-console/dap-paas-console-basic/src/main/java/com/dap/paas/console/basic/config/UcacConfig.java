package com.dap.paas.console.basic.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UcacConfig {
    /**
     * 动态注册与发现定时时间 如果关闭 "off"
     */
    @Value("${gientech.ucac-client.address}")
    private String address;
    
    @Value("${dmc.dmcUrl}")
    private String dmcUrl;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDmcUrl() {
        return dmcUrl;
    }
    
    public void setDmcUrl(String dmcUrl) {
        this.dmcUrl = dmcUrl;
    }
}
