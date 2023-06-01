package ctbiyi.com.timer;


import ctbiyi.com.service.LicenseVerifiedService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class LicenseVerifiedTimer {

    @Value("${yy.zk.lockPath}")
    private String lockPath;

    @Value("${yy.zk.acquireLockTimeout}")
    private int acquireLockTimeout;

    @Value("${yy.node.name}")
    private String nodeName;

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private LicenseVerifiedService licenseVerifiedService;

    @Value("${yy.license-server.enable-time}")
    private boolean enableTimer;

    //TODO
    @Scheduled(cron ="${yy.license-server.license-verified-cron}")
    public void licenseVerified() {
        if (enableTimer) {
            InterProcessMutex zkLock = new InterProcessMutex(zkClient, lockPath);
            try {
                if (zkLock.acquire(acquireLockTimeout, TimeUnit.SECONDS)) {
                    log.info("{} 获取分布式锁成功", nodeName);

                    licenseVerifiedService.licenseVerified();

                    zkLock.release();
                    log.info("{} 释放分布式锁", nodeName);
                } else {
                    log.info("{} 获取分布式锁失败", nodeName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
