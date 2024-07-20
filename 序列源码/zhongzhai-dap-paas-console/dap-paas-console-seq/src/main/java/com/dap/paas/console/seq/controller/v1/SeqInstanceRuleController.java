package com.dap.paas.console.seq.controller.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRuleUpdateOrder;
import com.dap.paas.console.seq.exception.ExceptionEnum;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqInstanceRuleController.class);

    @Autowired
    private SeqDesignService seqDesignService;

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
    public Result queryPage(@RequestBody PageInDto<SeqInstanceRule> param) {
        Page seqInstanceRuleList = seqInstanceRuleService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, seqInstanceRuleList);
    }

    /**
     * 序列设计规则新增
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    @ApiOperation(value = "序列设计规则新增")
    @PostMapping("/insert")
    public Result insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqInstanceRule seqInstanceRule) {
        LOGGER.info("序列设计规则新增信息 = {}", seqInstanceRule);
        checkOptional(seqInstanceRule);
        seqInstanceRuleService.saveObject(seqInstanceRule);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    private void checkOptional(SeqInstanceRule seqInstanceRule) {
        List<SeqInstanceRule> seqInstanceRules = seqInstanceRuleService.queryList(Collections.singletonMap("seqDesignId", seqInstanceRule.getSeqDesignId()));
        seqInstanceRules.add(seqInstanceRule);
        Map<String, Long> typeMap = seqInstanceRules.stream()
                .filter(rule -> rule.getInstanceRuleType().equals(SeqConstant.NUMBER_RULE) && rule.getInstanceRuleInfo().contains("isOptional\":true")
                        || rule.getInstanceRuleType().equals(SeqConstant.SPECIAL_RULE))
                .collect(Collectors.groupingBy(SeqInstanceRule::getInstanceRuleType, Collectors.counting()));
        if (typeMap.size() > 1) {
            throw new ServiceException(ExceptionEnum.SEQ_OPTIONAL_PLACE_RULE_MAX_1.getResultMsg());
        }
        if (Optional.ofNullable(typeMap.get(SeqConstant.NUMBER_RULE)).orElse(0L) > 1) {
            throw new ServiceException(ExceptionEnum.SEQ_OPTIONAL_RULE_MAX_1.getResultMsg());
        }
    }

    /**
     * 序列设计规则编辑
     *
     * @param seqInstanceRule seqInstanceRule
     * @return Result
     */
    @ApiOperation(value = "序列设计规则编辑")
    @PutMapping("/update")
    public Result update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class}) SeqInstanceRule seqInstanceRule) {
        LOGGER.info("序列设计规则编辑信息 = {}", seqInstanceRule);
        SeqInstanceRule instanceRule = seqInstanceRuleService.getObjectById(seqInstanceRule.getId());
        if (instanceRule == null) {
            throw new ServiceException(ExceptionEnum.SEQ_RULE_NOT_EXIST.getResultMsg());
        }
        instanceRule.setInstanceRuleInfo(seqInstanceRule.getInstanceRuleInfo());
        checkOptional(instanceRule);
        seqInstanceRuleService.updateObject(seqInstanceRule);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 规则修改
     *
     * @param seqRuleUpdateOrder seqRuleUpdateOrder
     * @return Result
     */
    @PostMapping("/updateOrder")
    public Result updateOrder(@RequestBody @Validated SeqRuleUpdateOrder seqRuleUpdateOrder) {
        seqRuleUpdateOrder.getInstanceRuleList().forEach(seqInstanceRule -> seqInstanceRuleService.updateObject(seqInstanceRule));
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列设计规则删除
     *
     * @param id id
     * @return Result
     */
    @ApiOperation("序列设计规则删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不能为空！") String id) {
        LOGGER.info("序列设计规则删除id = {}", id);
        Map<String, String> map = new HashMap<>(2);
        map.put("seqDesignId", seqInstanceRuleService.getObjectById(id).getSeqDesignId());
        if (seqInstanceRuleService.queryList(map).size() == 1) {
            return ResultUtils.error(ResultEnum.FAILED.getCode(), "规则不能为空");
        }
        seqInstanceRuleService.delObjectById(id);
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 序列测试
     *
     * @param seqNo seqNo
     * @return Result
     */
    @ApiOperation("规则测试")
    @GetMapping("/testSequence/{seqNo}")
    public Result testSeqCode(@PathVariable("seqNo") @Size(min = 1, max = 255, message = "序列编码不能为空！") String seqNo) {
        SequenceUtil sequenceUtil = new SequenceUtil(seqDesignService, seqInstanceRuleService);
        return sequenceUtil.test(seqNo);
    }
}
