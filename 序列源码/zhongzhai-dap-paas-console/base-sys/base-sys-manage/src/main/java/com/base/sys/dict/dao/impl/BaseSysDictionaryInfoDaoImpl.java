package com.base.sys.dict.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysDictionaryInfo;
import com.base.sys.dict.dao.BaseSysDictionaryInfoDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BaseSysDictionaryInfoDaoImpl extends AbstractBaseDaoImpl<BaseSysDictionaryInfo,String> implements BaseSysDictionaryInfoDao {

    @Override
    public Integer deleteByDictId(String dictId) throws DaoException{
        Integer result=null;
        try {
            result= this.getSqlSession().delete(getSqlNamespace()+".deleteByDictId", dictId);
        }catch (Exception e){
            throw new DaoException("dictId执行异常:"+e.getMessage());
        }
        return result;
     }
}
