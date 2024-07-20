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
import com.dap.sequence.client.balancer.RestTemplateBalancer;
import com.dap.sequence.client.config.SequenceConfig;
import com.dap.sequence.client.properties.ConfigBeanValue;
import com.dap.sequence.client.utils.OtherUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
    SequenceConfig sequenceConfig;

    @Autowired
    ConfigBeanValue cb;

    @Autowired
    RestTemplateBalancer restTemplateBalancer;

    @Override
    public SeqTransferDto getSeqFormServer(SeqParamsDto seqParamsDto) {
        return getSeqFormServer(seqParamsDto, OtherUtil.SEQ_CONSUMER_URL, SeqTransferDto.class);
    }

    @Override
    public SeqTransferDto getIncreaseSeqFormServer(SeqParamsDto seqParamsDto) {
        return getSeqFormServer(seqParamsDto, OtherUtil.SEQ_INCREASE_CONSUMER_URL, SeqTransferDto.class);
    }

    @Override
    public SeqTransferDto getRecoverySeqFormServer(SeqParamsDto seqParamsDto) {
        return getSeqFormServer(seqParamsDto, OtherUtil.SEQ_RECOVERY_QUERY_URL, SeqTransferDto.class);
    }

    @Override
    public SeqTransferDto recoverySequence(SeqParamsDto seqParamsDto) {
        return getSeqFormServer(seqParamsDto, OtherUtil.SEQ_RECOVERY_URL, SeqTransferDto.class);
    }

    @Override
    public Boolean clientCacheQueueCallback(SeqCallbackDto seqCallbackDto) {
        seqCallbackDto.setTenantId(cb.getTenantId());
        logger.info("clientCacheQueueCallback sequenceCode={} ", seqCallbackDto.getSequenceCode());
        String content = JSON.toJSONString(seqCallbackDto);
        String body = restTemplateBalancer.postRequest(content, OtherUtil.CacheCallBack_URL);
        logger.debug("response body{}", body);
        return true;

    }

    @Override
    public SeqObj getSeqDesignObj(String seqCode) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        return getSeqFormServer(seqParamsDto, OtherUtil.GET_SEQ_DESIGN_OBJ, SeqObj.class);
    }

    @Override
    public ResultResponse getOptionalSequenceFormServer(SeqParamsDto seqParamsDto) {
        return getSeqBody(seqParamsDto, OtherUtil.SEQ_OPTIONAL_URL);
    }

    @Override
    public ResultResponse selectedOptionalSequence(SeqParamsDto seqParamsDto) {
        return getSeqBody(seqParamsDto, OtherUtil.SEQ_OPTIONAL_SELECTED_URL);
    }

    @Override
    public ResultResponse cancelOptionalSequence(SeqParamsDto seqParamsDto) {
        return getSeqBody(seqParamsDto, OtherUtil.SEQ_OPTIONAL_CANCEL_URL);
    }

    private <T> T getSeqFormServer(SeqParamsDto seqParamsDto, String url, Class<T> clazz) {
        logger.info("getSeqFormServer sequenceCode = {}", seqParamsDto);
        ResultResponse resultResponse = getSeqBody(seqParamsDto, url);
        if (!resultResponse.getCode().equals(ExceptionEnum.SUCCESS.getResultCode())) {
            throw new SequenceException(resultResponse.getCode(), resultResponse.getMsg());
        }
        return Optional.ofNullable(resultResponse.getData())
                .map(Object::toString)
                .map(data -> JSON.parseObject(data, clazz))
                .orElse(null);
    }

    private ResultResponse getSeqBody(SeqParamsDto seqParamsDto, String url) {
        seqParamsDto.setClientPort(cb.getServerPort());
        seqParamsDto.setTenantId(cb.getTenantId());
        String content = JSON.toJSONString(seqParamsDto);
        String result = restTemplateBalancer.postRequest(content, url);
        if (StringUtils.isBlank(result)) {
            logger.error("请求url = {} 响应为空", url);
            return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR.getResultCode(), ExceptionEnum.INTERNAL_SERVER_ERROR.getResultMsg());
        }
        return JSON.parseObject(result, ResultResponse.class);
    }
}
