package com.base.sys.org.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.api.entity.BaseSysUserRole;
import com.base.sys.org.service.BaseSysUserRoleService;
import com.base.sys.org.service.BaseSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseSysUserRoleServiceImpl extends AbstractBaseServiceImpl<BaseSysUserRole, String> implements BaseSysUserRoleService {

    @Autowired
    private BaseSysUserService baseSysUserService;


    @Override
    public Page queryUserByRole(String roleId,String name) throws  Exception {
        try {
            List<BaseSysUser> userList = new ArrayList<>();
            BaseSysUserRole sqlUserRole = new BaseSysUserRole();
            sqlUserRole.setRoleId(roleId);
            List<BaseSysUserRole> userRoleList = this.queryList(sqlUserRole);
            if (userRoleList.isEmpty()) {
                return new Page();
            }
            /**
             * 查询角色下所有用户
             */
            for (BaseSysUserRole sysUserRole : userRoleList) {
                BaseSysUser sqlUser = baseSysUserService.getUserById(sysUserRole.getUserId());
                if (sqlUser != null) {
                    userList.add(sqlUser);
                }

            }
            List<BaseSysUser> resultList = new ArrayList<>();
            if (name != null && !("").equals(name)) {
                for (BaseSysUser user : userList) {
                    if (user != null && user.getName().contains(name)) {
                        resultList.add(user);
                    }
                }
            } else {
                resultList.addAll(userList);
            }
            Page page = new Page();
            page.setTotalCount(new Integer(resultList.size()));
            page.setData(resultList);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page();
        }
    }

}
