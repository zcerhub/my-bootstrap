package com.dap.paas.console.seq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 上下文路径：接口的上下文路径，平安的继承halo的包，原理一致
 * 由于前端调用接口未加上下文，此处配置控制，平安里配置了 /seqctr
 *
 * @author yzp
 * @date 2024/6/20 19:32
 */
@Configuration
public class InterceptorConfigurerAdapter implements WebMvcConfigurer {

    @Value("${spring.controller.path.prefix:}")
    private String pathPrefix;


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(pathPrefix, c -> c.isAnnotationPresent(RestController.class));
    }
}
