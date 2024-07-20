package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.service.ApplicationService;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织信息管理
 */
@Api(tags = "基础信息-组织信息管理")
@RestController
@RequestMapping("/v1/basic/org")
public class OrganizationController {
    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private MachineRoomService machineRoomService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Organization organization) {
        try {
            List<Organization> organizationList = organizationService.queryList(new HashMap());
            for(Organization o:organizationList){
                if(o.getOrganizationCode().equals(organization.getOrganizationCode()) || o.getOrganizationName().equals(organization.getOrganizationName())){
                    return ResultUtils.error(ResultEnum.FAILED.getCode(),"已存在");
                }
            }
            organizationService.saveObject(organization);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            Organization organization = organizationService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, organization);
        } catch (ServiceException e) {
            log.error("查询组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
    @ApiOperation("查询所有的组织")
    @PostMapping("/queryList")
    public Result queryList() {
        Map param = new HashMap<>();
        try {
            List<Organization> organizations = organizationService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, organizations);
        } catch (ServiceException e) {
            log.error("查询组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<Organization> param) {
        try {
            Page organizations = organizationService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
            return ResultUtils.success(ResultEnum.SUCCESS, organizations);
        } catch (ServiceException e) {
            log.error("分页查询组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody Organization organization) {
        try {
            List<Organization> organizationList = organizationService.queryList(new HashMap());
            for(Organization o:organizationList){
                if((!o.getId().equals(organization.getId())) && (o.getOrganizationCode().equals(organization.getOrganizationCode()) || o.getOrganizationName().equals(organization.getOrganizationName()))){
                    return ResultUtils.error(ResultEnum.FAILED.getCode(),"已存在");
                }
            }
            organizationService.updateObject(organization);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            List<MachineRoom> machineRooms = machineRoomService.queryList(new HashMap());
            Map map = new HashMap();
            map.put("organizationId",id);
            List<Application> applications = applicationService.queryList(map);
            List<MachineRoom> MachineRooms= machineRoomService.queryList(map);
            if(!applications.isEmpty()){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"有应用在使用该组织，不能删除");
            }
            if(!MachineRooms.isEmpty()){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"有机房在使用该组织，不能删除");
            }
            organizationService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            organizationService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
