package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.menu.dao.BaseSysAppDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BaseSysAppDaoImpl extends AbstractBaseDaoImpl<BaseSysApp,String> implements BaseSysAppDao {
    @Override
    public List<BaseSysApp> selectByIds(List<String> ids) throws DaoException {
        List<BaseSysApp> result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".selectByIds"), ids);
        }catch (Exception e){
            throw new DaoException("selectByIds执行异常"+ ResultEnum.FAILED.getMsg());
        }
        return result;
    }
    @Override
    public List<TreeNodeData> queryIdAndName(Map<String,String> sqlmap) throws DaoException{
        List<TreeNodeData> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryIdAndName"), sqlmap);
        }catch (Exception e){
            throw new DaoException("queryIdAndName执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }
    @Override
    public BaseSysApp getObjectByCode(String code) throws DaoException{
        BaseSysApp result;
        try {
            result= getSqlSession().selectOne(getSqlNamespace().concat(".getObjectByCode"), code);
        }catch (Exception e){
            throw new DaoException("getObjectByCode执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }
}
