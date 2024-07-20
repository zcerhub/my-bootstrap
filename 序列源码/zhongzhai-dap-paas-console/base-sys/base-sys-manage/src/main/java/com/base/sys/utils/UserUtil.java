package com.base.sys.utils;


import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.auth.AuthenticationUser;
import com.base.sys.auth.constant.TokenConstant;
import com.base.sys.auth.token.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.ObjectUtils;

public class UserUtil {

    /**
     * 获取用户信息
     * @return
     */
    public static AuthenticationUserDto getUser() {
        AuthenticationUserDto authenticationUserDto =null;
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
            Object userDto = ThreadLocalUtil.get(SysConstant.USER_INFO);
            if (!ObjectUtils.isEmpty(userDto)) {
                authenticationUserDto = ((AuthenticationUserDto) userDto);
            }
        }
        return authenticationUserDto;
    }

    /**
     * 获取统一登录token
     */
    public static String getAuthorization() {
        AuthenticationUserDto authenticationUserDto=getUser();
        return null==authenticationUserDto?null:authenticationUserDto.getAuthorization();
    }
}
