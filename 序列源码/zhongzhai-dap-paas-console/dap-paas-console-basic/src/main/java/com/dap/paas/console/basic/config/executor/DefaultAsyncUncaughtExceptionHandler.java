package com.dap.paas.console.basic.config.executor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class DefaultAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (log.isErrorEnabled()) {
            log.error(String.format("Unexpected error occurred invoking async " +
                    "method '%s'.", method), ex);
        }
    }
}
