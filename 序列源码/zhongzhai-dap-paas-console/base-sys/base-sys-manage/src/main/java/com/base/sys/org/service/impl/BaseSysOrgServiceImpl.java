package com.base.sys.org.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.TreeDto;
import com.base.sys.api.entity.BaseSysOrg;
import com.base.sys.org.service.BaseSysOrgService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 组织机构(BaseSysOrg)表服务实现类
 *
 * @author makejava
 * @since 2021-01-14 09:32:28
 */
@Service("baseSysOrgService")
public class BaseSysOrgServiceImpl extends AbstractBaseServiceImpl<BaseSysOrg,String> implements BaseSysOrgService {
    /**
     * 只更改层级
     * @param input
     */
    @Override
    public void updateObjectPs(JSONObject input)throws DaoException{
        if(!StringUtils.isEmpty(input)){
            String id = (String)input.get("id");
            String targetId = (String)input.get("targetId");
//            center  bottom top
            String position = (String)input.get("position");
            BaseSysOrg oldOrg =  this.getBaseDao().getObjectById(id);
            BaseSysOrg tarOrg =  this.getBaseDao().getObjectById(targetId);

            HashMap<String, Object> map = new HashMap<>();
//            获取 preid之下的所有子节点，进行重新排序
            Integer tOrgSort = tarOrg.getOrgSort();
            String tParentId = tarOrg.getParentId();
            String olParentId = oldOrg.getParentId();
            Integer olOrgSort = oldOrg.getOrgSort();
//            对parentId 进行统一赋值 为null;
            if(StringUtils.isEmpty(tParentId)){ tParentId=null;
            }
               if(StringUtils.isEmpty(olParentId)){ olParentId=null;
            }
            oldOrg.setParentId(olParentId);
            tarOrg.setParentId(tParentId);

            map.put("parentId",tParentId);
            map.put("orgSort",tOrgSort);
            Set<BaseSysOrg> updateOrgs=new HashSet<>();
            List<BaseSysOrg> listTar = this.getBaseDao().queryList(map);

            map.put("parentId",targetId);
            Integer count = this.getBaseDao().selectTotal(map);

            map.put("parentId",olParentId);
            map.put("orgSort",olOrgSort);
            List<BaseSysOrg> listOld = this.getBaseDao().queryList(map);
//            目标节点的里面 ，则只用修改parentId 既可以
            if("center".equals(position)){
                oldOrg.setParentId(tarOrg.getId());
//                查看目标节点的子节点数目，对老数据进行排序
                int i = count + 1;
                oldOrg.setOrgSort(i);
//                必定存在跨级 ，修改老数据之后的sort
                for (BaseSysOrg olorg:listOld
                ) {
                    olorg.setOrgSort(olOrgSort++);
                    updateOrgs.add(olorg);
                }
                updateOrgs.add(oldOrg);
            }else if("top".equals(position)){
                //同级别处理
                if(tParentId.equals(olParentId)){
                    if(olOrgSort<tOrgSort){
//                    如果是老数据的序号大于目标序号，那就只需改老数据和到目标数据之间的排序
                        oldSortDown(tOrgSort, olOrgSort, updateOrgs, listOld);
                        int i = tOrgSort - 1;
                        oldOrg.setOrgSort(i);
                    }else{
                        oldSortUp(tOrgSort, olOrgSort, updateOrgs, listTar);
                        oldOrg.setOrgSort(tOrgSort);
                        int i = tOrgSort + 1;
                        tarOrg.setOrgSort(i);
                    }
                }else{
                    oldOrg.setOrgSort(tOrgSort);
                    updateNotLevel(oldOrg, tarOrg, tOrgSort, tParentId, olOrgSort, updateOrgs, listTar, listOld);
                }
                updateOrgs.add(oldOrg);
                updateOrgs.add(tarOrg);
            }else{
                //同级之间的移动 bottom
                if(tParentId.equals(olParentId)){
                    if(olOrgSort<tOrgSort){
                        oldSortDown(tOrgSort, olOrgSort, updateOrgs, listOld);
                        oldOrg.setOrgSort(tOrgSort);
                        int i = tOrgSort - 1;
                        tarOrg.setOrgSort(i);
                    }else {
                        oldSortUp(tOrgSort, olOrgSort, updateOrgs, listTar);
                        int i = tOrgSort + 1;
                        oldOrg.setOrgSort(i);
                    }
                    updateOrgs.add(oldOrg);
                    updateOrgs.add(tarOrg);
                }else{
                    updateNotLevel(oldOrg, oldOrg, tOrgSort, tParentId, olOrgSort, updateOrgs, listTar, listOld);
                    updateOrgs.add(oldOrg);
                }
            }
            for ( BaseSysOrg upDateOrg:
            updateOrgs ) {
                this.getBaseDao().updateObject(upDateOrg);
            }
        }

    }
//修改跨级别的数据
    private void updateNotLevel(BaseSysOrg oldOrg, BaseSysOrg tarOrg, Integer tOrgSort, String tParentId, Integer olOrgSort, Set<BaseSysOrg> updateOrgs, List<BaseSysOrg> listTar, List<BaseSysOrg> listOld) {
        tOrgSort = tOrgSort + 1;
        tarOrg.setOrgSort(tOrgSort);
//     跨级别了
        //目标列数据排序
        oldOrg.setParentId(tParentId);
        for (BaseSysOrg torg : listTar
        ) {
            tOrgSort = tOrgSort + 1;
            torg.setOrgSort(tOrgSort);
            updateOrgs.add(torg);
        }
//                    老数据列排序
        for (BaseSysOrg olorg : listOld
        ) {
            olorg.setOrgSort(olOrgSort++);
            updateOrgs.add(olorg);
        }
    }

