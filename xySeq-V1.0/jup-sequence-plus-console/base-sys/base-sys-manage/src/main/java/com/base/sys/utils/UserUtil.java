package com.base.sys.utils;


import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.auth.AuthenticationUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {

    /**
     * 获取用户信息
     * @return
     */
    public static AuthenticationUserDto getUser() {
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        //获取用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof AuthenticationUser) {
                AuthenticationUser authenticationUser = (AuthenticationUser) authentication.getPrincipal();
                authenticationUserDto = authenticationUser.getAuthenticationUserDto();
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                if (null != details) {
                    authenticationUserDto.setUserIp(details.getRemoteAddress());
                }
            }
        }

        if (authenticationUserDto == null || authenticationUserDto.getTenantId() == null) {
            ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes());
            if (servletRequestAttributes == null) {
                return null;
            }
            //从springsecurity取不到用户信息时，从request中取
            HttpServletRequest request = servletRequestAttributes.getRequest();
            authenticationUserDto = (AuthenticationUserDto) request.getSession().getAttribute(SysConstant.USER_INFO);
        }
        return authenticationUserDto;
    }


}
