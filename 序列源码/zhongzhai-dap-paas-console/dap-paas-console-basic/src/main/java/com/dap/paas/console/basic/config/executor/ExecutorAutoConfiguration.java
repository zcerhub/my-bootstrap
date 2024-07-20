package com.dap.paas.console.basic.config.executor;

import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@EnableConfigurationProperties(ExecutorProperties.class)
public class ExecutorAutoConfiguration {
    private final ExecutorProperties executorProperties;

    public ExecutorAutoConfiguration(ExecutorProperties executorProperties) {
        this.executorProperties = executorProperties;
    }

    @Bean
    public ThreadPoolTaskExecutor coreThreadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(executorProperties.getCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(executorProperties.isAllowCoreThreadTimeOut());
        threadPoolTaskExecutor.setKeepAliveSeconds(executorProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setQueueCapacity(executorProperties.getQueueCapacity());
        return threadPoolTaskExecutor;
    }


    @Bean
    @ConditionalOnClass(SimpleAsyncUncaughtExceptionHandler.class)
    public DefaultAsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
        return new DefaultAsyncUncaughtExceptionHandler();
    }

    // TODO
    // 多线程池集合，可用来做不同业务间的线程池隔离，异步request，rxjava等，以及监控
    // 线程池命名以及根据命名获取注入
}
