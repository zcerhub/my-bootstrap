package com.dap.sequence.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @description:
 * @author: zhangce
 * @create: 6/4/2024 9:48 AM
 **/
@Configuration
public class SeqCacheCleanerThreadPoolConfiguration {


    @Value("${seq.cacheCleaner.threadPool.corePoolSize:10}")
    private int corePoolSize;

    @Value("${seq.cacheCleaner.threadPool.maxPoolSize:10}")
    private int maxPoolSize;

    @Value("${seq.cacheCleaner.threadPool.queueCapacity:200}")
    private int queueCapacity;



    @Bean
    public Executor  cacheCleanerTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix("cacheCleanerTaskExecutor--");
        taskExecutor.initialize();
        return taskExecutor;
    }


}
