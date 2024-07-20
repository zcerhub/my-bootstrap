package com.base.sys.org.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.SysRoleDto;
import com.base.sys.api.entity.BaseSysRole;

import com.base.sys.org.dao.BaseSysRoleDao;
import com.base.sys.org.service.BaseSysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BaseSysRoleServiceImpl extends AbstractBaseServiceImpl<BaseSysRole, String> implements BaseSysRoleService {


    @Resource
    private BaseSysRoleDao baseSysRoleDaoImpl;

    @Override
    public List<BaseSysRole> getSysRoleByNameOrCode(BaseSysRole baseSysRole) throws DaoException {
        List<BaseSysRole> baseSysRoleList ;
        Map map = new HashMap();
        map.put("roleCode", baseSysRole.getCode());
        map.put("name", baseSysRole.getName());
        baseSysRoleList = this.getBaseDao().queryList(map);
        return baseSysRoleList;
    }


    @Override
    public List<SysRoleDto> getAllRole() throws DaoException {
        //最后的结果
        List<SysRoleDto> roleDtoList = new ArrayList<>();
        List<SysRoleDto> roleDtos = new ArrayList<>();
        List<BaseSysRole> baseSysRoleList = this.getBaseDao().queryList(new HashMap());
        if (CollectionUtils.isNotEmpty(baseSysRoleList)) {
            for (BaseSysRole baseSysRole : baseSysRoleList) {
                SysRoleDto sysRoleDto = new SysRoleDto();
                BeanUtils.copyProperties(baseSysRole, sysRoleDto);
                sysRoleDto.setRoleId(baseSysRole.getId());
                roleDtos.add(sysRoleDto);
            }
            //整理对象  roleDtos
            for (int i = 0; i < roleDtos.size(); i++) {
                // 一级菜单没有parentId
                if (StringUtils.isEmpty(roleDtos.get(i).getParentId())) {
                    roleDtoList.add(roleDtos.get(i));
                }
            }
            for (SysRoleDto roleDto : roleDtoList) {
                roleDto.setChildren(getChild(roleDto.getRoleId(), roleDtos));
            }
        }
        return roleDtoList;
    }


    public List<SysRoleDto> getChild(String id, List<SysRoleDto> allRole) {

        List<SysRoleDto> childList = new ArrayList<>();
        for (SysRoleDto role : allRole) {

            if (!StringUtils.isEmpty(role.getParentId())) {
                if (role.getParentId().equals(id)) {
                    childList.add(role);
                }
            }
        }

        // 按照roleSort升序
        Collections.sort(childList, new Comparator<SysRoleDto>() {
            @Override
            public int compare(SysRoleDto o1, SysRoleDto o2) {
                //升序
                return o1.getRoleSort().compareTo(o2.getRoleSort());
            }
        });

        for (SysRoleDto roleDto : childList) {
            if (!StringUtils.isEmpty(roleDto.getParentId())) {
                // 递归
                roleDto.setChildren(getChild(roleDto.getRoleId(), allRole));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;

    }

    @Override
    public List<BaseSysRole> getRolesByParentId(String parentId) throws DaoException {
        List<BaseSysRole> ruleList ;
        BaseSysRole baseSysRole = new BaseSysRole();
        baseSysRole.setParentId(parentId);
        ruleList = baseSysRoleDaoImpl.queryListByAttribute(baseSysRole);
        return ruleList;
    }

    @Override
    public void updateRoleSort(JSONObject input) throws ServiceException, DaoException {
        if (!StringUtils.isEmpty(input)) {
            String id = (String) input.get("id");
            String targetId = (String) input.get("targetId");
            //          center   bottom   top
            String position = (String) input.get("position");

            //被挪动的角色对象
            BaseSysRole selfRole = (BaseSysRole) this.getBaseDao().getObjectById(id);

            //目标对象
            BaseSysRole targetRole = (BaseSysRole) this.getBaseDao().getObjectById(targetId);


            Integer oldRoleSort = selfRole.getRoleSort();

            Integer targetRoleSort = targetRole.getRoleSort();

            String oldParentId = selfRole.getParentId();

            String newParentId = targetRole.getParentId();

            if (StringUtils.isEmpty(oldParentId)) {
                oldParentId = "";
            }
            if (StringUtils.isEmpty(newParentId)) {
                newParentId = "";
            }


            //在目标节点的里面
            if ("center".equals(position)) {
                //老节点的排序
                List<BaseSysRole> oldRoleList = this.getRolesByParentId(selfRole.getParentId());
                if (CollectionUtils.isNotEmpty(oldRoleList)) {
                    for (BaseSysRole role : oldRoleList) {
                        if (role.getRoleSort() > selfRole.getRoleSort()) {
                            role.setRoleSort(role.getRoleSort() - 1);
                            this.getBaseDao().updateObject(role);
                        }
                    }
                }


                Map map = new HashMap();
                map.put("parent", targetRole.getId());
                Integer count = this.getBaseDao().selectTotal(map);
                //修改被移动节点的parentId和新的排序
                selfRole.setParentId(targetId);
                selfRole.setRoleSort(count + 1);
                this.getBaseDao().updateObject(selfRole);
            } else if ("top".equals(position)) {
                //在目标节点的上面
                if (oldParentId .equals(newParentId) ) {
                    //修改同级别其他的排序
                    List<BaseSysRole> roleList = this.getRolesByParentId(newParentId);
                    //目标节点在老节点的上面
                    if (oldRoleSort > targetRoleSort) {
                        for (BaseSysRole sysRole : roleList) {
                            if (sysRole.getRoleSort() < oldRoleSort && sysRole.getRoleSort() >= targetRoleSort) {
                                //将目标节点及后面的都加1
                                sysRole.setRoleSort(sysRole.getRoleSort() + 1);
                                this.getBaseDao().updateObject(sysRole);
                            }
                        }
                        //同级别的排序  此时，被挪动的节点在目标节点的前面
                        selfRole.setRoleSort(targetRoleSort);
                        this.getBaseDao().updateObject(selfRole);

                    } else {
                        for (BaseSysRole sysRole : roleList) {
                            if (sysRole.getRoleSort() > oldRoleSort && sysRole.getRoleSort() < targetRoleSort) {
                                //将目标节点及后面的都加1
                                sysRole.setRoleSort(sysRole.getRoleSort() - 1);
                                this.getBaseDao().updateObject(sysRole);
                            }
                        }
                        //同级别的排序  此时，被挪动的节点在目标节点的前面
                        selfRole.setRoleSort(targetRoleSort - 1);
                        this.getBaseDao().updateObject(selfRole);
                    }


                } else {
                    //跨节点的排序  分别修改新节点和老节点的排序

                    //新节点的排序
                    List<BaseSysRole> roleList = this.getRolesByParentId(targetRole.getParentId());
                    for (BaseSysRole sysRole : roleList) {
                        if (sysRole.getRoleSort() >= oldRoleSort) {
                            //将目标节点及后面的都加2
                            sysRole.setRoleSort(sysRole.getRoleSort() + 1);
                            this.getBaseDao().updateObject(sysRole);
                        }
                    }

                    //老节点的排序
                    List<BaseSysRole> oldRoleList = this.getRolesByParentId(selfRole.getParentId());
                    if (CollectionUtils.isNotEmpty(oldRoleList)) {
                        for (BaseSysRole role : oldRoleList) {
                            if (role.getRoleSort() > selfRole.getRoleSort()) {
                                role.setRoleSort(role.getRoleSort() - 1);
                                this.getBaseDao().updateObject(role);
                            }
                        }
                    }

                    //修改被移动节点的排序
                    selfRole.setParentId(newParentId);
                    selfRole.setRoleSort(targetRoleSort);
                    this.getBaseDao().updateObject(selfRole);

                }
            } else {
                //在目标节点的下面
                if (oldParentId.equals(newParentId) ) {
                    //修改同级别其他的排序
                    List<BaseSysRole> roleList = this.getRolesByParentId(newParentId);
                    //目标节点在老节点的上面
                    if (oldRoleSort > targetRoleSort) {
                        for (BaseSysRole sysRole : roleList) {
                            if (sysRole.getRoleSort() < oldRoleSort && sysRole.getRoleSort() > targetRoleSort) {
                                //将目标节点及后面的都加1
                                sysRole.setRoleSort(sysRole.getRoleSort() + 1);
                                this.getBaseDao().updateObject(sysRole);
                            }
                        }
                        //同级别的排序  此时，被挪动的节点在目标节点的前面
                        selfRole.setRoleSort(targetRoleSort - 1);
                        this.getBaseDao().updateObject(selfRole);

                    } else {
                            for (BaseSysRole sysRole : roleList) {
                                if (sysRole.getRoleSort() >oldRoleSort && sysRole.getRoleSort() <= targetRoleSort) {
                                    //将目标节点及后面的都加1
                                    sysRole.setRoleSort(sysRole.getRoleSort() - 1);
                                    this.getBaseDao().updateObject(sysRole);
                                }
                            }
                            //同级别的排序  此时，被挪动的节点在目标节点的
                            selfRole.setRoleSort(targetRoleSort);
                            this.getBaseDao().updateObject(selfRole);

                    }

                }else{
                    //跨节点的排序  分别修改新节点和老节点的排序

                    //新节点的排序
                    List<BaseSysRole> roleList = this.getRolesByParentId(targetRole.getParentId());
                    for (BaseSysRole sysRole : roleList) {
                        if (sysRole.getRoleSort() >= oldRoleSort) {
                            //将目标节点及后面的都加1
                            sysRole.setRoleSort(sysRole.getRoleSort() + 1);
                            this.getBaseDao().updateObject(sysRole);
                        }
                    }

                    //老节点的排序
                    List<BaseSysRole> oldRoleList = this.getRolesByParentId(selfRole.getParentId());
                    if (CollectionUtils.isNotEmpty(oldRoleList)) {
                        for (BaseSysRole role : oldRoleList) {
                            if (role.getRoleSort() > selfRole.getRoleSort()) {
                                role.setRoleSort(role.getRoleSort() - 1);
                                this.getBaseDao().updateObject(role);
                            }
                        }
                    }

                    //跨节点排序
                    selfRole.setParentId(newParentId);
                    selfRole.setRoleSort(targetRoleSort + 1);
                    this.getBaseDao().updateObject(selfRole);
                }
            }

        }

    }


    @Override
    public Integer getRoleCountByParentId(String parentId) throws DaoException {
        Map map = new HashMap();
        map.put("parentId", parentId);
        return this.getBaseDao().selectTotal(map);
    }





}











