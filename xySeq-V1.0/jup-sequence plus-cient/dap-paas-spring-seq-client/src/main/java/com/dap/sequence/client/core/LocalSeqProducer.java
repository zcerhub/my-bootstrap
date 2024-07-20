package com.dap.sequence.client.core;


import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;

import java.util.List;

/**
 * @className SeqProducer
 * @description 序列生产接口
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public interface LocalSeqProducer {


    /**
     * 创建序号
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    List<Object> createSeq(SeqObj seqObj, SeqParamsDto seqParamsDto) throws SequenceException;

    /**
     * 创建严格递增序列
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @return List
     */
    List<Object> createIncreaseSeq(SeqObj seqObj, SeqParamsDto seqParamsDto);

    /**
     * 创建回收序列
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @return List
     */
    List<Object> createRecoverySeq(SeqObj seqObj, SeqParamsDto seqParamsDto);

    /**
     * 创建自选序号
     *
     * @param obj obj
     * @param seqParamsDto seqParamsDto
     * @return SeqTransferDto
     */
    List<Object> createOptional(SeqObj obj, SeqParamsDto seqParamsDto) throws SequenceException;

    /**
     * 加载回收序号
     *
     * @param seqCallbackDto seqCallbackDto
     * @param callBackNumList callBackNumList
     */
    void loadSeq(SeqCallbackDto seqCallbackDto, List callBackNumList);


    /**
     * 创建序列并放入缓存中
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     */
    void createSeqAndCache(SeqObj seqObj, SeqParamsDto seqParamsDto) throws SequenceException;

    /**
     * 创建序列并放入缓存中
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     */
    void refreshSeqAndCache(SeqObj seqObj, SeqParamsDto seqParamsDto) throws SequenceException;
}
