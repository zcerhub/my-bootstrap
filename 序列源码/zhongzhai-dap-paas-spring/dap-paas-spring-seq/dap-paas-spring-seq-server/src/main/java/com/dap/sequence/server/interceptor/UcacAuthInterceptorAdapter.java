package com.dap.sequence.server.interceptor;


import com.dap.dmc.client.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Ucac 权限校验拦截器
 * @author XIAYUANA
 */

@Component
public class UcacAuthInterceptorAdapter implements HandlerInterceptor {

    @Autowired
    private AuthService authService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) {
        String url = request.getRequestURL().toString();
        if(url.contains("accessId")){
            return authService.urlVerify(url);
        }
        return true;
    }
}
