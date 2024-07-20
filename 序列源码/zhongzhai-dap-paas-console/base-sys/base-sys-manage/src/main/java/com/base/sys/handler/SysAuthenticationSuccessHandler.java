package com.base.sys.handler;

import com.base.sys.api.common.Result;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.auth.AuthenticationUser;
import com.base.sys.auth.token.util.TokenUtil;
import com.base.sys.utils.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功调用方法
 */
public class SysAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private static final Logger log= LoggerFactory.getLogger(
            SysAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            Result result=new Result();
            AuthenticationUser authenticationUser=(AuthenticationUser)authentication.getPrincipal();
            AuthenticationUserDto authenticationUserDTo=authenticationUser.getAuthenticationUserDto();
            PrintWriter out = httpServletResponse.getWriter();
            result.setMsg("登录成功");
            result.setCode(200);
            result.setData(TokenUtil.createAuthInfo(authenticationUserDTo));

            //登录时将字典，菜单等基础数据写入缓存

            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
