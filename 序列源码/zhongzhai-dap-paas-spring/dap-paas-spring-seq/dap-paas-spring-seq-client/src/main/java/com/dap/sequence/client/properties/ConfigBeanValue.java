package com.dap.sequence.client.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @className ConfigBeanValue
 * @description 配置类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Configuration
public class ConfigBeanValue {

    /**
     * 动态注册与发现定时时间 如果关闭 "off"
     */
    //@Value("${gientech.dap.sequence.refreshTime:0 */1 * * * ?}")
    @Value("${gientech.dap.sequence.refreshTime:off}")
    private String refreshTime;

    /**
     * 客户端定时注册信息到服务端 如果关闭 "off"
     */
    //    @Value("${gientech.dap.sequence.registerTime:off}")
    @Value("${gientech.dap.sequence.registerTime:0 */5 * * * ?}")
    private String registerTime;

    /**
     * 定时刷新设计器 如果关闭 "off"
     */
    @Value("${gientech.dap.sequence.flushDesign:0 */2 * * * ?}")
    private String flushDesign;

    @Value("${gientech.dap.sequence.urls:}")
    private String urls;

    @Value("${gientech.dap.sequence.seqClusterName:}")
    private String seqClusterName;

    @Value("${gientech.dap.sequence.clientName:seq-client}")
    private String clientName;

    @Value("${gientech.dap.sequence.tenantId:391312369558487040}")
    private String tenantId;

    @Value("${server.port:}")
    private String serverPort;

    @Value("${server.context-path:}")
    private String contextPath;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${gientech.dap.sequence.connectTimeout:5000}")
    private int connectTimeout;

    @Value("${gientech.dap.sequence.readTimeout:30000}")
    private int readTimeout;

    @Value("${gientech.dap.sequence.workerId:}")
    private Integer workerId;

    /**
     * 默认清理超过7天的日切缓存
     */
    @Value("${gientech.dap.sequence.clearDay:-7}")
    private Integer clearDay;

    /**
     * 默认每天凌晨执行 0 0 0 * * ?
     */
    @Value("${gientech.dap.sequence.clearCorn:0 0 0 * * ?}")
    private String clearCorn;

    /**
     * 雪花算法心跳维护时间，默认5分钟=300秒
     */
    @Value("${gientech.dap.sequence.snowflakeInterval:300}")
    private Integer snowflakeInterval;


    public Integer getSnowflakeInterval() {
        return snowflakeInterval;
    }

    public void setSnowflakeInterval(Integer snowflakeInterval) {
        this.snowflakeInterval = snowflakeInterval;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getSeqClusterName() {
        return seqClusterName;
    }

    public void setSeqClusterName(String seqClusterName) {
        this.seqClusterName = seqClusterName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getFlushDesign() {
        return flushDesign;
    }

    public void setFlushDesign(String flushDesign) {
        this.flushDesign = flushDesign;
    }

    public Integer getClearDay() {
        return clearDay;
    }

    public void setClearDay(Integer clearDay) {
        this.clearDay = clearDay;
    }

    public String getClearCorn() {
        return clearCorn;
    }

    public void setClearCorn(String clearCorn) {
        this.clearCorn = clearCorn;
    }
}
