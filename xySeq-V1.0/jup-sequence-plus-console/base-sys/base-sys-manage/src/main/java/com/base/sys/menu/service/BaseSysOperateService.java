package com.base.sys.menu.service;


import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysOperate;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/14
 */

public interface BaseSysOperateService  extends BaseService<BaseSysOperate, String> {


    List<BaseSysOperate> getOperateByMenuId(String menuId) throws DaoException;

    /**
     * 根据角色查询有权限操作点
     * @param map
     * @return
     * @throws DaoException
     */
    List<BaseSysOperate> queryListByRole(Map map) throws DaoException;


    List<TreeNodeData>   queryIdAndName(String appId,String tenantId);

}
