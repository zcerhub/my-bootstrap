package com.base.sys.handler;

import com.base.sys.api.common.Result;
import com.base.sys.utils.JacksonUtil;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SysAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 认证失败调用接口
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            Result result=new Result();
            if (e instanceof InsufficientAuthenticationException) {
                result.setMsg("未登录，请登录!");
                result.setCode(401);
            }
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
