package com.dap.sequence.api.core;


import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.obj.SeqObj;

/**
 * @Description: 序列消费者对象
 * @Author: liu
 * @Date: 2022/2/11
 * @Version: 1.0.0
 */
public interface SeqConsumer {

    /**
     * 从服务端获取序列对象
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto getSeqFormServer(SeqParamsDto seqParamsDto);

    /**
     * 获取严格递增序列
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto getIncreaseSeqFormServer(SeqParamsDto seqParamsDto);

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto getRecoverySeqFormServer(SeqParamsDto seqParamsDto);

    /**
     * 回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto recoverySequence(SeqParamsDto seqParamsDto);

    /**
     * 序列回收
     *
     * @param SeqCallbackDto SeqCallbackDto
     * @return Boolean
     */
    Boolean clientCacheQueueCallback(SeqCallbackDto SeqCallbackDto);

    /**
     * 请求server端 获取 序列规则信息
     *
     * @param seqCode seqCode
     * @return SeqObj
     */
    SeqObj getSeqDesignObj(String seqCode);

    /**
     * 获取自选序列
     *
     * @param seqParamsDto seqParamsDto
     * @return ResultResponse
     */
    ResultResponse getOptionalSequenceFormServer(SeqParamsDto seqParamsDto);

    /**
     * 选中序号
     *
     * @param seqParamsDto seqParamsDto
     * @return ResultResponse
     */
    ResultResponse selectedOptionalSequence(SeqParamsDto seqParamsDto);

    /**
     * 服务端取消自选预留
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    ResultResponse cancelOptionalSequence(SeqParamsDto seqParamsDto);
}
