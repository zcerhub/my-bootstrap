package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.check.CollectionValue;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqTemplateRule;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqTemplateRuleFeignApi", url = "${dap.sequence.urls:}")
public interface SeqTemplateRuleFeignApi {

    /**
     * 序列规则新增
     *
     * @param seqTemplateRule seqTemplateRule
     * @return
     */
    @ApiOperation("序列规则新增")
    @PostMapping("/v1/seq/template/insert")
    ResultResponse<ExceptionEnum> insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqTemplateRule seqTemplateRule);

    /**
     * 序列规则列表分页查询
     *
     * @param param param
     * @return
     */
    @ApiOperation("序列规则列表分页查询")
    @PostMapping("/v1/seq/template/queryPage")
    ResultResponse<Page<SeqTemplateRule>> queryPage(@RequestBody PageInDto<SeqTemplateRule> param);

    /**
     * 序列规则管理编辑
     *
     * @param seqTemplateRule seqTemplateRule
     * @return
     */
    @ApiOperation("序列规则管理编辑")
    @PutMapping("/v1/seq/template/update")
    ResultResponse<ExceptionEnum> update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class})  SeqTemplateRule seqTemplateRule);

    /**
     * 序列规则配置
     *
     * @param seqTemplateRule seqTemplateRule
     * @return
     */
    @ApiOperation("序列规则配置")
    @PutMapping("/v1/seq/template/update/ruleInfo")
    ResultResponse<ExceptionEnum> updateRuleInfo(@RequestBody @Validated(value = {ValidGroup.Valid.RuleConfig.class})  SeqTemplateRule seqTemplateRule);


    /**
     * 序列规则管理删除
     *
     * @param id id
     * @return
     */
    @ApiOperation("序列规则管理删除")
    @DeleteMapping("/v1/seq/template/delete/{id}")
    ResultResponse<ExceptionEnum> delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不能为空") String id);

    /**
     * 序列规则管理-批量删除
     *
     * @param ids ids
     * @return
     */
    @ApiOperation("序列规则管理-批量删除")
    @DeleteMapping("/v1/seq/template/batchDelete")
    ResultResponse<ExceptionEnum> batchDelete(@RequestBody @CollectionValue(regex = "^\\d{1,32}$", message = "序列id不能为空") List<String> ids);

    /**
     * 根据类型查询所有模板
     *
     * @param ruleType ruleType
     * @return result
     */
    @ApiOperation("根据类型查询所有模板")
    @PostMapping("/v1/seq/template/queryListByType/{ruleType}")
    ResultResponse<List<SeqTemplateRule>> queryListByType(@PathVariable("ruleType") @LegalValue(values = {"1","2","3","4","5","6","7"}, message = "规则类型非法") String ruleType);

    /**
     * 批量添加规则定义管理列表
     *
     * @param seqTemplateRuleList seqTemplateRuleList
     * @return Result
     */
    @PostMapping("/v1/seq/template/saveBatch")
    ResultResponse<ExceptionEnum> saveBatch(@RequestBody List<SeqTemplateRule> seqTemplateRuleList);

    /**
     * 根据ids集合查询规则定义管理
     *
     * @param ids
     * @return
     */
    @PostMapping("/v1/seq/template/queryListByIds")
    ResultResponse<List<SeqTemplateRule>> queryListByIds(Set<String> ids);

    /**
     * 查询map
     *
     * @param queryMap
     * @return
     */
    @PostMapping("/v1/seq/template/getObjectByMap")
    ResultResponse<SeqTemplateRule> getObjectByMap(@RequestBody Map<String, String> queryMap);

    /**
     * 查询集合
     *
     * @param seqTemplateRule
     * @return
     */
    @PostMapping("/v1/seq/template/queryList")
    ResultResponse<List<SeqTemplateRule>> queryList(@RequestBody SeqTemplateRule seqTemplateRule);
}
