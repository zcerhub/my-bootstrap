package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRuleUpdateOrder;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqInstanceRuleFeignApi", url = "${dap.sequence.urls:}")
public interface SeqInstanceRuleFeignApi {

    /**
     * 序列设计规则列表
     *
     * @param param param
     * @return
     */
    @PostMapping("/v1/seq/rule/queryPage")
    ResultResponse<Page<SeqInstanceRule>> queryPage(@RequestBody PageInDto<SeqInstanceRule> param);

    /**
     * 序列设计规则新增
     *
     * @param seqInstanceRule seqInstanceRule
     * @return
     */
    @PostMapping("/v1/seq/rule/insert")
    ResultResponse<ExceptionEnum> insert(@RequestBody @Validated(value = {ValidGroup.Valid.Create.class}) SeqInstanceRule seqInstanceRule);


    /**
     * 序列设计规则编辑
     *
     * @param seqInstanceRule seqInstanceRule
     * @return
     */
    @PutMapping("/v1/seq/rule/update")
    ResultResponse<ExceptionEnum> update(@RequestBody @Validated(value = {ValidGroup.Valid.Update.class})  SeqInstanceRule seqInstanceRule);

    /**
     * 序列设计规则修改
     *
     * @param seqRuleUpdateOrder seqRuleUpdateOrder
     * @return
     */
    @PostMapping("/v1/seq/rule/updateOrder")
    ResultResponse<ExceptionEnum> updateOrder(@RequestBody @Validated SeqRuleUpdateOrder seqRuleUpdateOrder);

    /**
     * 序列设计规则删除
     *
     * @param id id
     * @return
     */
    @PostMapping("/v1/seq/rule/delete/{id}")
    ResultResponse<ExceptionEnum> delete(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "序列id不能为空") String id);

    /**
     * 序列测试
     *
     * @param seqNo seqNo
     * @return
     */
    @GetMapping("/v1/seq/rule/testSequence/{seqNo}")
    ResultResponse<String> testSeqCode(@PathVariable("seqNo") @Size(min = 1, max = 255, message = "序列编码不能为空！") String seqNo);

    /**
     * 根据designId删除规则
     *
     * @param seqDesignId
     * @return
     */
    @GetMapping("/v1/seq/rule/delObjectByDesignId/{seqDesignId}")
    ResultResponse<Integer> delObjectByDesignId(@PathVariable("seqDesignId") String seqDesignId);

    /**
     * 查询集合
     *
     * @param map
     * @return
     */
    @PostMapping("/v1/seq/rule/queryList")
    ResultResponse<List<SeqInstanceRule>> queryList(Map map);

}
