package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.ConfigTemplate;
import com.dap.paas.console.basic.enums.ComponentEnum;
import com.dap.paas.console.basic.enums.ModuleEnum;
import com.dap.paas.console.basic.service.ConfigTemplateService;
import com.dap.paas.console.basic.vo.TemplateDetailVo;
import com.dap.paas.console.basic.vo.TemplateNameIdVo;
import com.dap.paas.console.basic.vo.TypeDescVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "通用配置模板管理")
@RestController
@RequestMapping("/v1/basic/template")
public class ConfigTemplateController {
    @Autowired
    private ConfigTemplateService configTemplateService;

    /**
     * 保存模板
     *
     * @param configTemplate 模板信息
     * @return 模板id
     */
    @ApiOperation(value = "保存模板", notes = "保存模板")
    @PostMapping("/save")
    public Result<String> saveTemplate(@RequestBody @Validated ConfigTemplate configTemplate) {
        configTemplateService.save(configTemplate);
        return ResultUtils.success(configTemplate.getId());
    }

    /**
     * 删除模板
     *
     * @param id 模板id
     */
    @ApiOperation(value = "删除模板", notes = "删除模板")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTemplate(@PathVariable String id) {
        return ResultUtils.success(configTemplateService.delObjectById(id) > 0);
    }

    /**
     * 根据id获取模板
     *
     * @param id 模板id
     * @return 模板信息
     */
    @ApiOperation(value = "根据id获取模板", notes = "根据id获取模板")
    @GetMapping("/{id}")
    public Result<TemplateDetailVo> getTemplateById(@PathVariable String id) {
        return ResultUtils.success(configTemplateService.getConfigTemplateDetail(id));
    }

    /**
     * 查询模板列表
     *
     * @param pageQuery 分页查询条件
     * @return 分页结果
     */
    @ApiOperation(value = "查询模板列表", notes = "查询模板列表")
    @PostMapping("/list")
    public Result<Page<ConfigTemplate>> listTemplatePage(@RequestBody PageInDto<ConfigTemplate> pageQuery) {
        return ResultUtils.success(configTemplateService.listObject(pageQuery));
    }

    /**
     * 查询模板列表
     *
     * @param moduleType    模块类型
     * @param componentType 组件类型
     * @return 分页结果
     */
    @ApiOperation(value = "查询模板列表", notes = "查询模板列表")
    @GetMapping("/list/all")
    public Result<List<TemplateNameIdVo>> listTemplate(@RequestParam("moduleType") String moduleType,
                                                       @RequestParam("componentType") String componentType) {
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.setModuleType(moduleType);
        configTemplate.setComponentType(componentType);
        return ResultUtils.success(configTemplateService.listTemplateNameId(configTemplate));
    }

    @ApiOperation(value = "获取模板类型", notes = "获取模板类型")
    @GetMapping("/modules")
    public Result<List<TypeDescVo>> listModuleType(
            @RequestParam(value = "componentType", required = false) String componentType) {
        List<ModuleEnum> enums;
        if (StringUtils.isNotBlank(componentType)) {
            enums = ModuleEnum.getCacheModules(componentType);
        } else {
            enums = Arrays.asList(ModuleEnum.values());
        }
        if (enums == null) {
            enums = Collections.emptyList();
        }
        return ResultUtils.success(enums.stream()
                .map(moduleEnum -> TypeDescVo.builder().name(moduleEnum.name())
                        .desc(moduleEnum.name()).build())
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "获取组件类型", notes = "获取组件类型")
    @GetMapping("/components")
    public Result<List<TypeDescVo>> listComponentType() {
        return ResultUtils.success(Arrays.stream(ComponentEnum.values())
                .map(componentEnum -> TypeDescVo.builder().name(componentEnum.name())
                        .desc(componentEnum.getDesc()).build())
                .collect(Collectors.toList()));
    }

    /**
     * 查询模板列表全量信息
     *
     * @param moduleType    模块类型
     * @param componentType 组件类型
     * @return 分页结果
     */
    @ApiOperation(value = "查询模板列表全量信息", notes = "查询模板列表全量信息")
    @GetMapping("/listConfigTemplate/all")
    public Result<List<ConfigTemplate>> listConfigTemplate(@RequestParam("moduleType") String moduleType,
                                                           @RequestParam("componentType") String componentType) {
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.setModuleType(moduleType);
        configTemplate.setComponentType(componentType);
        return ResultUtils.success(configTemplateService.queryList(configTemplate));
    }
}
