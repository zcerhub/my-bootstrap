package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqCurNum;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqOptionalRecordFeignApi", url = "${dap.sequence.urls:}")
public interface SeqOptionalRecordFeignApi {

    /**
     * 分页查询当前值
     *
     * @param param
     * @return
     */
    @PostMapping("/v1/seq/optional/queryPage")
    ResultResponse<Page<SeqOptionalRecord>> queryPage(@RequestBody PageInDto<SeqOptionalRecord> param);

    /**
     * 保存自选记录
     *
     * @param seqOptionalRecord
     * @return
     */
    @PostMapping("/v1/seq/optional/saveObject")
    ResultResponse<Integer> saveObject(@RequestBody SeqOptionalRecord seqOptionalRecord);

    /**
     *
     * 更新自选自录
     * @param seqOptionalRecord
     * @return
     */
    @PostMapping("/v1/seq/optional/updateObject")
    ResultResponse<Integer> updateObject(@RequestBody SeqOptionalRecord seqOptionalRecord);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/v1/seq/optional/delObjectById/{id}")
    ResultResponse<Integer> delObjectById(@PathVariable("id") String id);
}
