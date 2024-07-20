package com.base.sys.menu.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.menu.service.BaseSysDataRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
@RestController
@RequestMapping("baseSysDataRuleEntity")
public class BaseSysDataRuleController {

    private static final Logger log= LoggerFactory.getLogger(
            BaseSysDataRuleController.class);

    @Resource
    private BaseSysDataRuleService baseSysDataRuleService;



    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id){
        BaseSysDataRule baseSysDataEntity=null;
        try {
            baseSysDataEntity = baseSysDataRuleService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysDataEntity);
    }


    /**
     *
     * @param
     * @return
     */
    @GetMapping("/selectByMenuId")
    public Result selectByMenuId( BaseSysMenu baseSysMenu){
        List<BaseSysDataRule> sysDataRules = new ArrayList<>();
        try {
            String menuId="";
            if(null !=baseSysMenu ){
                menuId= baseSysMenu.getId();
            }
            if(StringUtils.isEmpty(menuId)){
                return ResultUtils.success(ResultEnum.SUCCESS,null);
            }
            sysDataRules = baseSysDataRuleService.getSysDataListByMenuId(menuId);
        } catch (ServiceException | DaoException e) {
            log.error("查询失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,sysDataRules);
    }

    /**
     * 新增数据
     *
     * @param baseSysData 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysDataRule baseSysData) {
        try {
           if(1==baseSysDataRuleService.saveObject(baseSysData)){
               return ResultUtils.success(ResultEnum.SUCCESS);
           }

        } catch (ServiceException e) {
            log.error("新增失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }

    /**
     * 修改数据
     *
     * @param baseSysDataEntity 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysDataRule baseSysDataEntity) {
        try {
            if(1==baseSysDataRuleService.updateObject(baseSysDataEntity)){
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
            baseSysDataRuleService.updateObject(baseSysDataEntity);
        } catch (ServiceException e) {
            log.error("修改失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable("id")String id) {
        try {
           if(1== baseSysDataRuleService.delObjectById(id)){
               return ResultUtils.success(ResultEnum.SUCCESS);
           }
        } catch (ServiceException e) {
            log.error("删除失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }


    /**
     * 批量删除
     * @param
     * @return
     */
    @DeleteMapping("/deleteAll")
    public Result deleteAll( @RequestBody JSONObject json) {
        List<String> ids = (List<String>) json.get("idList");

        try {
            if(ids.size()== baseSysDataRuleService.delObjectByIds(ids)){
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (ServiceException e) {
            log.error("删除失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }

}

