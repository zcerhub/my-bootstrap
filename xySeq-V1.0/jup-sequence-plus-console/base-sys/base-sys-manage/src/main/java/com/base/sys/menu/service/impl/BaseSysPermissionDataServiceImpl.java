package com.base.sys.menu.service.impl;

import com.base.api.exception.DaoException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.PermissionTypeEnum;
import com.base.sys.api.common.SysMenuEnum;
import com.base.sys.api.dto.PermissionShowDto;
import com.base.sys.api.dto.PermissionTreeDto;
import com.base.sys.api.dto.SysDataRuleDto;
import com.base.sys.api.dto.SysMenuDataRuleDto;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysPermissionData;
import com.base.sys.menu.service.BaseSysDataRuleService;
import com.base.sys.menu.service.BaseSysMenuService;
import com.base.sys.menu.service.BaseSysPermissionDataService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BaseSysPermissionDataServiceImpl extends AbstractBaseServiceImpl<BaseSysPermissionData, String> implements BaseSysPermissionDataService {

    @Resource
    private BaseSysDataRuleService baseSysDataRuleService;

    @Resource
    private BaseSysMenuService baseSysMenuService;

    @Override
    public List<SysDataRuleDto> getPermissionDataByRole(BaseSysPermissionData baseSysPermissionData) throws DaoException {
        List<SysDataRuleDto> sysDataRuleDtoList = new ArrayList<>();
        List<BaseSysPermissionData> list = new ArrayList<>();
        Map map = new HashMap<>();
        if(!StringUtils.isEmpty(baseSysPermissionData.getRoleId())){
            map.put("roleId", baseSysPermissionData.getRoleId());
           list = this.getBaseDao().queryList(map);
        }
        if (CollectionUtils.isNotEmpty(list)){
           for (BaseSysPermissionData permissionData :list){
               BaseSysDataRule baseSysData = null;
               if (permissionData.getDataRuleId()!=null){
                   baseSysData = baseSysDataRuleService.getObjectById(permissionData.getDataRuleId());
               }
              if(null != baseSysData){
                  SysDataRuleDto sysDataRuleDto =new SysDataRuleDto();
                  BeanUtils.copyProperties(baseSysData,sysDataRuleDto);
                  sysDataRuleDto.setPermissionDataId(permissionData.getId());
                  sysDataRuleDto.setDataRuleId(permissionData.getDataRuleId());
                  sysDataRuleDtoList.add(sysDataRuleDto);
              }

           }
        }
        return sysDataRuleDtoList;
    }

    @Override
    public List<SysMenuDataRuleDto> getPermissionMenuDataByRole(BaseSysPermissionData baseSysPermissionData) throws DaoException {

        //最后的返回数据
        List<SysMenuDataRuleDto> dataList =  new ArrayList<>();

        List<BaseSysDataRule> ruleList = new ArrayList<>();

        Map map = new HashMap<>();
        map.put("roleId", baseSysPermissionData.getRoleId());
        //数据权限对象
        List<BaseSysPermissionData> permissionDataList = this.getBaseDao().queryList(map);

        if(CollectionUtils.isNotEmpty(permissionDataList)){
            for(BaseSysPermissionData sysPermissionData :permissionDataList){
                //根据数据规则id 查询 数据规则
               BaseSysDataRule dataRule =  baseSysDataRuleService.getObjectById(sysPermissionData.getDataRuleId());
                 if(null !=dataRule){
                  ruleList.add(dataRule);
              }
            }

        }

        return dataList;
    }




    @Override
    public PermissionShowDto showDataRulePermissions(String roleId) throws DaoException {

        //最后结果
        List<PermissionTreeDto> allPermissionDataTreeList = new ArrayList<>();

        //查到所有的菜单
        List<BaseSysMenu> baseSysMenus = baseSysMenuService.getAllBaseSysMenus();

        //所有的菜单和数据规则
        List<PermissionTreeDto> allPermissionDataTreeDtos = new ArrayList<>();

        //将叶子节点菜单下的操作点查询出来
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu baseSysMenu : baseSysMenus) {
                PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
                permissionTreeDto.setId(baseSysMenu.getId());
                permissionTreeDto.setType(PermissionTypeEnum.MENU_TYPE.getCode());
                permissionTreeDto.setParentId(baseSysMenu.getParentId());
                permissionTreeDto.setName(baseSysMenu.getText());

                allPermissionDataTreeDtos.add(permissionTreeDto);

                //如果是叶子节点，查询下面的数据规则
                if (SysMenuEnum.LEAF.getCode().equals(baseSysMenu.getIsLeaf())) {
                    List<BaseSysDataRule> baseSysDataRules = baseSysDataRuleService.getSysDataListByMenuId(baseSysMenu.getId());
                    if (CollectionUtils.isNotEmpty(baseSysDataRules)) {
                        for(BaseSysDataRule dataRule :baseSysDataRules){
                            PermissionTreeDto permissionDataTreeDto = new PermissionTreeDto();
                            permissionDataTreeDto.setId(dataRule.getId());
                            permissionDataTreeDto.setName(dataRule.getName());
                            permissionDataTreeDto.setType(PermissionTypeEnum.DATA_RULE_TYPE.getCode());
                            //为了展示树形结构 ，将menuId作为dataRule的父id
                            permissionDataTreeDto.setParentId(baseSysMenu.getId());
                            allPermissionDataTreeDtos.add(permissionDataTreeDto);
                        }
                    }
                }
            }

        }

        //整理数据 allPermissionDataTreeDtos

        for (int i = 0; i < allPermissionDataTreeDtos.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(allPermissionDataTreeDtos.get(i).getParentId())) {
                allPermissionDataTreeList.add(allPermissionDataTreeDtos.get(i));
            }
        }
        for (PermissionTreeDto menu : allPermissionDataTreeList) {
            menu.setChildren(getChildren(menu.getId(), allPermissionDataTreeDtos));
        }


        PermissionShowDto permissionShowDto = new PermissionShowDto();

        permissionShowDto.setDataRuleShow(allPermissionDataTreeList);

        return permissionShowDto;
    }



    @Override
    public List<PermissionTreeDto> showDataPermission(String roleId) throws DaoException {

        List<PermissionTreeDto> permissionTreeDtoList =new ArrayList<>();
        // 查询数据授权信息
        Map map = new HashMap<>();
        map.put("roleId", roleId);
        List<BaseSysPermissionData> sysPermissionDataList = this.getBaseDao().queryList(map);
        if(CollectionUtils.isNotEmpty(sysPermissionDataList)){
              for(BaseSysPermissionData baseSysPermissionData:sysPermissionDataList){
                  String dataRuleId = baseSysPermissionData.getDataRuleId();
                 //查询数据规则
                BaseSysDataRule  dataRule  =  baseSysDataRuleService.getObjectById(dataRuleId);
                if(null != dataRule){
                    PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
                    permissionTreeDto.setDataPermissionId(baseSysPermissionData.getId());
                    permissionTreeDto.setId(dataRule.getId());
                    permissionTreeDto.setParentId(dataRule.getMenuId());
                    permissionTreeDto.setName(dataRule.getName());
                    permissionTreeDto.setType(PermissionTypeEnum.DATA_RULE_TYPE.getCode());
                    permissionTreeDtoList.add(permissionTreeDto);
                }
              }
        }
        return permissionTreeDtoList;
    }


    /**
     * 整理 List<PermissionTreeDto> 数据
     * @param id
     * @param permissionTreeDtos
     * @return
     */
    private List<PermissionTreeDto> getChildren(String id, List<PermissionTreeDto> permissionTreeDtos) {
        // 子菜单
        List<PermissionTreeDto> childList = new ArrayList<>();
        for (PermissionTreeDto permissionTreeDto : permissionTreeDtos) {

            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(permissionTreeDto.getParentId())) {
                if (permissionTreeDto.getParentId().equals(id)) {
                    childList.add(permissionTreeDto);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (PermissionTreeDto permissionTreeDto : childList) {
            if (!StringUtils.isEmpty(permissionTreeDto.getParentId())) {
                // 递归
                permissionTreeDto.setChildren(getChildren(permissionTreeDto.getId(), permissionTreeDtos));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
