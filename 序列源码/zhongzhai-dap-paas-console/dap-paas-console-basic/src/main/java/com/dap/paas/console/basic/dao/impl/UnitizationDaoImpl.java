package com.dap.paas.console.basic.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.UnitizationDao;
import com.dap.paas.console.basic.entity.Unitization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnitizationDaoImpl extends AbstractBaseDaoImpl<Unitization,String> implements UnitizationDao {
    @Override
    public int selectCount(Unitization unitization) throws DaoException{
        int count;
        try {
            count = this.getSqlSession().selectOne(getSqlNamespace().concat(".selectCount"), unitization);
        }catch ( Exception e){
            throw new DaoException("selectCount方法调用异常："+e.getMessage());
        }
        return count;
    }

}
