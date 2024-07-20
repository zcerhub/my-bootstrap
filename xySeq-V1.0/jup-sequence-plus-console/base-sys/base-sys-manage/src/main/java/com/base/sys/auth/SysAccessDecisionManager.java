package com.base.sys.auth;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import com.base.sys.api.dto.AuthenticationUserDto;

public class SysAccessDecisionManager implements AccessDecisionManager {
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if(authentication instanceof AnonymousAuthenticationToken){
            throw new InsufficientAuthenticationException("未登录");
        }
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        String method = ((FilterInvocation) o).getRequest().getMethod();

        String newRequestUrl = method+requestUrl;

        AuthenticationUser authenticationUser=(AuthenticationUser)authentication.getPrincipal();
        AuthenticationUserDto authenticationUserDTo=authenticationUser.getAuthenticationUserDto();

        /**
         * 注册的地址
         */
        List<String> allRuls=authenticationUserDTo.getAllUrls();
        boolean flag=false;

        newRequestUrl = StringUtils.substringBefore(newRequestUrl, "?");
        
        if(allRuls!=null && allRuls.size()>0) {
            for (String url : allRuls) {
                if (url!=null && antPathMatcher.match(url, newRequestUrl)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                /**
                 * 拥有的地址
                 */
                List<String> urls = authenticationUserDTo.getUrls();
                if(CollectionUtils.isEmpty(urls)){
                    throw new AccessDeniedException("权限不足!");
                }
                for (String url : urls) {
                    if (url!=null &&  antPathMatcher.match(url, newRequestUrl)) {

                        return;
                    }
                }
                throw new AccessDeniedException("权限不足!");
            } else {
                return;
            }
        }else{
            return ;
        }
    }








    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }


}
