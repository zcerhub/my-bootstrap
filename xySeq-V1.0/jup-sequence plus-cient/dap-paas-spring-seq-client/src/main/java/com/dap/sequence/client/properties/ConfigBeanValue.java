package com.dap.sequence.client.properties;

import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.client.core.SequenceQueueFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @className ConfigBeanValue
 * @description 配置类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@ComponentScan({"com.dap.sequence"})
@Configuration
@EnableFeignClients(basePackages = "com.dap.sequence.client.feign")
public class ConfigBeanValue {

    /**
     * 动态注册与发现定时时间 如果关闭 "off"
     */
    @Value("${dap.sequence.refreshTime:off}")
    private String refreshTime;

    /**
     * 客户端定时注册信息到服务端 如果关闭 "off"
     */
    @Value("${dap.sequence.registerTime:0 */5 * * * ?}")
    private String registerTime;

    /**
     * 定时刷新设计器 如果关闭 "off"
     */
    @Value("${dap.sequence.flushDesign:0 */2 * * * ?}")
    private String flushDesign;

    @Value("${dap.sequence.urls:}")
    private String urls;

    @Value("${dap.sequence.seqClusterName:}")
    private String seqClusterName;

    @Value("${dap.sequence.clientName:seq-client}")
    private String clientName;

    @Value("${dap.sequence.tenantId:391312369558487040}")
    private String tenantId;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.context-path:}")
    private String contextPath;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${dap.sequence.connectTimeout:5000}")
    private int connectTimeout;

    @Value("${dap.sequence.readTimeout:30000}")
    private int readTimeout;

    @Value("${dap.sequence.workerId:}")
    private Integer workerId;

    @Value("${dap.sequence.sequenceQueueCapacity:100}")
    private int sequenceQueueCapacity;

    /**
     * 缓存号段数量， 默认双号段 压测时可适当调大一点
     */
    @Value("${dap.sequence.bufferCount:2}")
    private int bufferCount;

    @Value("${dap.sequence.timeout:0}")
    private int timeout;

    /**
     * 雪花算法心跳维护时间，默认5分钟=300秒
     */
    @Value("${dap.sequence.snowflakeInterval:300}")
    private Integer snowflakeInterval;

    public Integer getSnowflakeInterval() {
        return snowflakeInterval;
    }

    public void setSnowflakeInterval(Integer snowflakeInterval) {
        this.snowflakeInterval = snowflakeInterval;
    }

    @Bean
    public SequenceQueueFactory getSequenceQueueFactory(SeqConsumer seqConsumer) {
        SequenceQueueFactory sequenceQueueFactory = new SequenceQueueFactory(seqConsumer);
        sequenceQueueFactory.setTimeout(this.timeout);
        sequenceQueueFactory.setSequenceQueueCapacity(this.sequenceQueueCapacity);
        sequenceQueueFactory.setBufferCount(this.bufferCount);
        return sequenceQueueFactory;
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

    public int getSequenceQueueCapacity() {
        return sequenceQueueCapacity;
    }

    public void setSequenceQueueCapacity(int sequenceQueueCapacity) {
        this.sequenceQueueCapacity = sequenceQueueCapacity;
    }

    public int getBufferCount() {
        return bufferCount;
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
