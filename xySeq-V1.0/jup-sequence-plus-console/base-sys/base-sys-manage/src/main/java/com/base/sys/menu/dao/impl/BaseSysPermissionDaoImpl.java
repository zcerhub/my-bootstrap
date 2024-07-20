package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.menu.dao.BaseSysPermissionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 17:53
 */
@Slf4j
@Repository
public class BaseSysPermissionDaoImpl extends AbstractBaseDaoImpl<BaseSysPermission, String> implements BaseSysPermissionDao {


    @Override
    public List<BaseSysPermission> selectByRoles(List<String> roleIds) throws DaoException {
        List<BaseSysPermission> result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".selectByRoles"), roleIds);
        }catch (Exception e){
            log.error("selectByRoles执行异常",e);
            throw new DaoException("selectByRoles执行异常"+e.getMessage());
        }
        return result;
    }


    @Override
    public List<String>  queryCheckMenu(BaseSysPermission baseSysPermission) throws DaoException{
        List<String> result = new ArrayList<>();
        try {
            List<String> sqlResult = this.getSqlSession().selectList(getSqlNamespace().concat(".queryCheckMenu"), baseSysPermission);
            for (String s :sqlResult){
                result.add("m_"+s);
            }
        }catch (Exception e){
            log.error("queryCheckMenu执行异常",e);
            throw new DaoException("queryCheckMenu执行异常"+e.getMessage());
        }
        return result;
    }

    @Override
    public List<String>  queryCheckOperate(BaseSysPermission baseSysPermission) throws DaoException{
        List<String> result = new ArrayList<>();
        try {
            List<String> sqlResult = this.getSqlSession().selectList(getSqlNamespace().concat(".queryCheckOperate"), baseSysPermission);
            for (String s :sqlResult){
                result.add("o_"+s);
            }
        }catch (Exception e){
            log.error("queryCheckOperate执行异常",e);
            throw new DaoException("queryCheckOperate执行异常"+e.getMessage());
        }
        return result;
    }

    @Override
    public  Integer delObjectByOperate(BaseSysPermission baseSysPermission) throws DaoException{
        Integer esList=null;
        try {
            esList=this.getSqlSession().delete(getSqlNamespace().concat(".delObjectByOperate"),baseSysPermission);
        }catch (Exception e){
            log.error("delObjectByOperate执行异常",e);
            throw new DaoException("delObjectByOperate执行异常",e.getMessage());
        }
        return esList;
    }

    @Override
   public Integer delObjectByMenu(BaseSysPermission baseSysPermission) throws DaoException{
       Integer esList=null;
       try {
           esList=this.getSqlSession().delete(getSqlNamespace().concat(".delObjectByMenu"),baseSysPermission);
       }catch (Exception e){
           log.error("delObjectByMenu执行异常",e);
           throw new DaoException("delObjectByMenu执行异常",e.getMessage());
       }
       return esList;
   }

    @Override
    public List<String> getRoleAppMenu(String appId, List<String> roleIds) throws DaoException{
        List<String> esList=null;
        Map<String,Object> sqlMap = new HashMap<>();
        sqlMap.put("appId",appId);
        sqlMap.put("roleIds",roleIds);
        try {
            esList=this.getSqlSession().selectList(getSqlNamespace().concat(".getRoleAppMenu"),sqlMap);
        }catch (Exception e){
            log.error("delObjectByMenu执行异常",e);
            throw new DaoException("delObjectByMenu执行异常",e.getMessage());
        }
        return esList;
    }
}
