package com.dap.paas.console.seq.controller.v1;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.util.JsonUtil;
import com.dap.paas.console.seq.check.CollectionValue;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqTemplateRule;
import com.dap.paas.console.seq.service.SeqTemplateRuleService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @className SeqTemplateRuleController
 * @description 序列规则模板管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列规则模板管理")
@RestController
@RequestMapping("/v1/seq/template")
@Validated
public class SeqTemplateRuleController {

    private final static Logger LOG = LoggerFactory.getLogger(SeqTemplateRuleController.class);

    @Autowired
    private SeqTemplateRuleService seqTemplateRuleService;

    /**
     * 序列规则新增
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则新增")
    @PostMapping("/insert")
    public Result insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqTemplateRule seqTemplateRule) {
        LOG.info("序列规则新增信息 = {}", seqTemplateRule);
        Map<String, String> queryMap = new HashMap<>();
        String ruleName = seqTemplateRule.getRuleName();
        queryMap.put("ruleName", ruleName);
        queryMap.put("ruleType", seqTemplateRule.getRuleType());
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> queryMap.put("tenantId", tenantId));
        SeqTemplateRule existSeq = seqTemplateRuleService.getObjectByMap(queryMap);
        if (Objects.nonNull(existSeq)) {
            return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), "该规则【" + ruleName + "】已存在，请修改后再操作!");
        }
        seqTemplateRuleService.saveObject(seqTemplateRule);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }


    /**
     * 序列规则列表分页查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列规则列表分页查询")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqTemplateRule> param) {
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> param.getRequestObject().setTenantId(tenantId));
        Page SeqDesignPo = seqTemplateRuleService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, SeqDesignPo);
    }

    /**
     * 序列规则管理编辑
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则管理编辑")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqTemplateRule seqTemplateRule) {
        LOG.info("序列规则管理编辑信息 = {}", seqTemplateRule);
        seqTemplateRuleService.updateObject(seqTemplateRule);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列规则配置
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则配置")
    @PutMapping("/update/ruleInfo")
    public Result updateRuleInfo(@RequestBody @Validated(value = {ValidGroup.Valid.RuleConfig.class}) SeqTemplateRule seqTemplateRule) {
        LOG.info("序列规则配置信息 = {}", seqTemplateRule);
        seqTemplateRuleService.updateObject(seqTemplateRule);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列规则管理删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列规则管理删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不合法！") String id) {
        LOG.info("序列规则管理删除id = {}", id);
        seqTemplateRuleService.delObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列规则管理-批量删除
     *
     * @param ids ids
     * @return Result
     */
    @ApiOperation(value = "序列规则管理-批量删除")
    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody @CollectionValue(regex = "^\\d{1,32}$", message = "序列id不合法！") List<String> ids) {
        LOG.info("序列规则管理-批量删除ids = {}", JsonUtil.obj2String(ids));
        Optional.ofNullable(ids).filter(CollectionUtils::isNotEmpty)
                .ifPresent(list -> seqTemplateRuleService.delObjectByIds(list));
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 根据类型查询所有的模板
     *
     * @param ruleType ruleType
     * @return Result
     */
    @ApiOperation("根据类型查询所有的模板")
    @PostMapping("/queryListByType/{ruleType}")
    public Result queryListByType(@PathVariable("ruleType") @LegalValue(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "规则类型非法") String ruleType) {
        Map<String, String> param = Collections.singletonMap("ruleType", ruleType);
        List<SeqTemplateRule> templateList = seqTemplateRuleService.queryList(param);
        return ResultUtils.success(ResultEnum.SUCCESS, templateList);
    }

    /**
     * 下载选中的规则定义管理列表excel
     *
     * @param response response
     * @param ids ids
     * @return Result
     */
    @ApiOperation("下载选中的规则定义管理列表excel")
    @PostMapping("/export")
    public Result export(HttpServletResponse response, @RequestBody @CollectionValue(regex = "^\\d{1,32}$", message = "序列id不合法！") Set<String> ids) {
        List<SeqTemplateRule> seqTemplateRuleList = seqTemplateRuleService.queryListByIds(ids);
        seqTemplateRuleService.exportExcel(response, seqTemplateRuleList);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 导入规则定义管理列表
     *
     * @param file file
     * @return Result
     */
    @ApiOperation("导入规则定义管理列表")
    @PostMapping(value = "/import", produces = {"application/json;charset=UTF-8"})
    public Result upload(@RequestBody @RequestParam("file") MultipartFile file) {
        List<SeqTemplateRule> seqTemplateRuleList = seqTemplateRuleService.readExcel(file);
        return seqTemplateRuleService.saveBatch(seqTemplateRuleList);
    }
}
