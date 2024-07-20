package com.dap.sequence.client.test;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther zpj
 * @descriptions
 * @Date 2022-02-22 15:16
 */

@EnableKnife4j
@SpringBootApplication(exclude = {MongoDataAutoConfiguration.class, MongoAutoConfiguration.class},scanBasePackages = {"com.dap.sequence.client"})
public class FeignClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);

    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
