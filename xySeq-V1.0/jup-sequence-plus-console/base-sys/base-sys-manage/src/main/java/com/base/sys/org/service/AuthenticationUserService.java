package com.base.sys.org.service;

import com.base.core.service.BaseService;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.BaseSysUser;


public interface AuthenticationUserService extends BaseService<BaseSysUser, String> {

  public AuthenticationUserDto getUserByLoginName(String loginName,String tenantId);

}
