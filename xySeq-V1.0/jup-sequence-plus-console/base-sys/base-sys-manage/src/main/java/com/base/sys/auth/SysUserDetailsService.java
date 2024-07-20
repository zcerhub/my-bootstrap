package com.base.sys.auth;

import com.base.sys.api.dto.AuthenticationUserDto;

import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.org.service.AuthenticationUserService;

import com.base.sys.tenant.dao.TenantManageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class SysUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthenticationUserService authenticationUserService;

    @Autowired
    private TenantManageDao tenantManageDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] usernameSplit = username.split("@@_@@");
        String realUsername = usernameSplit[0];
        String tenantId = usernameSplit[1];

        AuthenticationUserDto authenticationUserDto = authenticationUserService.getUserByLoginName(realUsername,tenantId);
        authenticationUserDto.setTenantId(tenantId);
        if (null == authenticationUserDto.getAccount()) {
            throw new UsernameNotFoundException("该用户不存在!");
        } else{
            AuthenticationUser authenticationUser = new AuthenticationUser
                    (authenticationUserDto.getAccount(), authenticationUserDto.getPassword(),
                            authenticationUserDto);
            return authenticationUser;

        }
    }

}
