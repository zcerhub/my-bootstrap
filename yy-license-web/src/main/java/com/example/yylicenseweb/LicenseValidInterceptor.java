package com.example.yylicenseweb;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LicenseValidInterceptor extends HandlerInterceptorAdapter {

    volatile boolean lisenceValid;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("执行LicenseValidInterceptor.preHandle方法");
        if (!lisenceValid) {
            throw new RuntimeException("抱歉，证书校验不合法");
        }
        return true;
    }

    public void setLisenceValid(boolean lisenceValid) {
        this.lisenceValid = lisenceValid;
    }

    public boolean getLisenceValid() {
        return lisenceValid;
    }

}
