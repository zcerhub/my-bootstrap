package com.base.sys.handler;

import com.base.sys.api.common.Result;
import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.org.service.impl.BaseSysUserServiceImpl;
import com.base.sys.utils.JacksonUtil;
import com.base.sys.utils.SpringUtilEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
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
    private static final Logger logger= LoggerFactory.getLogger(
            SysAuthenticationFailureHandler.class);


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        try {
            //更新单位时间内的密码失败次数
            BaseSysUserService baseSysUserService= SpringUtilEx.getBean(BaseSysUserServiceImpl.class);
            baseSysUserService.updateUserPasswdUseCount((String)httpServletRequest.getAttribute("username"),(String)httpServletRequest.getAttribute("password"),true);
            //返回失败信息
            Result result=new Result();
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            result.setMsg(e.getMessage());
            result.setCode(500);
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();

        } catch (Exception exception) {
            logger.error("登录失败！",exception);
            exception.printStackTrace();
        }
    }
}
