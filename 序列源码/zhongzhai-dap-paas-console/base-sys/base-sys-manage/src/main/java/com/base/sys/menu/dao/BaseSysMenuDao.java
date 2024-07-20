package com.base.sys.menu.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysMenu;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 16:37
 */
public interface BaseSysMenuDao extends BaseDao<BaseSysMenu,String> {

    List<BaseSysMenu> queryListByAttribute(BaseSysMenu po) throws DaoException;
    /**
     * 查询该角色下有权限的菜单
     * @param map
     * @return
     * @throws DaoException
     */

    Integer selectMenuList(Map map) throws DaoException;



    List<TreeNodeData> queryIdAndName(Map<String,String> map) throws DaoException;
    List<String> queryIdByApp(Map<String,String> map) throws DaoException;

    List<BaseSysMenu> queryListByIds(List<String> ids) throws DaoException;
}
