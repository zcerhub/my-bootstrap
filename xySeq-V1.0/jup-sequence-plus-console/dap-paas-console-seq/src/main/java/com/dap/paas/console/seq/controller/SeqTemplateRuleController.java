package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.check.CollectionValue;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqTemplateRule;
import com.dap.paas.console.seq.feign.SeqTemplateRuleFeignApi;
import com.dap.paas.console.seq.service.SeqTemplateRuleService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
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

    private final static Logger logger = LoggerFactory.getLogger(SeqTemplateRuleController.class);

    @Autowired
    private SeqTemplateRuleService seqTemplateRuleService;

    @Autowired
    private SeqTemplateRuleFeignApi seqTemplateRuleFeignApi;

    /**
     * 序列规则新增
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则新增")
    @PostMapping("/insert")
    public ResultResponse<ExceptionEnum> insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqTemplateRule seqTemplateRule) {
        logger.debug("序列规则新增信息 = {}", seqTemplateRule);
        return seqTemplateRuleService.insert(seqTemplateRule);
    }


    /**
     * 序列规则列表分页查询
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列规则列表分页查询")
    @PostMapping("/queryPage")
    public ResultResponse<Page<SeqTemplateRule>>  queryPage(@RequestBody PageInDto<SeqTemplateRule> param) {
        return seqTemplateRuleService.queryPage(param);
    }

    /**
     * 序列规则管理编辑
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则管理编辑")
    @PutMapping("/update")
    public ResultResponse<ExceptionEnum> update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqTemplateRule seqTemplateRule) {
        logger.debug("序列规则管理编辑信息 = {}", seqTemplateRule);
        return seqTemplateRuleService.update(seqTemplateRule);
    }

    /**
     * 序列规则配置
     *
     * @param seqTemplateRule seqTemplateRule
     * @return Result
     */
    @ApiOperation(value = "序列规则配置")
    @PutMapping("/update/ruleInfo")
    public ResultResponse<ExceptionEnum> updateRuleInfo(@RequestBody @Validated(value = {ValidGroup.Valid.RuleConfig.class}) SeqTemplateRule seqTemplateRule) {
        logger.debug("序列规则配置信息 = {}", seqTemplateRule);
        return seqTemplateRuleService.updateRuleInfo(seqTemplateRule);
    }

    /**
     * 序列规则管理删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "序列规则管理删除")
    @DeleteMapping("/delete/{id}")
    public ResultResponse<ExceptionEnum> delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不合法！") String id) {
        logger.debug("序列规则管理删除id = {}", id);
        return seqTemplateRuleService.delete(id);
    }

    /**
     * 序列规则管理-批量删除
     *
     * @param ids ids
     * @return Result
     */
    @ApiOperation(value = "序列规则管理-批量删除")
    @PostMapping("/batchDelete")
    public ResultResponse<ExceptionEnum> batchDelete(@RequestBody @CollectionValue(regex = "^\\d{1,32}$", message = "序列id不合法！") List<String> ids) {
        logger.debug("序列规则管理-批量删除ids = {}", ids);
        return seqTemplateRuleService.batchDelete(ids);
    }

    /**
     * 根据类型查询所有的模板
     *
     * @param ruleType ruleType
     * @return Result
     */
    @ApiOperation("根据类型查询所有的模板")
    @PostMapping("/queryListByType/{ruleType}")
    public ResultResponse<List<SeqTemplateRule>> queryListByType(@PathVariable("ruleType") @LegalValue(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "规则类型非法") String ruleType) {
        return seqTemplateRuleService.queryListByType(ruleType);
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
    public void export(HttpServletResponse response, @RequestBody @CollectionValue(regex = "^\\d{1,32}$", message = "序列id不合法！") Set<String> ids) {
        ResultResponse<List<SeqTemplateRule>> resultResponse = seqTemplateRuleFeignApi.queryListByIds(ids);
        List<SeqTemplateRule> seqTemplateRuleList =  resultResponse.getData();
        seqTemplateRuleService.exportExcel(response, seqTemplateRuleList);
    }

    /**
     * 导入规则定义管理列表
     *
     * @param file file
     * @return Result
     */
    @ApiOperation("导入规则定义管理列表")
    @PostMapping(value = "/import", produces = {"application/json;charset=UTF-8"})
    public ResultResponse<ExceptionEnum> upload(@RequestBody @RequestParam("file") MultipartFile file) {
        List<SeqTemplateRule> seqTemplateRuleList = seqTemplateRuleService.readExcel(file);
        return seqTemplateRuleFeignApi.saveBatch(seqTemplateRuleList);
    }
}
