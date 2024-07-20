package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.service.SeqFromPlatformService;
import com.dap.sequence.server.utils.JsonUtils;
import com.dap.sequence.server.utils.NetUtils;
import com.dap.sequence.server.utils.RestTemplateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @className SeqFromPlatformServiceImpl
 * @description 从管控端获取
 * @author renle
 * @date 2023/12/13 17:05:15 
 * @version: V23.06
 */

@Service
public class SeqFromPlatformServiceImpl implements SeqFromPlatformService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqFromPlatformServiceImpl.class);

    public static final String HTTP = "http://";

    private static final String SEQ_DESIGN_QUERY_TENANT = "/v1/seq/server/seq-list?status={2}";

    @Autowired
    private RestTemplateUtils templateUtils;

    @Value("${gientech.dap.sequence.platform.url:}")
    private String platformUrl;

    @Value("${server.port:9080}")
    private String serverPort;

    @Override
    public List<SeqDesignPo> getAllSeqDesign() {
        String url = HTTP + platformUrl + SEQ_DESIGN_QUERY_TENANT;
        ResponseEntity<ResultResponse> responseEntity = templateUtils.get(url, addHeader(), ResultResponse.class, "2");
        return getData(responseEntity, new TypeReference<List<SeqDesignPo>>() {
        });
    }

    private <T> T getData(ResponseEntity<ResultResponse> responseEntity, TypeReference<T> tTypeReference) {
        ResultResponse resultResponse = Optional.ofNullable(getBody(responseEntity)).orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
        Object object = Optional.of(resultResponse)
                .filter(response -> response.getCode().equals(ExceptionEnum.SUCCESS.getResultCode()))
                .map(ResultResponse::getData)
                .orElseThrow(() -> new SequenceException(resultResponse.getCode(), resultResponse.getMsg()));
        return JsonUtils.readValue(object.toString(), tTypeReference);
    }

    private <T> T getBody(ResponseEntity<T> response) {
        Optional.ofNullable(response)
                .map(ResponseEntity::getStatusCode)
                .filter(httpStatus -> !httpStatus.isError())
                .orElseThrow(() -> new SequenceException("请求管控端报错"));
        return response.getBody();
    }

    private Map<String, String> addHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("port", serverPort);
        header.put("hostIp", NetUtils.ip());
        return header;
    }
}
