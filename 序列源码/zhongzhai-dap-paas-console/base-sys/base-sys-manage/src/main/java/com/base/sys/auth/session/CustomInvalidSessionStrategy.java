package com.base.sys.auth.session;

import com.base.sys.api.cache.DataAuthMapCache;
import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.cache.UserAppCache;
import com.base.sys.api.cache.UserInfoCache;
import com.base.sys.api.common.Result;
import com.base.sys.utils.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当session失效后的处理逻辑
 */
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    private static final Logger log= LoggerFactory.getLogger(CustomInvalidSessionStrategy.class);

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 要将浏览器中的cookie的jsessionid删除
        cancelCookie(request, response);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Result result=new Result();
        result.setCode(401);
        result.setMsg("登录已超时，请重新登录");
        //session失效时清除本地缓存数据
        DictMapCache.clear();
        UserInfoCache.clear();
        DataAuthMapCache.clear();
        UserAppCache.clear();
        try {
            out.write(JacksonUtil.obj2json(result));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("CustomInvalidSessionStrategy error",e);
        }
    }



    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }


    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }


}
