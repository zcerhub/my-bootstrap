package com.dap.paas.console.basic.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.MachineRoom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApplicationDaoImpl extends AbstractBaseDaoImpl<Application,String> implements ApplicationDao {
    @Override
    public Application getObjectDataByMap(Map map) throws DaoException {
        Application result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".getObjectDataByMap",map);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.GetObjectById.getName()+":getObjectDataByMap"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public int checkAccessKey(Map map)  {
        return this.getSqlSession().selectOne(getSqlNamespace()+".checkAccessKey",map);
    }

    @Override
    public List<Application> findExist(List<Application> applicationList) {
        List<Application> result = null;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".findExist"), applicationList);
        }catch (Exception e){
        }
        return result;
    }

    @Override
    public Integer saveApplicationBatch(List<Application> applicationList) {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + ".saveApplicationBatch", applicationList);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<Application> queryListByIdList(List<String> ids) {
        List<Application> result = null;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".queryListByIdList"), ids);
        }catch (Exception e){
        }
        return result;
    }
    
    @Override
    public Integer updateApplicationBatch(List<Application> applicationList) {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + ".updateApplicationBatch", applicationList);
        } catch (Exception e) {
        }
        return result;
    }
}
