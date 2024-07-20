package com.base.sys.handler;

import com.base.sys.api.cache.DataAuthMapCache;
import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.cache.UserAppCache;
import com.base.sys.api.cache.UserInfoCache;
import com.base.sys.api.common.Result;
import com.base.sys.utils.JacksonUtil;
import com.base.sys.utils.ThreadLocalUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登出成功
 */
public class SysLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        try {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            Result result=new Result();
            PrintWriter out = httpServletResponse.getWriter();
            result.setMsg("登出成功");
            result.setCode(200);
            ThreadLocalUtil.clear();
            //登出时清除本地缓存数据
            DictMapCache.clear();
            UserInfoCache.clear();
            DataAuthMapCache.clear();
            UserAppCache.clear();
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
