package com.base.sys.tenant.controller;

import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.auth.dataAuth.DataAuthInterceptor;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tenantmanage")
public class TenantManageController {

    private static final Logger logger = LoggerFactory.getLogger(TenantManageController.class);
    @Autowired
    private TenantManageDao tenantManageDao;

    @GetMapping("/list")
    Result<Object> queryTenantList(){
        try {
            List<TenantManageEntity> tenantManageEntityList = tenantManageDao.queryList(new TenantManageEntity());
            return ResultUtils.success(tenantManageEntityList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/info")
    Result<Object> queryTenant(){
        try {
            AuthenticationUserDto user = UserUtil.getUser();
            TenantManageEntity query=new TenantManageEntity();
            query.setId(user.getTenantId());
            List<TenantManageEntity> tenantManageEntityList = tenantManageDao.queryList(query);
            return ResultUtils.success(tenantManageEntityList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
