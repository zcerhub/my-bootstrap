package com.dap.paas.console.basic.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.MachineRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class ApplicationDaoImpl extends AbstractBaseDaoImpl<Application,String> implements ApplicationDao {
    @Override
    public Application getObjectDataByMap(Map map) throws DaoException {
        Application result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".getObjectDataByMap",map);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getObjectDataByMap"+e.getMessage();
            log.error(msg,e);
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public int checkAccessKey(Map map)  {
        return this.getSqlSession().selectOne(getSqlNamespace()+".checkAccessKey",map);
    }

}
