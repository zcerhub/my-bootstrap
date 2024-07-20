package com.base.sys.menu.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.common.SysMenuEnum;
import com.base.sys.api.dto.*;
import com.base.sys.api.entity.*;
import com.base.sys.menu.dao.BaseSysAppDao;
import com.base.sys.menu.dao.BaseSysMenuDao;
import com.base.sys.menu.dao.BaseSysPermissionDao;
import com.base.sys.menu.service.BaseSysDataRuleService;
import com.base.sys.menu.service.BaseSysMenuService;
import com.base.sys.menu.service.BaseSysOperateService;
import com.base.sys.menu.service.BaseSysPermissionService;
import com.base.sys.org.dao.BaseSysUserRoleDao;
import com.base.sys.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;


@Service
public class BaseSysMenuServiceImpl extends AbstractBaseServiceImpl<BaseSysMenu, String> implements BaseSysMenuService {

    @Lazy
    @Resource
    private BaseSysOperateService baseSysOperateService;

    @Resource
    private BaseSysDataRuleService baseSysDataRuleService;

    @Resource
    private BaseSysMenuDao baseSysMenuDaoImpl;
    @Lazy
    @Resource
    private BaseSysPermissionService baseSysPermissionService;
    @Resource
    private BaseSysPermissionDao permissionDao;
    @Resource
    private BaseSysAppDao baseSysAppDao;

    @Resource
    private BaseSysUserRoleDao userRoleDao;



