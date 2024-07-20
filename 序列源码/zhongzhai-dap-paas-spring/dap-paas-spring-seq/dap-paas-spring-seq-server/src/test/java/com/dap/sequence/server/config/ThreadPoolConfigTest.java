package com.dap.sequence.server.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @className ThreadPoolConfigTest
 * @description TODO
 * @author renle
 * @date 2024/02/06 09:11:07 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class ThreadPoolConfigTest {

    @InjectMocks
    private ThreadPoolConfig threadPoolConfig;

    @Test
    public void seqTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolConfig.seqTaskExecutor();
        Assert.assertNotNull(threadPoolTaskExecutor);
    }

    @Test
    public void seqScheduledExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolConfig.seqScheduledExecutor();
        Assert.assertNotNull(threadPoolTaskExecutor);
    }
}