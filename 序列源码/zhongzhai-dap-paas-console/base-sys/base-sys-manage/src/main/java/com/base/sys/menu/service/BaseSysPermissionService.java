package com.base.sys.menu.service;


import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.AddCheckMenuDto;
import com.base.sys.api.dto.AppMenuTreeDto;
import com.base.sys.api.dto.PermissionShowDto;
import com.base.sys.api.dto.SysMenuDto;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermission;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface BaseSysPermissionService extends BaseService<BaseSysPermission, String> {

    /**
     * 根据角色查询
     *
     * @param roleId
     * @return
     * @throws DaoException
     */
    List<SysMenuDto> getSysMeunPermissionByRoleId(String roleId) throws DaoException;

    List<BaseSysOperate> getSysOperatePermissionByRoleAndMenu(BaseSysPermission baseSysPermission) throws DaoException;

    List<SysMenuDto> showMenuAndDataPermission(String roleId) throws DaoException;

    PermissionShowDto showOperatePermissions(String roleId,String appId) throws DaoException;

    List<BaseSysPermission> getPermissionByRoleId(String roleId) throws DaoException;

    List<String> showOperatePermission(String roleId)  throws DaoException;

    List<BaseSysPermission>  selectByRoles(List<String> roleIds) throws DaoException;

    Result getAppMenu(String roleId)throws DaoException;

    Result addPermission(AddCheckMenuDto addCheckMenuDto)  throws DaoException;

    List<BaseSysPermission>  selectByUser(Map map) throws DaoException;

}
