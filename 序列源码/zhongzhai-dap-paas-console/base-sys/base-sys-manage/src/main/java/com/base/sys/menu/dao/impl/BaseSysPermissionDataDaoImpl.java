package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.api.entity.BaseSysPermissionData;
import com.base.sys.menu.dao.BaseSysPermissionDataDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 17:55
 */
@Repository
public class BaseSysPermissionDataDaoImpl extends AbstractBaseDaoImpl<BaseSysPermissionData,String> implements BaseSysPermissionDataDao {

    @Override
    public  List<String> queryCheckData(BaseSysPermissionData baseSysPermissionData) throws DaoException{
        List<String> result= new ArrayList<>();
        try {
            List<String> sqlResult = this.getSqlSession().selectList(getSqlNamespace().concat(".queryCheckData"), baseSysPermissionData);
            for (String s :sqlResult){
                result.add("d_"+s);
            }
        }catch (Exception e){
            throw new DaoException("queryCheckData执行异常"+e.getMessage());
        }
        return result;
    }
    @Override
    public  Integer delObjectByData(BaseSysPermissionData baseSysPermissionData) throws DaoException{
        Integer esList=null;
        try {
            esList=this.getSqlSession().delete(getSqlNamespace().concat(".delObjectByData"),baseSysPermissionData);
        }catch (Exception e){
            throw new DaoException("delObjectByData执行异常",e.getMessage());
        }
        return esList;
    }

    @Override
    public Integer delObjectByMenu(BaseSysPermissionData baseSysPermissionData) throws DaoException{
        Integer esList=null;
        try {
            esList=this.getSqlSession().delete(getSqlNamespace().concat(".delObjectByMenu"),baseSysPermissionData);
        }catch (Exception e){
            throw new DaoException("delObjectByMenu执行异常",e.getMessage());
        }
        return esList;
    }

    @Override
    public List<String> queryCheckDataMenu(BaseSysPermissionData baseSysPermissionData) throws DaoException{
        List<String> result= new ArrayList<>();
        try {
            List<String> sqlResult = this.getSqlSession().selectList(getSqlNamespace().concat(".queryCheckDataMenu"), baseSysPermissionData);
            for (String s :sqlResult){
                result.add("m_"+s);
            }
        }catch (Exception e){
            throw new DaoException("queryCheckDataMenu执行异常"+e.getMessage());
        }
        return result;
    }



}
