package com.base.sys.menu.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.api.entity.BaseSysPermissionData;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 16:38
 */
public interface BaseSysPermissionDataDao  extends BaseDao<BaseSysPermissionData,String> {

    List<String> queryCheckData(BaseSysPermissionData baseSysPermissionData) throws DaoException;

    Integer delObjectByData(BaseSysPermissionData baseSysPermissionData) throws DaoException;
    Integer delObjectByMenu(BaseSysPermissionData baseSysPermissionData) throws DaoException;

    List<String> queryCheckDataMenu(BaseSysPermissionData baseSysPermissionData) throws DaoException;


}
