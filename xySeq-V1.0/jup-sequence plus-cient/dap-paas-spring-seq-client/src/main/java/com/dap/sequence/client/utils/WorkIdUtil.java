package com.dap.sequence.client.utils;

import com.alibaba.fastjson.JSON;
import com.dap.sequence.api.dto.SeqSdkMonitorDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.client.feign.SeqFeignApi;
import com.dap.sequence.client.properties.ConfigBeanValue;
import com.dap.sequence.client.snow.SnowflakeGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 获取workId工具类
 *
 * @author lyf
 * @date 2024/1/20
 */
@Component
@Order(-1)
public class WorkIdUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(WorkIdUtil.class);

    private static ConfigBeanValue configBeanValue;

    private static SeqFeignApi seqFeignApi;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        configBeanValue = applicationContext.getBean(ConfigBeanValue.class);
        seqFeignApi = applicationContext.getBean(SeqFeignApi.class);

        logger.info("Starting init SnowflakeGenerator.Snowflake");
        final String nextIdStr = SnowflakeGenerator.getInstance().nextIdStr();
        logger.info("Ended init SnowflakeGenerator.Snowflake, first id is {}", nextIdStr);
    }

    /**
     * 从应用配置application文件{dap.sequence.workerId}获取
     *
     * @return
     */
    public static Integer wordIdFromConfig() {
        //获取配置的workId
        logger.info("Loading workId from application config:{dap.sequence.workerId}");

        Integer workerId = configBeanValue.getWorkerId();
        if (workerId != null) {
            return workerId;
        }
        logger.info("The application is not configured {dap.sequence.workerId}");
        return null;
    }

    /**
     * 从序列服务端获取workId
     *
     * @return
     */
    public static Integer wordIdFromSeqServer() {
        SeqSdkMonitorDto seqSdkMonitor = new SeqSdkMonitorDto();
        if (K8sPodUtil.isIsK8sPod()) {
            //容器部署
            seqSdkMonitor.setInstanceName(K8sPodUtil.getPodInstanceNameName());
        } else {
            //虚机
            seqSdkMonitor.setHostIp(OSUtils.getHostIp());
            seqSdkMonitor.setPort(configBeanValue.getServerPort());
            seqSdkMonitor.setInstanceName(OSUtils.getHostIp() + ":" + configBeanValue.getServerPort());
        }
        seqSdkMonitor.setServiceName(configBeanValue.getSeqClusterName());
        seqSdkMonitor.setSdkName(StringUtils.isNotBlank(configBeanValue.getClientName()) ? configBeanValue.getClientName() :
                configBeanValue.getApplicationName());
        seqSdkMonitor.setContextPath(configBeanValue.getContextPath());
        String content = JSON.toJSONString(seqSdkMonitor);
        logger.info("Starting request sequence server to obtain workId");
        //调用序列服务端workId生成接口
        ResultResponse<Integer> response = seqFeignApi.snowflakewordid(seqSdkMonitor);
        Integer workerId = null;
        //获取workId
        if (response != null && ExceptionEnum.SUCCESS.getResultCode().equals(response.getCode())
                && response.getData() != null) {
            workerId = response.getData();
            logger.info("Successfully requested workId from sequence server, and obtained workId is {}.", workerId);
        }else {
            logger.error("Failed to request workId from sequence server, the error message:{}", (response != null) ? response.getMsg():"other reason");
        }
        return workerId;
    }


}
