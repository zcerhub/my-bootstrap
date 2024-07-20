package com.base.sys.auth;

import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.sys.api.dto.AuthenticationUserDto;

import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.config.SysSecurityConfig;
import com.base.sys.org.service.AuthenticationUserService;

import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.tenant.dao.TenantManageDao;
import com.dap.dmc.client.entity.ThirdUserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


@Component
public class SysUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthenticationUserService authenticationUserService;

    @Autowired
    private TenantManageDao tenantManageDao;

    @Lazy
    @Autowired
    private BaseSysUserService baseSysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] usernameSplit = username.split("@@_@@");
        String realUsername = usernameSplit[0];
        String tenantId = usernameSplit[1];
        String authorization="";
        if(usernameSplit.length>2){
             authorization = usernameSplit[2];
        }

        AuthenticationUserDto authenticationUserDto = authenticationUserService.getUserByLoginName(realUsername,tenantId,authorization);
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

    public void saveUserInfo(ThirdUserInfoVO thirdUserInfoVO, String tenantCode) {
        BaseSysUser user = new BaseSysUser();
        user.setAccount(thirdUserInfoVO.getUsername());
        user.setName(thirdUserInfoVO.getUsername());
        user.setEmail(thirdUserInfoVO.getEmail());
        user.setNickName(thirdUserInfoVO.getNickName());
        user.setStatus("1");
        user.setIsDelete("0");
        user.setPassword(SysSecurityConfig.DEFAULT_TOKEN_PASSWORD);
        user.setTenantId(tenantCode);
        /*如果不存在，则保存*/
        try {
            BaseSysUser baseSysUser = baseSysUserService.getBaseSysUser(thirdUserInfoVO.getUsername(),tenantCode);
            if (ObjectUtils.isEmpty(baseSysUser)) {
                baseSysUserService.saveObject(user);
                thirdUserInfoVO.setUserId(user.getId());
            }
        } catch (Exception e) {
            throw new ServiceException("SysUserDetailsService saveUserInfo failed");
        }
    }

    public String saveTenantInfo(String tenantCode) {
        String tenantId= SnowflakeIdWorker.getID() + Thread.currentThread().getId();
        TenantManageEntity tenantManageEntity = new TenantManageEntity();
        tenantManageEntity.setId(tenantId);
        tenantManageEntity.setTenantCode(tenantCode);
        tenantManageEntity.setTenantName(tenantCode);
        tenantManageEntity.setTenantCompany(tenantCode);
        tenantManageEntity.setAuthStatus("1");
        tenantManageEntity.setTenantStatus("1");
        /*如果不存在，则保存*/
        try {
            String tId = tenantManageDao.getIdByCodeOnLogin(tenantCode);
            if (ObjectUtils.isEmpty(tId)) {
                tenantManageDao.saveObject(tenantManageEntity);
                return tenantId;
            }
            return  tId;
        } catch (Exception e) {
            throw new ServiceException("SysUserDetailsService saveTenantInfo failed");
        }
    }

}
