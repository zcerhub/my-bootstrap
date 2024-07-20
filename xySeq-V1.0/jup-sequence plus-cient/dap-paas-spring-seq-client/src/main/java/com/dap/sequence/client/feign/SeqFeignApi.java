package com.dap.sequence.client.feign;

import com.dap.sequence.api.dto.*;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.obj.SeqObj;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 序列FeignApi
 *
 * @date 2024/6/22 0022 11:45
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqFeignApi", url = "${dap.sequence.urls:}")
public interface SeqFeignApi {
    /**
     * 获取普通序列
     *
     * @param seqParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/getStringSeq")
    ResultResponse<SeqTransferDto> getStringSeq(@RequestBody SeqParamsDto seqParamsDto);

    /**
     * 获取自选序列
     *
     * @param seqOptionalParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/getOptionalSeq")
    ResultResponse<SeqTransferDto> getOptionalSeq(@RequestBody SeqOptionalParamsDto seqOptionalParamsDto);

    /**
     * 选定自选序列
     *
     * @param seqOptionaParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/selectedOptionalSeq")
    ResultResponse<SeqTransferDto> selectedOptionalSeq(@RequestBody SeqOptionalParamsDto seqOptionaParamsDto);

    /**
     * 取消自选序列
     *
     * @param seqOptionalParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/cancelOptionalSeq")
    ResultResponse<SeqTransferDto> cancelOptionalSeq(@RequestBody SeqOptionalParamsDto seqOptionalParamsDto);

    /**
     * 获取序列对象
     *
     * @param seqParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/getSeqDesignObj")
    ResultResponse<SeqObj> getSeqObj(@RequestBody SeqParamsDto seqParamsDto);

    /**
     * 获取所有序列对象
     *
     * @return
     */
    @PostMapping("/v1/seq/publish/getAllSeqDesignObj")
    ResultResponse<List<SeqObj>> getAllSeqDesignObj();

    /**
     * 回收序列
     *
     * @param seqCallbackDto
     * @return
     */
    @PostMapping("/v1/seq/callback/shutdownCallbackSeq")
    ResultResponse<ExceptionEnum> shutdownCallbackSeq(@RequestBody SeqCallbackDto seqCallbackDto);

    /**
     * 严格递增序列
     *
     * @param seqParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/getIncreaseSeq")
    ResultResponse<SeqTransferDto> getIncreaseSeqFormServer(@RequestBody SeqParamsDto seqParamsDto);

    /**
     * 获取普通非自选序列
     *
     * @param seqParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/getRecoverySeq")
    ResultResponse<SeqTransferDto> getRecoverySeqFormServer(@RequestBody SeqParamsDto seqParamsDto);

    /**
     * 回收普通非自选序列
     *
     * @param seqParamsDto
     * @return
     */
    @PostMapping("/v1/seq/publish/recoverySeq")
    ResultResponse<SeqTransferDto> recoverySequence(@RequestBody SeqParamsDto seqParamsDto);

    /**
     * 客户端启动后，维护心跳
     */
    @PostMapping("/v1/seq/publish/heartbeat")
    ResultResponse<Boolean> heartbeat(@RequestBody SeqSdkMonitorDto dto);

    /**
     * 客户端启动时，向序列中心申请workId
     */
    @PostMapping( "/v1/seq/publish/snowflakewordid")
    ResultResponse<Integer> snowflakewordid(@RequestBody SeqSdkMonitorDto seqSdkMonitor);

    /**
     * SDK 请求server端 获取所有 Server 的地址服务器信息
     */
    @PostMapping( "/v1/seq/publish/getStringIps")
    ResultResponse<StringBuilder> getStringIps(String seqClusterName);
}
