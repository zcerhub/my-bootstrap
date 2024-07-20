package com.base.sys.org.service;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.SysRoleDto;
import com.base.sys.api.entity.BaseSysRole;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
public interface
BaseSysRoleService extends BaseService<BaseSysRole, String> {

    List<BaseSysRole>  getSysRoleByNameOrCode(BaseSysRole baseSysRole) throws DaoException;


    /**
     * 查询所有的角色，返回树形结构
     * @return
     */
    List<SysRoleDto>  getAllRole() throws DaoException;

    /**
     * 根据parentId查询该下面的角色
     * @param parentId
     * @return
     */
    List<BaseSysRole>  getRolesByParentId(String parentId) throws DaoException;

    /**
     * 修改角色的排序
     * @param input
     * @throws ServiceException
     */
    void updateRoleSort(JSONObject input) throws ServiceException, DaoException;



    Integer getRoleCountByParentId(String parentId) throws DaoException;
}
