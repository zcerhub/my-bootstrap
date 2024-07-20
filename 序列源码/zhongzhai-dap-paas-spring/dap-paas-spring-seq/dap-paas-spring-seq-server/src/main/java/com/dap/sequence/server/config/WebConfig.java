package com.dap.sequence.server.config;

import com.dap.sequence.server.interceptor.UcacAuthInterceptorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author XIAYUANA
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private  final  UcacAuthInterceptorAdapter ucacAuthInterceptorAdapter;

    public WebConfig(UcacAuthInterceptorAdapter ucacAuthInterceptorAdapter) {
        this.ucacAuthInterceptorAdapter = ucacAuthInterceptorAdapter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并设置拦截路径
        registry.addInterceptor(ucacAuthInterceptorAdapter);
    }
}
