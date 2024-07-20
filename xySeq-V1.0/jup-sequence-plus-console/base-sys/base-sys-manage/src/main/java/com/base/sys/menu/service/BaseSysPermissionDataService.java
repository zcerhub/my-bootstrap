package com.base.sys.menu.service;


import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.PermissionShowDto;
import com.base.sys.api.dto.PermissionTreeDto;
import com.base.sys.api.dto.SysDataRuleDto;
import com.base.sys.api.dto.SysMenuDataRuleDto;
import com.base.sys.api.entity.BaseSysPermissionData;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface BaseSysPermissionDataService extends BaseService<BaseSysPermissionData, String> {

    List<SysDataRuleDto>  getPermissionDataByRole(BaseSysPermissionData baseSysPermissionData) throws DaoException;

    List<SysMenuDataRuleDto>  getPermissionMenuDataByRole(BaseSysPermissionData baseSysPermissionData) throws DaoException;

    PermissionShowDto showDataRulePermissions(String roleId) throws DaoException;


    List<PermissionTreeDto>  showDataPermission(String roleId) throws DaoException;

}
