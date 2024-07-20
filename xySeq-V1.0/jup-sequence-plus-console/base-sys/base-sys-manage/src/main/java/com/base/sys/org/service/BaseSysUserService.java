package com.base.sys.org.service;

import com.base.api.dto.Page;
import com.base.api.exception.DaoException;
import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.dto.UpdatePasswordDto;
import com.base.sys.api.dto.UserOutDto;
import com.base.sys.api.entity.BaseSysUser;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * 用户表(BaseSysUser)表服务接口
 *
 * @author makejava
 * @since 2021-01-14 09:32:50
 */
public interface BaseSysUserService extends BaseService<BaseSysUser, String> {

    UserOutDto getObjectP(String id) throws DaoException;

    void  saveUser( BaseSysUser baseSysUser  );

    BaseSysUser getByAccount(String account) throws DaoException;

    public BaseSysUser getBaseSysUser(String account , String tenantId);
    
    Result updatePassword(ServletRequest servletRequest, UpdatePasswordDto updatePasswordDto);

    BaseSysUser getUserById(String id) throws Exception;
    List<BaseSysUser> getUserList(BaseSysUser baseSysUser) throws Exception;

    Page queryUserPage(BaseSysUser po)throws Exception;
}
