package com.dap.paas.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.base", "com.dap"})
@EnableDiscoveryClient
@EnableFeignClients
public class PlatformBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(PlatformBootstrap.class, args);
    }

}
