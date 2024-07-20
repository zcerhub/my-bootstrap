package com.dap.paas.console.seq.controller;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRuleUpdateOrder;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @className SeqInstanceRuleController
 * @description 序列设计规则管理
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列设计规则管理")
@RestController
@RequestMapping("/v1/seq/rule")
@Validated
public class SeqInstanceRuleController {

    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;


    /**
     * 序列设计规则列表
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("序列设计规则列表")
    @PostMapping("/queryPage")
    public ResultResponse<Page<SeqInstanceRule>> queryPage(@RequestBody PageInDto<SeqInstanceRule> param) {
        return seqInstanceRuleService.queryPage(param);
    }

    /**
     * 序列设计规则新增
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    @ApiOperation(value = "序列设计规则新增")
    @PostMapping("/insert")
    public ResultResponse<ExceptionEnum> insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqInstanceRule seqInstanceRule) {
        return seqInstanceRuleService.insert(seqInstanceRule);
    }

    /**
     * 序列设计规则编辑
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    @ApiOperation(value = "序列设计规则编辑")
    @PutMapping("/update")
    public ResultResponse<ExceptionEnum> update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqInstanceRule seqInstanceRule) {
        return seqInstanceRuleService.update(seqInstanceRule);
    }

    /**
     * 规则修改
     *
     * @param seqRuleUpdateOrder seqRuleUpdateOrder
     * @return Result
     */
    @PostMapping("/updateOrder")
    public ResultResponse<ExceptionEnum> updateOrder(@RequestBody @Validated SeqRuleUpdateOrder seqRuleUpdateOrder) {
        return seqInstanceRuleService.updateOrder(seqRuleUpdateOrder);
    }

    /**
     * 序列设计规则删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation("序列设计规则删除")
    @DeleteMapping("/delete/{id}")
    public ResultResponse<ExceptionEnum> delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不能为空！") String id) {
        return seqInstanceRuleService.delete(id);
    }

    /**
     * 序列测试
     *
     * @param seqNo seqNo
     * @return Result
     */
    @ApiOperation("规则测试")
    @GetMapping("/testSequence/{seqNo}")
    public ResultResponse<String> testSeqCode(@PathVariable("seqNo") @Size(min = 1, max = 255, message = "序列编码不能为空！") String seqNo) {
        return seqInstanceRuleService.testSeqCode(seqNo);
    }
}
