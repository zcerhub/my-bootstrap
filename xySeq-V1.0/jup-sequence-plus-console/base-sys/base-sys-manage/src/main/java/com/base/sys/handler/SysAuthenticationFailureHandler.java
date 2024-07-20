package com.base.sys.handler;

import com.base.sys.api.common.Result;
import com.base.sys.auth.PreUsernamePasswordAuthenticationFilter;
import com.base.sys.utils.JacksonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录失败调用方法
 */
public class SysAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        try {
            Result result=new Result();
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            result.setMsg(e.getMessage());
            result.setCode(500);
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
