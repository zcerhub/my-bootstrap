package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqCurNum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqCurNumFeignApi", url = "${dap.sequence.urls:}")
public interface SeqCurNumFeignApi {

    /**
     * 分页查询当前值
     *
     * @param param
     * @return
     */
    @PostMapping("/v1/seq/useInfo/queryPage")
    ResultResponse<Page<SeqCurNum>> queryPage(@RequestBody PageInDto<SeqCurNum> param);

    /**
     * 序列使用情况编辑
     *
     * @param seqUseCondition seqUseCondition
     * @return Result
     */
    @PutMapping("/v1/seq/useInfo/update")
    ResultResponse<Integer> update(SeqCurNum seqUseCondition);
}
