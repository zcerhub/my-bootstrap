package com.base.sys.org.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysUserRole;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/15 16:40
 */
public interface BaseSysUserRoleDao extends BaseDao<BaseSysUserRole,String> {
    List<String> getRoleIdByUser(String userID) throws DaoException;

}
