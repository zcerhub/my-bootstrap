package com.base.sys.org.service;


import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.api.entity.BaseSysUserRole;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface BaseSysUserRoleService extends BaseService<BaseSysUserRole, String> {
    Page queryUserByRole(String roleId, String name)throws  Exception;
}
