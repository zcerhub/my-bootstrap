package com.base.sys.org.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysRole;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/15 16:39
 */
public interface BaseSysRoleDao extends BaseDao<BaseSysRole,String> {

    List<BaseSysRole> queryListByAttribute(BaseSysRole po) throws DaoException;

}
