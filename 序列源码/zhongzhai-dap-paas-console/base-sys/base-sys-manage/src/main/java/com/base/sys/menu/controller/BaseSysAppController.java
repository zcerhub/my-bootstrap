package com.base.sys.menu.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.async.aspect.AsynSelectMenuAnnotation;
import com.base.sys.menu.service.BaseSysAppService;
import com.base.sys.menu.service.BaseSysMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("baseSysAppEntity")
public class BaseSysAppController {

    private static final Logger log= LoggerFactory.getLogger(
            BaseSysAppController.class);


    @Resource
    private BaseSysAppService baseSysAppService;

    @Resource
    private BaseSysMenuService baseSysMenuService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id){
        BaseSysApp baseSysApp=null;
        try {
            baseSysApp = baseSysAppService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysApp);
    }




    /**
     * 新增数据
     *
     * @param baseSysApp 实体对象
     * @return 新增结果
     */
    @AsynSelectMenuAnnotation(title = "应用",action = "应用")
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysApp baseSysApp) {
        try {
            // 查询所有应用
            List<BaseSysApp>  list= baseSysAppService.queryList(new BaseSysApp());
            baseSysApp.setSort(list.size()+1);
            if(1==baseSysAppService.saveObject(baseSysApp)){
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
     * @param baseSysApp 实体对象
     * @return 修改结果
     */
    @AsynSelectMenuAnnotation(title = "应用",action = "更新")
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysApp baseSysApp) {
        try {
            if(1==baseSysAppService.updateObject(baseSysApp)){
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
            baseSysAppService.updateObject(baseSysApp);
        } catch (ServiceException e) {
            log.error("修改失败",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }



    @AsynSelectMenuAnnotation(title = "应用",action = "删除")
    @DeleteMapping("/delete")
    public Result delete(@RequestBody JSONObject json) {
        List<String> ids = (List<String>) json.get("idList");

        //查询菜单表
        try {
            //加校验，应用下面如果有菜单，不能删除,ids只有一条数据
            BaseSysMenu baseSysMenu = new BaseSysMenu();
            baseSysMenu.setAppId(ids.get(0));
            List<BaseSysMenu> baseSysMenus=  baseSysMenuService.getAllBaseSysMenusByAppId(baseSysMenu);
            if(!CollectionUtils.isEmpty(baseSysMenus)){
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "该应用下有菜单，不能删除");
            }
            baseSysAppService.delObjectByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败：",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }



    /**
     * 分页查询
     * @param pages
     * @return
     */
    @PostMapping("queryPage")
    public Result queryPage(@RequestBody PageInDto<BaseSysApp> pages) {
        Page page=null;
        try {
            BaseSysApp requestObject = pages.getRequestObject();
            if(StringUtils.isEmpty(requestObject)){
                requestObject=new BaseSysApp();
            }
            page = baseSysAppService.queryPage(pages.getPageNo(), pages.getPageSize(),requestObject);
        } catch (ServiceException e) {
            log.error("查询所有结果失败：",e.getStackTrace());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,page);
    }



    @GetMapping("selectAll")
    public Result selectAll(BaseSysApp baseSysApp) {

        Map<String,Object> paraMap=new HashMap<>();

        List<BaseSysApp> baseSysApps=null;
        try {
            baseSysApps = baseSysAppService.queryList(paraMap);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("查询所有失败",e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysApps);
    }


    @GetMapping("/paas/instruction/book/{id}")
    public void watchInstructionBook(@PathVariable("id") String id, HttpServletResponse response) {
        baseSysAppService.watchInstructionBookById(id,response);
    }



}
