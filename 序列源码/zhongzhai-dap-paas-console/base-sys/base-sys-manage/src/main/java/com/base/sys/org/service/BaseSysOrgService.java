package com.base.sys.org.service;

import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.TreeDto;
import com.base.sys.api.entity.BaseSysOrg;

import java.util.List;

/**
 * 组织机构(BaseSysOrg)表服务接口
 *
 * @author makejava
 * @since 2021-01-14 09:32:26
 */
public interface BaseSysOrgService extends BaseService<BaseSysOrg, String> {

    List<TreeDto> queryTree(BaseSysOrg baseSysOrg) throws DaoException;

    Integer delOne(BaseSysOrg baseSysOrg) throws DaoException;

    void updateObjectP(BaseSysOrg baseSysOrg) throws DaoException;

    void updateObjectPs(JSONObject input) throws DaoException;
}
