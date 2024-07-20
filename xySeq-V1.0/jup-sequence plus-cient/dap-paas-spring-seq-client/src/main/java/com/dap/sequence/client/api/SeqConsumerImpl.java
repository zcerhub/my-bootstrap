package com.dap.sequence.client.api;

import com.alibaba.fastjson.JSON;


import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.LocalSeqProducer;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;
import com.dap.sequence.client.feign.SeqFeignApi;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.properties.ConfigBeanValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.dap.sequence.api.dto.SeqOptionalParamsDto;

/**
 * @className SeqConsumerImpl
 * @description 序列消费实现
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Service
public class SeqConsumerImpl implements SeqConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SeqConsumerImpl.class);

    @Autowired
    ConfigBeanValue cb;

    @Autowired
    private SeqFeignApi seqFeignApi;

    /**
     * 从服务端获取序列对象
     *
     * @param seqParamsDto seqParamsDto
     * @return
     */
    @Override
    public SeqTransferDto getSeqFormServer(SeqParamsDto seqParamsDto) {
        logger.debug("getSeqFormServer sequenceCode = {}", seqParamsDto);
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        ResultResponse<SeqTransferDto> result = seqFeignApi.getStringSeq(seqParamsDto);
        if (!result.getCode().equals(ExceptionEnum.SUCCESS.getResultCode())) {
            logger.error("request getSeqFormServer fail !!!,param={},msg={}",seqParamsDto,result.getMsg());
            throw new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode());
        }
        return result.getData();
    }

    /**
     * 获取严格递增序列
     *
     * @param seqParamsDto seqParamsDto
     * @return
     */
    @Override
    public SeqTransferDto getIncreaseSeqFormServer(SeqParamsDto seqParamsDto) {
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        ResultResponse<SeqTransferDto> increaseSeqResult = seqFeignApi.getIncreaseSeqFormServer(seqParamsDto);
        return increaseSeqResult.getData();
    }

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @return
     */
    @Override
    public SeqTransferDto getRecoverySeqFormServer(SeqParamsDto seqParamsDto) {
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        ResultResponse<SeqTransferDto> result = seqFeignApi.getRecoverySeqFormServer(seqParamsDto);
        return result.getData();
    }

    /**
     * 回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @return
     */
    @Override
    public SeqTransferDto recoverySequence(SeqParamsDto seqParamsDto) {
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        ResultResponse<SeqTransferDto> result = seqFeignApi.recoverySequence(seqParamsDto);
        if(!result.getCode().equals(ExceptionEnum.SUCCESS.getResultCode())) {
            logger.error("request recoverySequence fail !!!,param={},msg={}", seqParamsDto, result.getMsg());
            return null;
        }
        return result.getData();
    }

    /**
     * 序列回收
     *
     * @param seqCallbackDto SeqCallbackDto
     * @return
     */
    @Override
    public Boolean clientCacheQueueCallBack(SeqCallbackDto seqCallbackDto) {
        seqCallbackDto.setTenantId(cb.getTenantId());
        logger.debug("clientCacheQueueCallback sequenceCode={} ", seqCallbackDto.getSequenceCode());
        ResultResponse<ExceptionEnum> result = seqFeignApi.shutdownCallbackSeq(seqCallbackDto);
        logger.debug("response body{}", result);
        return true;

    }

    /**
     * 请求server端 获取 序列规则信息
     *
     * @param seqCode seqCode
     * @return
     */
    @Override
    public SeqObj getSeqDesignObj(String seqCode) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        ResultResponse<SeqObj> result = seqFeignApi.getSeqObj(seqParamsDto);
        if (!result.getCode().equals(ExceptionEnum.SUCCESS.getResultCode())) {
            logger.error("request getSeqFormServer fail !!!,param={},msg={}",seqParamsDto, result.getMsg());
            return null;
        }
        return Optional.ofNullable(result.getData()).map(data -> JSON.parseObject(JSON.toJSONString(data), SeqObj.class)).orElse(null);

    }

    /**
     * 请求server端 获取所有序列规则信息
     *
     * @return
     */
    @Override
    public List<SeqObj> getAllSeqDesignObj() {
        ResultResponse<List<SeqObj>> result = seqFeignApi.getAllSeqDesignObj();
        return result.getData();
    }

    /**
     * 获取自选序列
     *
     * @param seqOptionalParamsDto seqParamsDto
     * @return
     */
    @Override
    public ResultResponse<SeqTransferDto> getOptionalSequenceFormServer(SeqOptionalParamsDto seqOptionalParamsDto) {
        logger.debug("getOptionalSequenceFormServer seqOptionalParamsDto={}",seqOptionalParamsDto);
        seqOptionalParamsDto.setClientPort(cb.getServerPort());
        seqOptionalParamsDto.setTenantId(cb.getTenantId());
        return seqFeignApi.getOptionalSeq(seqOptionalParamsDto);
    }

    /**
     * 选中序号
     *
     * @param seqOptionalParamsDto seqParamsDto
     * @return
     */
    @Override
    public ResultResponse<SeqTransferDto> selectedOptionalSequence(SeqOptionalParamsDto seqOptionalParamsDto) {
        logger.debug("getOptionalSequenceFormServer selectedOptionalSequence={}",seqOptionalParamsDto);
        seqOptionalParamsDto.setClientPort(cb.getServerPort());
        seqOptionalParamsDto.setTenantId(cb.getTenantId());
        return seqFeignApi.selectedOptionalSeq(seqOptionalParamsDto);
    }

    /**     
     * 服务端取消自选预留
     *
     * @param seqOptionalParamsDto seqParamsDto
     * @return
     */
    @Override
    public ResultResponse<SeqTransferDto> cancelOptionalSequence(SeqOptionalParamsDto seqOptionalParamsDto) {
        logger.debug("getOptionalSequenceFormServer cancelOptionalSequence={}",seqOptionalParamsDto);
        seqOptionalParamsDto.setClientPort(cb.getServerPort());
        seqOptionalParamsDto.setTenantId(cb.getTenantId());
        return seqFeignApi.cancelOptionalSeq(seqOptionalParamsDto);
    }

    @Override
    public List<Object> getIncreaseSeqFormLocal(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        LocalSeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
        return getSeqFormLocal(seqParamsDto,seqProducer);
    }
    private List<Object>getSeqFormLocal(SeqParamsDto seqParamsDto, LocalSeqProducer seqProducer) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        logger.info("getSeqFormServer sequenceCode = {}", seqParamsDto);
        List<Object> objects = seqProducer.createIncreaseSeq(seqFlowEngine.getSeqObj(),seqParamsDto);
        return objects;
    }

}
