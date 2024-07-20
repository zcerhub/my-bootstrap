package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.ContainerImage;
import com.dap.paas.console.basic.service.ContainerImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 镜像信息管理
 */
@Api(tags = "基础信息-镜像信息管理")
@RestController
@RequestMapping("/v1/basic/containerImage")
public class ContainerImageController {

    private static final Logger log = LoggerFactory.getLogger(ContainerImageController.class);
    @Autowired
    private ContainerImageService containerImageService;

    @PostMapping("/insert")
    public Result insert(@RequestBody ContainerImage containerImage) {
        try {
            List<ContainerImage> containerImageList = containerImageService.queryList(new ContainerImage());
            for (ContainerImage c : containerImageList) {
                if (c.getImageName().equals(containerImage.getImageName())) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            containerImageService.saveObject(containerImage);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            ContainerImage containerImage = containerImageService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, containerImage);
        } catch (ServiceException e) {
            log.error("查询镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("查询所有的镜像")
    @GetMapping("/queryList/{imageType}")
    public Result queryList(@PathVariable("imageType") String imageType) {
        Map param = new HashMap<>();
        param.put("imageType", imageType);
        try {
            List<ContainerImage> containerImageList = containerImageService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, containerImageList);
        } catch (ServiceException e) {
            log.error("查询镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<ContainerImage> param) {
        try {
            Page containerImages = containerImageService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
            return ResultUtils.success(ResultEnum.SUCCESS, containerImages);
        } catch (ServiceException e) {
            log.error("查询分页镜像失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody ContainerImage containerImage) {
        try {
            List<ContainerImage> containerImageList = containerImageService.queryList(new ContainerImage());
            for (ContainerImage c : containerImageList) {
                if ((!c.getId().equals(containerImage.getId())) && (c.getImageName().equals(containerImage.getImageName()))) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            containerImageService.updateObject(containerImage);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            containerImageService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            containerImageService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除镜像失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
