package com.dap.paas.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.base", "com.dap","com.gientech"})
@ComponentScan({"com.dap"})
@MapperScan({"com.dap.route.dao"})
public class PlatformBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(PlatformBootstrap.class, args);
    }

}
