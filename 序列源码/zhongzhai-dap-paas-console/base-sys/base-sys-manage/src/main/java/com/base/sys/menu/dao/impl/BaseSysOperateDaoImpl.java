package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.menu.dao.BaseSysOperateDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 17:51
 */
@Repository
public class BaseSysOperateDaoImpl extends AbstractBaseDaoImpl<BaseSysOperate,String> implements BaseSysOperateDao {


    @Override
    public List<BaseSysOperate> queryListByAttribute(BaseSysOperate po) throws DaoException {
        return null;
    }


    

    @Override
    public List<TreeNodeData> queryMenuOperate(Map<String,String> map) throws DaoException{
        List<TreeNodeData> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryMenuOperate"), map);
        }catch (Exception e){
            throw new DaoException("queryIdAndName执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }

}