    //同级别 上移
    private void oldSortUp(Integer tOrgSort, Integer olOrgSort, Set<BaseSysOrg> updateOrgs, List<BaseSysOrg> listTar) {
        for (BaseSysOrg torg : listTar
        ) {
            if ((torg.getOrgSort() > tOrgSort) && (torg.getOrgSort() < olOrgSort)) {
                int i = torg.getOrgSort() + 1;
                torg.setOrgSort(i);
                updateOrgs.add(torg);
            }
        }
    }
//同级别 下移
    private void oldSortDown(Integer tOrgSort, Integer olOrgSort, Set<BaseSysOrg> updateOrgs, List<BaseSysOrg> listOld) {
        for (BaseSysOrg torg : listOld
        ) {
            if ((torg.getOrgSort() > olOrgSort) && (torg.getOrgSort() < tOrgSort)) {
                int i = torg.getOrgSort() - 1;
                torg.setOrgSort(i);
                updateOrgs.add(torg);
            }
        }
    }
    @Override
    public void updateObjectP(BaseSysOrg baseSysOrg) throws DaoException{
        if(null!=baseSysOrg){
            HashMap<String, String> map = new HashMap<>();
            map.put("parentId",baseSysOrg.getParentId());
            //获取count 进行++，确定sort
            Integer count = this.getBaseDao().selectTotal(map);
            //和老数据对比看父id是否发生改变，未改，则进行跟新，改变了，则根系sort
            BaseSysOrg oldOrg = this.getBaseDao().getObjectById(baseSysOrg.getId());
             //父节点不变，就只更新数据
            String oldParentId = oldOrg.getParentId();
            if(StringUtils.isEmpty(oldParentId)){
                oldParentId=null;
            }
            String baseParentId = baseSysOrg.getParentId();
            if(StringUtils.isEmpty(baseParentId)){
                baseParentId=null;
            }
//            重新赋值为 null
            baseSysOrg.setParentId(baseParentId);
            if(baseParentId==null||baseParentId==oldParentId||baseParentId.equals(oldParentId)){
                this.getBaseDao().updateObject(baseSysOrg);
            }else{
//                父节点不一样 就需要进行重新排序
                int i = count + 1;
                baseSysOrg.setOrgSort(i);
                //同时，需要修改 老数据的sort后面的排序 都得加一
                HashMap<String, Object> map2 = new HashMap<>();
//             获取 preid之下的所有子节点，进行重新排序
                String parentId = oldOrg.getParentId();
                Integer orgSort = oldOrg.getOrgSort();
                map2.put("parentId",parentId);
                map2.put("orgSort",orgSort);
                List<BaseSysOrg> list = this.getBaseDao().queryList(map);
                list.add(baseSysOrg);
                for (int j = 0; j < list.size(); j++) {
                    BaseSysOrg baseSysOrg2 = list.get(j);
                    orgSort= orgSort + 1;
                    baseSysOrg2.setOrgSort(orgSort);
                    this.getBaseDao().updateObject(baseSysOrg2);
                }
            }
        }
    }

    @Override
    public Integer delOne(BaseSysOrg baseSysOrg)throws DaoException{
        //判断如果机构层级leve选择是1，则父id 赋值为0
        if(null!=baseSysOrg){
            baseSysOrg.setIsDelete("1");
        }
        return this.getBaseDao().updateObject(baseSysOrg);
    }

    @Override
    public List<TreeDto> queryTree(BaseSysOrg baseSysOrg) throws DaoException{
        // orgType
        BaseSysOrg allSysOrgs =new BaseSysOrg();
        allSysOrgs.setIsDelete("0");
        List<BaseSysOrg> orglist = this.getBaseDao().queryListByPo(allSysOrgs);
        List<TreeDto> dtos = new ArrayList<>();
        List<TreeDto> dorgList = new ArrayList<>();
//        根据id 查询指定父节点的 tree数据,不包含子节点
        if(!StringUtils.isEmpty(baseSysOrg.getId())){
            BaseSysOrg baseSysOrg1 = new BaseSysOrg();
            baseSysOrg1.setIsDelete("0");
            baseSysOrg1.setParentId(baseSysOrg.getId());
            List<BaseSysOrg> list = this.getBaseDao().queryListByPo(baseSysOrg1);
            list.forEach(org->{
                TreeDto dto = new TreeDto();
                BeanUtils.copyProperties(org, dto);
                dto.setChildren(getChild(dto.getId(),orglist));
                dorgList.add(dto);
            });
        }else{
            for (int i = 0; i < orglist.size(); i++) {
                TreeDto treeDto = new TreeDto();
//            对所以的组织机构进行排序
                if (StringUtils.isEmpty(orglist.get(i).getParentId())) {
                    BeanUtils.copyProperties(orglist.get(i), treeDto);
                    dtos.add(treeDto);
                }
            }
            for (int i = 0; i < dtos.size(); i++) {
                // 一级菜单没有parentId
                TreeDto dto = dtos.get(i);
                dto.setChildren(getChild(dto.getId(),orglist));
                dorgList.add(dto);
            }
        }

        return dorgList;
    }

    public List<TreeDto> getChild(String id, List<BaseSysOrg> orglist){
        // 子菜单
        List<TreeDto> childList = new ArrayList<>();
        for (BaseSysOrg org : orglist) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if(!StringUtils.isEmpty(org.getParentId())&&id.equals(org.getParentId())){
                TreeDto treeDto = new TreeDto();
                BeanUtils.copyProperties(org, treeDto);
                treeDto.setChildren(getChild(treeDto.getId(),  orglist));
                childList.add(treeDto);
            }
        }
       return childList;
    }

}
