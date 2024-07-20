package com.dap.sequence.api.core;


import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqOptionalParamsDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.obj.SeqObj;

import java.util.List;

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
    Boolean clientCacheQueueCallBack(SeqCallbackDto SeqCallbackDto);

    /**
     * 请求server端 获取 序列规则信息
     *
     * @param seqCode seqCode
     * @return SeqObj
     */
    SeqObj getSeqDesignObj(String seqCode);

    /**
     * 请求server端 获取 所有序列规则信息
     *
     * @return SeqObj
     */
    List<SeqObj> getAllSeqDesignObj();

    /**
     * 获取自选序列
     *
     * @param seqParamsDto seqParamsDto
     * @return ResultResponse
     */
    ResultResponse<SeqTransferDto> getOptionalSequenceFormServer(SeqOptionalParamsDto seqParamsDto);

    /**
     * 选中序号
     *
     * @param seqParamsDto seqParamsDto
     * @return ResultResponse
     */
    ResultResponse<SeqTransferDto> selectedOptionalSequence(SeqOptionalParamsDto seqParamsDto);

    /**
     * 服务端取消自选预留
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    ResultResponse<SeqTransferDto> cancelOptionalSequence(SeqOptionalParamsDto seqParamsDto);

    /**
     * 本地获取严格递增序列
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    List<Object> getIncreaseSeqFormLocal(SeqParamsDto seqParamsDto);

}
