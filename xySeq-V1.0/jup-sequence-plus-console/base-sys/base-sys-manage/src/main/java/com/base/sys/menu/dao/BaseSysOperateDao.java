package com.base.sys.menu.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysOperate;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 16:38
 */
public interface BaseSysOperateDao extends BaseDao<BaseSysOperate,String> {

    List<BaseSysOperate> queryListByAttribute(BaseSysOperate po) throws DaoException;


    List<TreeNodeData> queryMenuOperate(Map<String,String> map) throws DaoException;
}
