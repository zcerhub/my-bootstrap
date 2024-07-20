package com.dap.sequence.server.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @className ThreadPoolConfig
 * @description 线程池配置
 * @author renle
 * @date 2023/11/30 09:54:25 
 * @version: V23.06
 */


@Configuration
public class ThreadPoolConfig {

    /**
     * 获取服务器的cpu个数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数量
     */
    private static final int COUR_SIZE = CPU_COUNT * 2;

    /**
     * 线程最大数量
     */
    private static final int MAX_COUR_SIZE = COUR_SIZE * 4;

    @Value("${gientech.dap.sequence.product.threadpool.corePoolSize:}")
    private String corePoolSize;

    @Value("${gientech.dap.sequence.product.threadpool.maxPoolSize:}")
    private String maxPoolSize;

    @Value("${gientech.dap.sequence.product.threadpool.queueCapacity:}")
    private String queueCapacity;

    @Value("${gientech.dap.sequence.product.threadpool.keepAliveSeconds:}")
    private String keepAliveSeconds;


    /**
     * 序列异步生产线程池
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean("seqTaskExecutor")
    public ThreadPoolTaskExecutor seqTaskExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数量
        int core = Optional.ofNullable(corePoolSize).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(COUR_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(core);

        // 最大线程数
        int max = Optional.ofNullable(maxPoolSize).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(MAX_COUR_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(max);

        // 线程缓冲任务队列
        int queue = Optional.ofNullable(queueCapacity).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElseGet(() -> MAX_COUR_SIZE * 2);
        threadPoolTaskExecutor.setQueueCapacity(queue);

        // 线程空闲时间
        int keep = Optional.ofNullable(keepAliveSeconds).filter(StringUtils::isNotBlank).map(Integer::parseInt).orElse(60);
        threadPoolTaskExecutor.setKeepAliveSeconds(keep);

        // 线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("seq-product-");

        // 线程拒绝任务处理策略：没精力处理时，直接拒绝任务；若执行程序已被关闭，则直接丢弃
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    /**
     * 序列异步生产线程池
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean("seqScheduledExecutor")
    public ThreadPoolTaskExecutor seqScheduledExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数量
        threadPoolTaskExecutor.setCorePoolSize(CPU_COUNT);

        // 最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(CPU_COUNT * 2);

        // 线程缓冲任务队列
        threadPoolTaskExecutor.setQueueCapacity(CPU_COUNT * 4);

        // 线程空闲时间
        threadPoolTaskExecutor.setKeepAliveSeconds(60);

        // 线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("seq-schedule-");

        // 线程拒绝任务处理策略：没精力处理时，直接拒绝任务；若执行程序已被关闭，则直接丢弃
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }
}
