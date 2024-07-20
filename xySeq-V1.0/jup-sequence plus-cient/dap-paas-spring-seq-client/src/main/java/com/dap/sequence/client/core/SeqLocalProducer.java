package com.dap.sequence.client.core;


import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;

/**
 * @Description: 序列消费者对象
 * @Author: liu
 * @Date: 2022/2/11
 * @Version: 1.0.0
 */
public interface SeqLocalProducer {


    /**
     * 从服务端获取序列对象 ,先从缓存中获取
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto getSeqFormServer(SeqParamsDto seqParamsDto);

    /**
     * 创建严格递增序号
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto createIncreaseSeq(SeqParamsDto seqParamsDto);

    /**
     * 创建回收递增序号
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto createRecoverySeq(SeqParamsDto seqParamsDto);

    /**
     * 创建回收递增序号
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto recoverySeq(SeqParamsDto seqParamsDto);

    /**
     * 创建并获取序列，不缓存
     *
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    SeqTransferDto getAndCreateSeq(SeqParamsDto seqParamsDto);

    /**
     * 选定序号
     *
     * @param seqParamsDto seqParamsDto
     */
    void selectedOptionalSeq(SeqParamsDto seqParamsDto);

    /**
     * 取消自选序号
     *
     * @param seqParamsDto seqParamsDto
     */
    void cancelOptionalSeq(SeqParamsDto seqParamsDto);
}
