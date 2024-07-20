package com.base.sys.dict.dao;


import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.BaseSysDictionaryInfo;

import java.util.List;
import java.util.Map;

/**
 * 字典详情(BaseSysDictionaryInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-21 14:58:03
 */
public interface BaseSysDictionaryInfoDao extends BaseDao<BaseSysDictionaryInfo,String> {
        Integer deleteByDictId(String dictId) throws DaoException;
}
