package com.dap.paas.console.basic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * spring上下文
 *
 * @author Arlo
 * @date 2021/10/12
 **/
@Component
public class SpringContext implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext context;

    private SpringContext() {
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取非空bean，无法获取返回null
     *
     * @param name beanName
     * @param <T>  T
     * @return T
     */
    public static <T> T getBeanNullable(String name) {
        try {
            return getBeanNonnull(name);
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取非空bean，无法获取抛NoSuchBeanDefinitionException
     *
     * @param name beanName
     * @param <T>  T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanNonnull(String name) {
        return (T) context.getBean(name);
    }

    /**
     * 获取非空bean，无法获取返回null
     *
     * @param clazz bean class
     * @param <T>   T
     * @return T
     */
    public static <T> T getBeanNullable(Class<T> clazz) {
        try {
            return context.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取非空bean，无法获取抛NoSuchBeanDefinitionException
     *
     * @param clazz bean class
     * @param <T>   T
     * @return T
     */
    public static <T> T getBeanNonnull(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        try {
            return context.getBeansOfType(clazz).values();
        } catch (BeansException e) {
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
