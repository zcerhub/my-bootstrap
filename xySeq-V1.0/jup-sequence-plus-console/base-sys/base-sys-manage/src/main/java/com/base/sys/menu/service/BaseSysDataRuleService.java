package com.base.sys.menu.service;


import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysDataRule;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface BaseSysDataRuleService extends BaseService<BaseSysDataRule, String> {

    List<BaseSysDataRule> getSysDataListByMenuId(String menuId) throws ServiceException, DaoException;
    List<TreeNodeData>   queryIdAndName(String appId, String tenantId);
}