    @Override
    public List<SysMenuDto> getAllMenu() throws DaoException {
        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<>();
        List<SysMenuDto> sysMenuDtos= new ArrayList<>();

        Map map = new HashMap();
        List<BaseSysMenu> baseSysMenus= this.getBaseDao().queryList(map);
         if(CollectionUtils.isNotEmpty(baseSysMenus)){
             for (BaseSysMenu menu : baseSysMenus) {
                 SysMenuDto sysMenuDto = new SysMenuDto();
                 BeanUtils.copyProperties(menu, sysMenuDto);
                 sysMenuDto.setMenuId(menu.getId());
                 sysMenuDtos.add(sysMenuDto);
             }

             // 按照Sort升序
             Collections.sort(sysMenuDtos, new Comparator<SysMenuDto>() {
                 @Override
                 public int compare(SysMenuDto o1, SysMenuDto o2) {
                     //升序
                     return o1.getSort().compareTo(o2.getSort());
                 }
             });


             //整理sysMenuDtos
             for (int i = 0; i < sysMenuDtos.size(); i++) {
                 // 一级菜单没有parentId
                 if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                     menuList.add(sysMenuDtos.get(i));
                 }
             }


             for (SysMenuDto menu : menuList) {
                 menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
             }
         }
        return menuList;
    }







    @Override
    public List<BaseSysMenu> getMenuByParentId(String parentId) throws DaoException {
        BaseSysMenu baseSysMenu = new BaseSysMenu();
        baseSysMenu.setParentId(parentId);
        List<BaseSysMenu> menuList = baseSysMenuDaoImpl.queryListByAttribute(baseSysMenu);
        return menuList;
    }

    @Override
    public List<SysMenuDto> getAllMenuAndOperate() throws DaoException {
        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<>();
        List<SysMenuDto> sysMenuDtos = new ArrayList<>();
        Map map = new HashMap();
        List<BaseSysMenu> baseSysMenus = this.getBaseDao().queryList(map);
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu menu : baseSysMenus) {
                SysMenuDto sysMenuDto = new SysMenuDto();
                BeanUtils.copyProperties(menu, sysMenuDto);
                sysMenuDto.setMenuId(menu.getId());
                //如果是叶子节点的菜单，查询该菜单下的操作点
                if (SysMenuEnum.LEAF.getCode().equals(sysMenuDto.getIsLeaf())) {
                    List<BaseSysOperate> baseSysOperates = baseSysOperateService.getOperateByMenuId(sysMenuDto.getMenuId());
                    if (CollectionUtils.isNotEmpty(baseSysOperates)) {
                        List<SysOperateDto> sysOperateDtoList = new ArrayList<>();
                        for (BaseSysOperate baseSysOperate : baseSysOperates) {
                            SysOperateDto sysOperateDto = new SysOperateDto();
                            BeanUtils.copyProperties(baseSysOperate, sysOperateDto);
                            sysOperateDto.setOperateId(baseSysOperate.getId());
                            sysOperateDtoList.add(sysOperateDto);
                        }
                        sysMenuDto.setSysOperateDtoList(sysOperateDtoList);
                    }
                }
                sysMenuDtos.add(sysMenuDto);

            }
        }
        //整理sysMenuDtos
        for (int i = 0; i < sysMenuDtos.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                menuList.add(sysMenuDtos.get(i));
            }
        }
        for (SysMenuDto menu : menuList) {
            menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
        }
        return menuList;
    }


    public List<SysMenuDto> getChild(String id, List<SysMenuDto> allMenu) {
        // 子菜单
        List<SysMenuDto> childList = new ArrayList<>();
        for (SysMenuDto menu : allMenu) {

            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(menu.getParentId())) {
                if (menu.getParentId().equals(id)) {
                    childList.add(menu);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (SysMenuDto menu : childList) {
            if (!StringUtils.isEmpty(menu.getParentId())) {
                // 递归
                menu.setChildren(getChild(menu.getMenuId(), allMenu));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }




    @Override
    public List<SysMenuDataRuleDto> getAllMeunAndDataRule() throws DaoException {
        //最后的结果
        List<SysMenuDataRuleDto> sysMenuDataRuleDtoList = new ArrayList<>();
        List<SysMenuDataRuleDto> sysMenuDataRuleDtos = new ArrayList<>();

        Map map = new HashMap();
        List<BaseSysMenu> baseSysMenus = this.getBaseDao().queryList(map);
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu menu : baseSysMenus) {
                SysMenuDataRuleDto sysMenuDataRuleDto = new SysMenuDataRuleDto();
                BeanUtils.copyProperties(menu, sysMenuDataRuleDto);
                sysMenuDataRuleDto.setMenuId(menu.getId());
                //如果是叶子节点的菜单，查询该菜单下的数据规则
                if (SysMenuEnum.LEAF.getCode().equals(sysMenuDataRuleDto.getIsLeaf())) {
                    List<BaseSysDataRule> dataRules = baseSysDataRuleService.getSysDataListByMenuId(menu.getId());
                    if (CollectionUtils.isNotEmpty(dataRules)) {
                        List<SysDataRuleDto> sysDataRuleDtoList = new ArrayList<>();
                        for (BaseSysDataRule baseSysDataRule : dataRules) {
                            SysDataRuleDto sysDataRuleDto = new SysDataRuleDto();
                            BeanUtils.copyProperties(baseSysDataRule, sysDataRuleDto);
                            sysDataRuleDto.setDataRuleId(baseSysDataRule.getId());
                            sysDataRuleDtoList.add(sysDataRuleDto);
                        }
                        sysMenuDataRuleDto.setSysDataRuleDtoList(sysDataRuleDtoList);
                    }
                }
                sysMenuDataRuleDtos.add(sysMenuDataRuleDto);

            }
        }
        //整理sysMenuDataRuleDtos
        for (int i = 0; i < sysMenuDataRuleDtos.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(sysMenuDataRuleDtos.get(i).getParentId())) {
                sysMenuDataRuleDtoList.add(sysMenuDataRuleDtos.get(i));
            }
        }
        return sysMenuDataRuleDtoList;
    }


    @Override
    public List<SysMenuDto> getAllMenuDataRuleOperate() throws DaoException {

        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<SysMenuDto>();

        List<SysMenuDto> sysMenuDtos = new ArrayList<>();

        Map map = new HashMap();
        List<BaseSysMenu> baseSysMenus = this.getBaseDao().queryList(map);
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu menu : baseSysMenus) {
                SysMenuDto sysMenuDto = new SysMenuDto();
                BeanUtils.copyProperties(menu, sysMenuDto);
                sysMenuDto.setMenuId(menu.getId());
                //如果是叶子节点的菜单，查询该菜单下的操作点
                if (SysMenuEnum.LEAF.getCode().equals(sysMenuDto.getIsLeaf())) {
                    //查询下面的操作点
                    List<BaseSysOperate> baseSysOperates = baseSysOperateService.getOperateByMenuId(sysMenuDto.getMenuId());
                    if (CollectionUtils.isNotEmpty(baseSysOperates)) {
                        List<SysOperateDto> sysOperateDtoList = new ArrayList<>();
                        for (BaseSysOperate baseSysOperate : baseSysOperates) {
                            SysOperateDto sysOperateDto = new SysOperateDto();
                            BeanUtils.copyProperties(baseSysOperate, sysOperateDto);
                            sysOperateDto.setOperateId(baseSysOperate.getId());
                            sysOperateDtoList.add(sysOperateDto);
                        }
                        sysMenuDto.setSysOperateDtoList(sysOperateDtoList);
                    }
                    //查询下面的的数据规则
                    List<BaseSysDataRule>  baseSysDataRules= baseSysDataRuleService.getSysDataListByMenuId(menu.getId());
                    if(CollectionUtils.isNotEmpty(baseSysDataRules)){
                     List<SysDataRuleDto>    sysDataRuleDtoList = new ArrayList<>();
                        for (BaseSysDataRule baseSysData :baseSysDataRules){
                            SysDataRuleDto sysDataRuleDto =new SysDataRuleDto();
                            BeanUtils.copyProperties(baseSysData,sysDataRuleDto);
                            sysDataRuleDto.setDataRuleId(baseSysData.getId());
                            sysDataRuleDtoList.add(sysDataRuleDto);
                        }

                        sysMenuDto.setSysDataRuleDtoList(sysDataRuleDtoList);
                    }


                }
                sysMenuDtos.add(sysMenuDto);

            }
        }

        // 整理对象sysMenuDtos
        for (int i = 0; i < sysMenuDtos.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                menuList.add(sysMenuDtos.get(i));
            }
        }
        for (SysMenuDto menu : menuList) {
            menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
        }

        return menuList;
    }




    @Override
    public List<BaseSysMenu> getAllBaseSysMenus() throws DaoException {
        Map map = new HashMap();
        List<BaseSysMenu> baseSysMenus= this.getBaseDao().queryList(map);
        return baseSysMenus;
    }



    @Override
    public List<BaseSysMenu> queryListByRole(Map map) throws DaoException {


        List<BaseSysMenu> result = new ArrayList<>();
        List<BaseSysPermission> permissionList = baseSysPermissionService.queryList(map);
        if (permissionList == null){
            return null;
        }
        for (BaseSysPermission sysPermission: permissionList){
            result.add(this.getObjectById(sysPermission.getMenuId()));
        }

        return result;
    }

    /**
     * 新增权限菜单进行唯一验证
     *
     * @param map 菜单对象
     * @return 返回Result
     * @throws ServiceException
     */
    @Override
    public Result selectMenuList(Map map) throws ServiceException {
        Integer baseSysMeunCount = null;
        try {
            baseSysMeunCount= baseSysMenuDaoImpl.selectMenuList(map);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        if(baseSysMeunCount > 0){
            return ResultUtils.error(ResultEnum.EXIST);
        }else {
            return  ResultUtils.success(ResultEnum.SUCCESS);
        }
    }



    @Override
    public List<SysMenuDto> getAllMenuByAppId(BaseSysMenu baseSysMenu) throws DaoException {
        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<>();
        List<SysMenuDto> sysMenuDtos= new ArrayList<>();

        Map map = new HashMap();
        map.put("appId",baseSysMenu.getAppId());
        List<BaseSysMenu> baseSysMenus= this.getBaseDao().queryList(map);
        if(CollectionUtils.isNotEmpty(baseSysMenus)){
            for (BaseSysMenu menu : baseSysMenus) {
                SysMenuDto sysMenuDto = new SysMenuDto();
                BeanUtils.copyProperties(menu, sysMenuDto);
                sysMenuDto.setMenuId(menu.getId());
                sysMenuDto.setAppId(menu.getAppId());
                sysMenuDtos.add(sysMenuDto);
            }

            // 按照Sort升序
            Collections.sort(sysMenuDtos, Comparator.comparing(SysMenuDto::getSort));

            //整理sysMenuDtos
            for (int i = 0; i < sysMenuDtos.size(); i++) {
                // 一级菜单没有parentId
                if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                    menuList.add(sysMenuDtos.get(i));
                }
            }

            for (SysMenuDto menu : menuList) {
                menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
            }
        }
        return menuList;
    }



    @Override
    public List<SysMenuDto> getAllMenuByAppCode(String code) throws DaoException{
        BaseSysApp baseSysApp = baseSysAppDao.getObjectByCode(code);
        BaseSysMenu baseSysMenu = new BaseSysMenu();
        baseSysMenu.setAppId(baseSysApp.getId());
        AuthenticationUserDto userDto = UserUtil.getUser();
        List<String> roleIds = userRoleDao.getRoleIdByUser(userDto.getUserId());
        List<String> menuId = permissionDao.getRoleAppMenu(baseSysApp.getId(),roleIds);

        return getAllMenuByMenuIdList(menuId);
    }
    @Override
    public List<SysMenuDto> getAllMenuByMenuIdList(List<String> ids) throws DaoException{
        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<>();
        List<SysMenuDto> sysMenuDtos= new ArrayList<>();

        List<BaseSysMenu> baseSysMenus= baseSysMenuDaoImpl.queryListByIds(ids);
        if(CollectionUtils.isNotEmpty(baseSysMenus)){
            for (BaseSysMenu menu : baseSysMenus) {
                SysMenuDto sysMenuDto = new SysMenuDto();
                BeanUtils.copyProperties(menu, sysMenuDto);
                sysMenuDto.setMenuId(menu.getId());
                sysMenuDto.setAppId(menu.getAppId());
                sysMenuDtos.add(sysMenuDto);
            }

            // 按照Sort升序
            Collections.sort(sysMenuDtos, Comparator.comparing(SysMenuDto::getSort));

            //整理sysMenuDtos
            for (int i = 0; i < sysMenuDtos.size(); i++) {
                // 一级菜单没有parentId
                if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                    menuList.add(sysMenuDtos.get(i));
                }
            }

            for (SysMenuDto menu : menuList) {
                menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
            }
        }
        return menuList;
    }

    @Override
    public List<BaseSysMenu> getAllBaseSysMenusByAppId(BaseSysMenu baseSysMenu) throws DaoException {
        Map map = new HashMap();
        map.put("appId",baseSysMenu.getAppId());
        List<BaseSysMenu> baseSysMenus= this.getBaseDao().queryList(map);
        return baseSysMenus;
    }


}



