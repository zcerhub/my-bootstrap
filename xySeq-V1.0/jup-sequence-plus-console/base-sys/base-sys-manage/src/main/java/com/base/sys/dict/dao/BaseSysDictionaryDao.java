package com.base.sys.dict.dao;


import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysDictionary;

/**
 * 系统字典表(BaseSysDictionary)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-21 14:57:04
 */
public interface BaseSysDictionaryDao extends BaseDao<BaseSysDictionary,String> {
    Integer selectDictCodeTotal(String dictCode) throws DaoException;
}
