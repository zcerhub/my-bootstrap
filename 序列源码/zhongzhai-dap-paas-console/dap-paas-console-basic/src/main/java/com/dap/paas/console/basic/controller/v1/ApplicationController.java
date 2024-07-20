package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.service.ApplicationService;
import com.dap.paas.console.basic.service.OrganizationService;
import com.dap.paas.console.basic.utils.RandomUtil;
import com.dap.paas.console.basic.vo.ApplicationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用管理
 */
@Api(tags = "基础信息-应用管理")
@RestController
@RequestMapping("/v1/basic/app")
public class ApplicationController {
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @PostMapping("/insert")
    public Result insert(@RequestBody Application application) {
        try {
            List<Application> applicationList = applicationService.queryList(new HashMap());
            for (Application a : applicationList) {
                if (application.getApplicationCode().equals(a.getApplicationCode()) || application.getApplicationName()
                        .equals(a.getApplicationName())) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            //后台生成密钥和密钥启动状态（密钥用于应用调用openapi时访问机制校验），默认开启
            application.setAccessKey(RandomUtil.generateSecureAccessKey(10));
            application.setAccessKeyStatus("1");
            applicationService.saveObject(application);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @PostMapping("/syncApplication")
    public Result syncApplication() {
        try {
            applicationService.syncApplication();
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (Exception e) {
            log.error("同步应用失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    
    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            Application application = applicationService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, application);
        } catch (ServiceException e) {
            log.error("查询应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @ApiOperation("查询所有的应用")
    @PostMapping("/queryList")
    public Result queryList() {
        Map param = new HashMap<>();
        try {
            List<Application> applicationList = applicationService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, applicationList);
        } catch (ServiceException e) {
            log.error("查询所有的应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<Application> param) {
        try {
            Page applications = applicationService.queryPage(param.getPageNo(), param.getPageSize(),
                    param.getRequestObject());
            List<Application> applicationList = applications.getData();
            List<ApplicationVo> applicationVos = new ArrayList<>();
            for (Application application : applicationList) {
                ApplicationVo applicationVo = new ApplicationVo(application);
                if (StringUtils.isNotEmpty(application.getOrganizationId())) {
                    Organization organization = organizationService.getObjectById(application.getOrganizationId());
                    if (organization != null) {
                        applicationVo.setOrganizationName(organization.getOrganizationName());
                    }
                }
                applicationVos.add(applicationVo);
            }
            applications.setData(applicationVos);
            applications.setPageSize(param.getPageSize());
            applications.setTotalCount(applications.getTotalCount());
            return ResultUtils.success(ResultEnum.SUCCESS, applications);
        } catch (ServiceException e) {
            log.error("查询分页应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @PutMapping("/update")
    public Result update(@RequestBody Application application) {
        try {
            List<Application> applicationList = applicationService.queryList(new HashMap());
            for (Application a : applicationList) {
                if ((!a.getId().equals(application.getId())) && (
                        application.getApplicationCode().equals(a.getApplicationCode())
                                || application.getApplicationName().equals(a.getApplicationName()))) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            if ("1".equals(application.getAccessKeyStatus()) && StringUtils.isBlank(application.getAccessKey())) {
                application.setAccessKey(RandomUtil.generateSecureAccessKey(10));
            }
            applicationService.updateObject(application);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            applicationService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    
    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            applicationService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除应用失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
