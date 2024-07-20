package com.base.sys.org.service.impl;

import com.base.api.dto.Page;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.dto.UpdatePasswordDto;
import com.base.sys.api.dto.UserOutDto;
import com.base.sys.api.entity.BaseSysOrg;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.org.dao.BaseSysOrgDao;
import com.base.sys.org.dao.BaseSysUserDao;
import com.base.sys.org.service.BaseSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表(BaseSysUser)表服务实现类
 *
 * @author makejava
 * @since 2021-01-14 09:32:51
 */
@Slf4j
@Service("baseSysUserService")
public class BaseSysUserServiceImpl extends AbstractBaseServiceImpl<BaseSysUser, String> implements BaseSysUserService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    BaseSysUserDao baseSysUserDao;
    @Autowired
    BaseSysOrgDao baseSysOrgDao;



    @Override
    public UserOutDto getObjectP(String id) {
        return null;
    }

    @Override
    public void saveUser(BaseSysUser baseSysUser) {
        //加密
    	if(!StringUtils.startsWith(baseSysUser.getPassword(), "$2a$10$") || StringUtils.length(baseSysUser.getPassword()) < 32) {
    		baseSysUser.setPassword(bCryptPasswordEncoder.encode(baseSysUser.getPassword()));
    	}
        
        this.saveObject(baseSysUser);
    }

    @Override
    public BaseSysUser getByAccount(String account) throws ServiceException {
        BaseSysUser result=null;
        if(account!=null && !"".equals(account)) {
            try {
                result =  baseSysUserDao.getByAccount(account);
            } catch (Exception e) {
                log.error("getObjectById方法调用异常:",e);
                throw new ServiceException("getObjectById方法调用异常"+e.getMessage());
            }
        }else{
            throw new ServiceException("getObjectById方法调用参数:id为空");
        }
        return result;
    }

    @Override
    public BaseSysUser getBaseSysUser(String account , String tenantId) {
    	Map<String, String> map = new HashMap<>();
    	map.put("account", account);
    	map.put("tenantId", tenantId);
        try {
			return baseSysUserDao.getObjectByMap(map);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			return null;
		}
    }

    @Override
    public Result updatePassword(ServletRequest servletRequest, UpdatePasswordDto updatePasswordDto){
        HttpServletRequest httpServletRequest =(HttpServletRequest)servletRequest;
        HttpSession session = httpServletRequest.getSession();
        AuthenticationUserDto authenticationUserDTo = (AuthenticationUserDto) session.getAttribute(SysConstant.USER_INFO);

        if (!authenticationUserDTo.getAccount().equals(updatePasswordDto.getAccount())){
            return ResultUtils.error(205,"账号不匹配");
        }
        BaseSysUser baseSysUser  = this.getByAccount(updatePasswordDto.getAccount());
        if(!bCryptPasswordEncoder.matches(updatePasswordDto.getOldPassword(),baseSysUser.getPassword())){
            return ResultUtils.error(205,"原始密码错误");
        }
        if(baseSysUser != null){
            baseSysUser.setPassword(bCryptPasswordEncoder.encode(updatePasswordDto.getNewPassword()));
            if (this.updateObject(baseSysUser)==1){
                return ResultUtils.success(200,"修改成功");
            }else {
                return ResultUtils.error(ResultEnum.FAILED);
            }
        }else {
            return ResultUtils.error(205,"该账号不存在");
        }



    }

    @Override
    public BaseSysUser getUserById(String id) throws Exception{

            BaseSysUser baseSysUser = baseSysUserDao.getObjectById(id);
            if (baseSysUser == null){
                return null;
            }
            BaseSysOrg company = baseSysOrgDao.getObjectById(baseSysUser.getCompanyId());
            BaseSysOrg org = baseSysOrgDao.getObjectById(baseSysUser.getOrgId());
            if (company!=null && org!=null) {
                baseSysUser.setCompanyOrgName(company.getSimpleName());
                baseSysUser.setOrgName(org.getSimpleName());
            }
            return baseSysUser;


    }


   @Override
    public List<BaseSysUser> getUserList(BaseSysUser baseSysUser) throws Exception{

            List<BaseSysUser> baseSysUserList = baseSysUserDao.queryList(baseSysUser);
            List<BaseSysUser> resultList = new ArrayList<>();
            if (baseSysUserList.isEmpty()){
                return null;
            }
            for (BaseSysUser sysUser : baseSysUserList){
                BaseSysUser resultUser = this.getUserById(sysUser.getId());
                resultList.add(resultUser);
            }
            return resultList;


   }

   @Override
   public Page queryUserPage(BaseSysUser po) throws Exception{
        Page page = new Page();
       try {
           List<BaseSysUser> baseSysUserList = this.getUserList(po);

           List<BaseSysUser> userCompanyList = new ArrayList<>();

           /**
            * 遍历获取公司名称符合的用户
            */
           if (po.getCompanyOrgName() != null && !"".equals(po.getCompanyOrgName())){
               for (BaseSysUser userCompany : baseSysUserList){
                    if (checkCompanyName(po.getCompanyOrgName(),userCompany)){
                       userCompanyList.add(userCompany);
                   }
               }
           }else {
               userCompanyList.addAll(baseSysUserList);
           }
           if (userCompanyList.isEmpty()){
               page.setData(null);
               page.setTotalCount(new Integer(0));
           }
           List<BaseSysUser> userOrgList = new ArrayList<>();
           /**
            * 遍历获取部门名称符合的用户
            */
           if (po.getOrgName() != null && !"".equals(po.getOrgName())){
               for (BaseSysUser userOrg : userCompanyList){
                   if (checkOrgName(po.getOrgName(),userOrg)){
                       userOrgList.add(userOrg);
                   }
               }
           }else {
               userOrgList.addAll(userCompanyList);
           }
           page.setData(userOrgList);
           page.setTotalCount(new Integer(userOrgList.size()));
       }catch (Exception e){
           log.error("queryUserPage error:",e);
           throw new Exception(e.getMessage());
       }
       return page;
   }

    public boolean checkCompanyName(String companyName, BaseSysUser baseSysUser){
        if (baseSysUser.getCompanyOrgName() == null){
            return false;
        }
        if(baseSysUser.getCompanyOrgName().contains(companyName)){
            return true;
        }
        return false;
    }

    public boolean checkOrgName(String orgName, BaseSysUser baseSysUser){
        if (baseSysUser.getOrgName() == null){
            return false;
        }
        if(baseSysUser.getOrgName().contains(orgName)){
            return true;
        }
        return false;
    }

}
