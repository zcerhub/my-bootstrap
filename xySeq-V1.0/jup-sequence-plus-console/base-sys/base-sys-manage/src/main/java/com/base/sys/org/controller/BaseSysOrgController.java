package com.base.sys.org.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.dto.TreeDto;
import com.base.sys.api.entity.BaseSysOrg;
import com.base.sys.org.service.BaseSysOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构(BaseSysOrg)表控制层
 *
 * @author makejava
 * @since 2021-01-14 09:32:29
 */
@RestController
@RequestMapping("baseSysOrg/")
public class BaseSysOrgController  {

    private static final Logger log= LoggerFactory.getLogger(BaseSysOrgController.class);

    
    /**
     * 服务对象
     */
    @Resource
    private BaseSysOrgService baseSysOrgService;

    /**
     * 分页查询所有数据
     * @return 所有数据
     */
    @GetMapping("queryPage")
    public Result queryPage(@RequestBody PageInDto<BaseSysOrg> baseSysOrg) {
        log.info("BaseSysOrgController.selectAll() param: page "+baseSysOrg.toString());
        Page page=null;
        try {
            page= baseSysOrgService.queryPage(baseSysOrg.getPageNo(),baseSysOrg.getPageSize(), baseSysOrg.getRequestObject());
        } catch (ServiceException e) {
            log.error("分页查询失败",e.toString());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(page.toString());
        return ResultUtils.success(ResultEnum.SUCCESS,page);
    }

    @GetMapping("selectCompany")
    public Result selectCompany() {
        List<BaseSysOrg>  sysOrgs=null;
        Map<String,String> map=new HashMap<>();
        try {
            map.put("orgType","0");
            map.put("isDelete","0");
            sysOrgs = baseSysOrgService.queryList(map);
        } catch (ServiceException e) {
            log.error("分页查询失败",e.toString());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(sysOrgs.toString());
        return ResultUtils.success(ResultEnum.SUCCESS,sysOrgs);
    }

    /**
     * 分页所有数据
     * @return 所有数据
     */
    @GetMapping("selectOrgTree")
    public Result selectOrgTree(BaseSysOrg baseSysOrg) {
        log.info("BaseSysOrgController.selectOrgTree() baseSysOrg: baseSysOrg "+baseSysOrg.toString());
        List<TreeDto> treeDtos=null;
        try {
            treeDtos=baseSysOrgService.queryTree(baseSysOrg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询数据失败",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info("result : "+treeDtos.toString());
        return ResultUtils.success(ResultEnum.SUCCESS,treeDtos);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id){
    log.info("BaseSysOrgController.selectOne() param: id:"+id);
    BaseSysOrg org=null;
        try {
        org = baseSysOrgService.getObjectById(id);
    } catch (ServiceException e) {
            e.printStackTrace();
        log.error("查询失败",e.getStackTrace());
        return ResultUtils.error(ResultEnum.FAILED);
    }
        return ResultUtils.success(ResultEnum.SUCCESS,org);
    }

    /**
     * 新增数据
     *
     * @param baseSysOrg 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Result insert(@RequestBody BaseSysOrg baseSysOrg) {
        log.info("BaseSysOrgController.insert() param: baseSysOrg :"+baseSysOrg.toString());
        try {
            //根据父id，获取所在层级的count数，来进行sort 排序
            HashMap<String, Object> map = new HashMap<>();
            String parentId=baseSysOrg.getParentId();
            if(StringUtils.isEmpty(baseSysOrg.getParentId())){
                parentId=null;

            }
            map.put("parentId",parentId);
            //获取count 进行++，确定sort
            Integer count = baseSysOrgService.selectTotal(map);
            String code = null;
            baseSysOrg.setOrgCode(code);
            baseSysOrg.setOrgSort(count+1);
            baseSysOrg.setIsDelete("0");
            baseSysOrg.setParentId(parentId);
            baseSysOrgService.saveObject(baseSysOrg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增失败",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 修改数据
     *    注意一点 ，如果机构有子菜单，那么在修改的时候
     * @param baseSysOrg 实体对象
     * @return 修改结果
     */
    @PutMapping("update")
    public Result update( @RequestBody BaseSysOrg baseSysOrg) {
        log.info("BaseSysOrgController.update() param: baseSysOrg :"+baseSysOrg.toString());
        try {
            baseSysOrgService.updateObjectP(baseSysOrg);
        } catch (Exception e) {
            log.error("修改失败",e.getStackTrace());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * {"parentId":null,"preId":null,"orgId":"183898940318416896"}
     * 同层级 上下切换排序
     * @return
     */
    @PutMapping("updateSort")
    public Result updateSort(@RequestBody JSONObject input) {
        log.info("BaseSysOrgController.updateSort() param: baseSysOrg :"+input.toString());
        try {
            baseSysOrgService.updateObjectPs(input);
        } catch (Exception e) {
            log.error("修改失败",e.getStackTrace());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 删除数据
     *  组织机构菜单是不允许真正删除的，所以只能假删除，调用修改接口
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public Result deleteAll(@RequestParam("idList") List<String> idList) {
        log.info("BaseSysOrgController.delete() param: deleteAll "+idList.toString());
        try {
            baseSysOrgService.delObjectByIds(idList);
        } catch (ServiceException e) {
            log.error("删除失败",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * isDelete 置为1，查询时过滤
     * @return
     */
    @DeleteMapping("deleteAll")
    public Result deleteOne(@RequestBody String input) {
        log.info("BaseSysOrgController.deleteAll() param: baseSysOrg "+ input);
        try {
            JSONObject input1 = JSON.parseObject(input);
            List<String> idList = (List) input1.get("idList");
            for (int i = 0; i < idList.size(); i++) {
                BaseSysOrg baseSysOrg = new BaseSysOrg();
                baseSysOrg.setId(idList.get(i));
                baseSysOrgService.delOne(baseSysOrg);
            }
        } catch (Exception e) {
            log.error("删除失败",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    @GetMapping("test")
    public Result test(){
        log.info("BaseSysOrgController.selectOne() param: id:");
        Integer integer=0;
        try {
             integer = baseSysOrgService.selectTotal(new HashMap<>());
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("查询失败",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,integer);
    }
}
