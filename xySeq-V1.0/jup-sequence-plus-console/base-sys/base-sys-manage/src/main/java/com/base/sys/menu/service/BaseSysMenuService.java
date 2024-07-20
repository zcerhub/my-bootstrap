package com.base.sys.menu.service;


import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.SysMenuDataRuleDto;
import com.base.sys.api.dto.SysMenuDto;
import com.base.sys.api.entity.BaseSysMenu;

import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface BaseSysMenuService extends BaseService<BaseSysMenu, String> {

    /**
     * 查询所有的菜单，返回树结构
     * @return
     * @throws DaoException
     */
    List< SysMenuDto> getAllMenu() throws DaoException;


    List<BaseSysMenu> getMenuByParentId(String parentId) throws DaoException;

    /**
     * 查询所有的菜单、及子菜单下的操作点
     * @return
     * @throws DaoException
     */
    List<SysMenuDto> getAllMenuAndOperate() throws DaoException;

    /**
     * 查询所有的菜单、及菜单的数据权限
     * @return
     * @throws DaoException
     */
    List<SysMenuDataRuleDto> getAllMeunAndDataRule() throws DaoException;


    List<SysMenuDto> getAllMenuDataRuleOperate() throws DaoException;


    List<BaseSysMenu> getAllBaseSysMenus() throws DaoException;


    /**
     * 根据角色查询有权限的菜单
     * @param map
     * @return
     * @throws DaoException
     */
    List<BaseSysMenu> queryListByRole(Map map) throws DaoException;

    /**
     * 新增权限菜单进行唯一验证
     *
     * @param map 菜单对象
     * @return 返回Result
     * @throws ServiceException
     */
    Result selectMenuList(Map map) throws ServiceException;



    /**
     * 查询一个应用下所有的菜单，返回树结构
     * @return
     * @throws DaoException
     */
    List< SysMenuDto> getAllMenuByAppId(BaseSysMenu baseSysMenu) throws DaoException;

    List< SysMenuDto> getAllMenuByAppCode(String code) throws DaoException;

    List< SysMenuDto> getAllMenuByMenuIdList(List<String> ids) throws DaoException;
    List<BaseSysMenu> getAllBaseSysMenusByAppId(BaseSysMenu baseSysMenu) throws DaoException;


}
