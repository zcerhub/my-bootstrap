package ctbiyi.com.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class TimerConfig {

    private static final String SEPARATOR = ":";

    @Value("${yy.zk.host}")
    private String zkHost;

    @Value("${yy.zk.port}")
    private String zkPort;

    @Value("${yy.zk.sessionTimeout}")
    private int zkSessionTimeout;

    @Value("${yy.zk.connectTimeout}")
    private int zkConnectTimeout;

    @Value("${yy.zk.maxRetries}")
    private int zkMaxRetries;

    @Value("${yy.zk.sleepMsBetweenRetries}")
    private int zkSleepMsBetweenRetries;

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.builder().
                connectString(getZKEnpoint(zkHost,zkPort))
                .sessionTimeoutMs(zkSessionTimeout)
                .connectionTimeoutMs(zkConnectTimeout)
                .retryPolicy(new ExponentialBackoffRetry(zkSleepMsBetweenRetries,zkMaxRetries))
                .build();
    }

    private String getZKEnpoint(String zkHost, String zkPort) {
        return zkHost+SEPARATOR+zkPort;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
