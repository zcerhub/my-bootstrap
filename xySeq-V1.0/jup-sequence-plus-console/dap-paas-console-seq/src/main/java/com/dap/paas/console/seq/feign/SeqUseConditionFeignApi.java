package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqUseCondition;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 *
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqUseConditionFeignApi", url = "${dap.sequence.urls:}")
public interface SeqUseConditionFeignApi {

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/v1/seq/condition/queryPage")
    ResultResponse<Page<SeqUseCondition>> queryPage(@RequestBody PageInDto<SeqUseCondition> param);

    /**
     * 删除
     *
     * @param map
     * @return
     */
    @PostMapping("/v1/seq/condition/deleteAttributeData")
    ResultResponse<Integer> deleteAttributeData(@RequestBody  Map<String, Object> map);

    /**
     * 保存
     *
     * @param seqUseCondition
     * @return
     */
    @PostMapping("/v1/seq/condition/saveObject")
    ResultResponse<Integer> saveObject(@RequestBody SeqUseCondition seqUseCondition);
}
