package com.base.sys.org.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysUser;

/**
 * 用户表(BaseSysUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-14 09:32:50
 */
public interface BaseSysUserDao extends BaseDao<BaseSysUser,String> {
     BaseSysUser getByAccount(String account) throws DaoException;
     Integer selectAccountTotal(String account) throws DaoException;
}
