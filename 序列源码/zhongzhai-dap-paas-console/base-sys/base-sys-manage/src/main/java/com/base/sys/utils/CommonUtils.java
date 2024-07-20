package com.base.sys.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

/**
 * @author qqqab
 */
public class CommonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * @param totalTimes  尝试调用次数
     * @param remainTimes 剩余次数
     * @param supplier    操作
     * @param <T>         返回类型
     * @return 调用结果
     */
    public static <T> T tryTimes(int totalTimes, int remainTimes, Supplier<T> supplier) {
        String name = Thread.currentThread().getName();
        try {
            if (remainTimes <= 0) {
                LOGGER.warn("线程{},共尝试调用{}次,调用失败,结束", name, totalTimes);
            } else {
                remainTimes--;
                T t = supplier.get();
                LOGGER.info("线程{},尝试第{}次调用,调用成功,结束", name, (totalTimes - remainTimes));
                return t;
            }
        } catch (Exception e) {
            LOGGER.warn("线程{},尝试第{}次调用，调用失败,{}", name, (totalTimes - remainTimes), e.getMessage());
            return tryTimes(totalTimes, remainTimes, supplier);
        }
        return null;
    }

    public static RestTemplate restTemplate(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(connectTimeout);
        httpRequestFactory.setReadTimeout(readTimeout);
        return new RestTemplate(httpRequestFactory);
    }
}
