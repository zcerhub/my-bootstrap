package com.dap.paas.console.basic.config;

import com.dap.paas.console.basic.service.DeployService;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局资源配置
 *
 */
@Component("deployServices")
public class DeployServicesConfig extends AbstractFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Class getObjectType() {
        return List.class;
    }

    @Override
    protected List createInstance() {
        // 扫描所有 provider 并从 Bean 容器取出放入 list
        Reflections reflections = new Reflections(DeployService.class.getPackage().getName());
        return reflections
                .getSubTypesOf(DeployService.class)
                .stream()
                .map(deployServiceClass -> applicationContext.getBean(deployServiceClass))
                .collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
