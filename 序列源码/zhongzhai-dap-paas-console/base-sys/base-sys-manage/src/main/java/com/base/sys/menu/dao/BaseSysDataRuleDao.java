package com.base.sys.menu.dao;


import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermissionData;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 16:35
 */
public interface BaseSysDataRuleDao extends BaseDao<BaseSysDataRule,String> {

    List<BaseSysDataRule> queryListByAttribute(BaseSysDataRule po) throws DaoException;

    List<TreeNodeData> queryMenuDataRule(Map<String,String> map) throws DaoException;



}
