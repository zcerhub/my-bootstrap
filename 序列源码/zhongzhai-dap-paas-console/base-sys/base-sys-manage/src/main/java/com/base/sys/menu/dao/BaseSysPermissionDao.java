package com.base.sys.menu.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysPermission;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 16:39
 */
public interface BaseSysPermissionDao extends BaseDao<BaseSysPermission,String> {

    List<BaseSysPermission>  selectByRoles(List<String> roleIds) throws DaoException;



    List<String>  queryCheckMenu(BaseSysPermission baseSysPermission) throws DaoException;

    List<String>  queryCheckOperate(BaseSysPermission baseSysPermission) throws DaoException;

    Integer delObjectByOperate(BaseSysPermission baseSysPermission) throws DaoException;
    Integer delObjectByMenu(BaseSysPermission baseSysPermission) throws DaoException;

    List<String> getRoleAppMenu(String appId, List<String> roleIds) throws DaoException;
}
