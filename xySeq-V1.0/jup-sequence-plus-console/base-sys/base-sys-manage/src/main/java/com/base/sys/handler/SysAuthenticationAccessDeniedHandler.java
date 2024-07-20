package com.base.sys.handler;

import com.base.sys.api.common.Result;
import com.base.sys.utils.JacksonUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有异常授权调用【主要面对一场操作】
 */
public class SysAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
       try {
           Result result = new Result();
           httpServletResponse.setContentType("application/json;charset=utf-8");
           PrintWriter out = httpServletResponse.getWriter();
           result.setCode(405);
           result.setMsg("没有权限");
           out.write(JacksonUtil.obj2json(result));
           out.flush();
           out.close();
       } catch (Exception exception) {
           exception.printStackTrace();
       }
    }
}
