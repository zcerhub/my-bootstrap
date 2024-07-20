package com.dap.sequence.client.config;

import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.client.balancer.RestTemplateBalancer;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.properties.ConfigBeanValue;
import com.dap.sequence.client.service.SeqCacheCleaningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @className SequenceConfig
 * @description 序列配置
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@ComponentScan("com.dap.sequence.client")
@Configuration
public class SequenceConfig {
    private static final Logger logger = LoggerFactory.getLogger(SequenceConfig.class);

    @Value("${gientech.dap.sequence.sequenceQueueCapacity:100}")
    private int sequenceQueueCapacity;

    /**
     * 缓存号段数量 ，默认双号段 压测时可适当调大一点
     */
    @Value("${gientech.dap.sequence.bufferCount:2}")
    private int bufferCount;

    @Value("${gientech.dap.sequence.timeout:0}")
    private int timeout;
    @Value("${gientech.dap.sequence.useSequenceCache:true}")
    private boolean useSequenceCache;

    @Autowired
    ConfigBeanValue configBeanValue;

    @Autowired
    RestTemplateBalancer restTemplateBalancer;

    @Bean
    public SequenceQueueFactory getSequenceQueueFactory(SeqConsumer seqConsumer,
                                                        SeqCacheCleaningService seqCacheCleaningService) {
        SequenceQueueFactory sequenceQueueFactory = new SequenceQueueFactory(seqConsumer);
        sequenceQueueFactory.setTimeout(this.timeout);
        sequenceQueueFactory.setSequenceQueueCapacity(this.sequenceQueueCapacity);
        sequenceQueueFactory.setUseSequenceCache(this.useSequenceCache);
        sequenceQueueFactory.setBufferCount(this.bufferCount);
        sequenceQueueFactory.setSeqCacheCleaningService(seqCacheCleaningService);
        return sequenceQueueFactory;
    }
}
