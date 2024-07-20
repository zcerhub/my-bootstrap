package com.base.sys.menu.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysApp;

import java.util.List;
import java.util.Map;

public interface BaseSysAppDao extends BaseDao<BaseSysApp,String> {
  List<BaseSysApp> selectByIds(List<String> ids) throws DaoException;
  List<TreeNodeData> queryIdAndName(Map<String,String> sqlmap) throws DaoException;
  BaseSysApp getObjectByCode(String code) throws DaoException;
}
