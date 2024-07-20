package com.base.sys.menu.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.async.aspect.AsynSelectMenuAnnotation;
import com.base.sys.menu.service.BaseSysOperateService;
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
@RequestMapping("/baseSysOperateEntity")
public class BaseSysOperateController {

    private static final Logger log= LoggerFactory.getLogger(
            BaseSysOperateController.class);

    @Resource
    private BaseSysOperateService baseSysOperateService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id")String id){
        BaseSysOperate baseSysOperateEntity=null;
        try {
            baseSysOperateEntity = baseSysOperateService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysOperateEntity);
    }



    /**
     * 通过menuId查询数据
     *
     * @param baseSysMenu
     * @return 单条数据
     */
    @GetMapping("/selectByMenuId")
    public Result selectByMenuId(BaseSysMenu baseSysMenu){
        List<BaseSysOperate> baseSysOperateList=new ArrayList<>();
        try {
            String menuId="";
            if(null !=baseSysMenu ){
                menuId= baseSysMenu.getId();
            }
            if(StringUtils.isEmpty(menuId)){
                return ResultUtils.success(ResultEnum.SUCCESS,null);
            }
            baseSysOperateList = baseSysOperateService.getOperateByMenuId(menuId);
        } catch (ServiceException | DaoException e) {
            log.error("查询失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysOperateList);
    }

    /**
     * 新增数据
     *
     * @param baseSysOperateEntity 实体对象
     * @return 新增结果
     */
    @AsynSelectMenuAnnotation(title = "操作点",action = "新增")
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysOperate baseSysOperateEntity) {
        try {
          if(1==baseSysOperateService.saveObject(baseSysOperateEntity)){
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
     * @param baseSysOperateEntity 实体对象
     * @return 修改结果
     */
    @AsynSelectMenuAnnotation(title = "操作点",action = "更新")
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysOperate baseSysOperateEntity) {
        try {
            if(baseSysOperateService.updateObject(baseSysOperateEntity)==1){
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
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
    @AsynSelectMenuAnnotation(title = "操作点",action = "删除")
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable("id")String id) {
        try {
            baseSysOperateService.delObjectById(id);
        } catch (ServiceException e) {
            log.error("删除失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }


    /**
     * 批量删除
     * @param json
     * @return
     */
    @DeleteMapping("/deleteAll")
    public Result deleteAll(@RequestBody JSONObject json) {
        List<String> ids = (List<String>) json.get("idList");
        try {
            if(ids.size()== baseSysOperateService.delObjectByIds(ids)){
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (ServiceException e) {
            log.error("删除失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }



}

