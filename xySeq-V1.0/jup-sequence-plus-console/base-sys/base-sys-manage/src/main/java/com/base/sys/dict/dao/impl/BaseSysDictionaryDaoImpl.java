package com.base.sys.dict.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysDictionary;
import com.base.sys.dict.dao.BaseSysDictionaryDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BaseSysDictionaryDaoImpl extends AbstractBaseDaoImpl<BaseSysDictionary,String> implements BaseSysDictionaryDao {

    @Override
    public Integer selectDictCodeTotal(String dictCode) throws DaoException{
        Integer result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".selectDictCodeTotal", dictCode);
        }catch (Exception e){
            log.error("selectDictCodeTotal执行异常",e);

            throw new DaoException("selectDictCodeTotal执行异常:"+e.getMessage());
        }
        return result;
    }
}
