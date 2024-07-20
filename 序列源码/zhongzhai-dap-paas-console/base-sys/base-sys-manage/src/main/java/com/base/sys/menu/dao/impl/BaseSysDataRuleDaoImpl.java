package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermissionData;
import com.base.sys.menu.dao.BaseSysDataRuleDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 17:39
 */
@Repository
public class BaseSysDataRuleDaoImpl extends AbstractBaseDaoImpl<BaseSysDataRule,String> implements BaseSysDataRuleDao {


    @Override
    public List<BaseSysDataRule> queryListByAttribute(BaseSysDataRule po) throws DaoException {
        return null;
    }

    @Override
    public List<TreeNodeData> queryMenuDataRule(Map<String,String> map) throws DaoException{
        List<TreeNodeData> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryMenuDataRule"), map);
        }catch (Exception e){
            throw new DaoException("queryIdAndName执行异常," + e.getMessage());
        }
        return result;
    }


}
