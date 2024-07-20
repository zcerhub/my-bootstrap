package com.dap.paas.console.seq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @className DatasourcePro
 * @description 数据源配置
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class DatasourcePro {

    private String url;

    private String username;

    private String password;

    /**
     * 配置文件中是driver-class-name, 转驼峰命名便可以绑定成
     */
    private String driverClassName;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

}