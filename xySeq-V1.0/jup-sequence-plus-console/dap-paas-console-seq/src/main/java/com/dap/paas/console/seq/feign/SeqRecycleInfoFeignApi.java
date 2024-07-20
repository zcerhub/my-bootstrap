package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqRecycleInfo;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqRecycleInfoFeignApi", url = "${dap.sequence.urls:}")
public interface SeqRecycleInfoFeignApi {

    /**
     * 分页查询回收信息
     *
     * @param param
     * @return
     */
    @PostMapping("/v1/seq/recycle/queryPage")
    ResultResponse<Page<SeqRecycleInfo>> queryPage(@RequestBody PageInDto<SeqRecycleInfo> param);

}
