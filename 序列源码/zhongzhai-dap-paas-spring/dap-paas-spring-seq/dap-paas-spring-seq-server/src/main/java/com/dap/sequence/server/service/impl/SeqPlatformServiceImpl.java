package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.entity.SeqUseCondition;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.utils.JsonUtils;
import com.dap.sequence.server.utils.NetUtils;
import com.dap.sequence.server.utils.RestTemplateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
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
public class SeqPlatformServiceImpl implements SeqPlatformService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqPlatformServiceImpl.class);

    public static final String HTTP = "http://";

    private static final String SEQ_DESIGN_QUERY = "/v1/seq/server/seq-list?status={2}";

    private static final String SEQ_RULE_QUERY = "/v1/seq/server/seq-rules";

    private static final String SEQ_USE_SAVE = "/v1/seq/server/seq-use";

    @Autowired
    private RestTemplateUtils templateUtils;

    @Value("${gientech.dap.sequence.platform.url:}")
    private String platformUrl;

    @Value("${server.port:9080}")
    private String serverPort;

    @Value("${gientech.dap.sequence.tenantId:391312369558487040}")
    private String tenantId;

    @Override
    public boolean isUsePlatform() {
        return StringUtils.isNotBlank(platformUrl) && isCanUse(HTTP + platformUrl);
    }

    private boolean isCanUse(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection oc = (HttpURLConnection) urlObj.openConnection();
            oc.setUseCaches(false);

            // 设置超时时间
            oc.setConnectTimeout(3000);

            // 请求状态
            int status = oc.getResponseCode();
            if (200 == status) {
                // 200是请求地址顺利连通。。
                return true;
            }
        } catch (Exception e) {
            LOG.warn("地址：{} 无法联通.msg = {}", url, e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public List<SeqDesignPo> getAllSeqDesign() {
        String url = HTTP + platformUrl + SEQ_DESIGN_QUERY;
        ResponseEntity<ResultResponse> responseEntity = templateUtils.get(url, addHeader(), ResultResponse.class, "2");
        return getData(responseEntity, new TypeReference<List<SeqDesignPo>>() {
        });
    }

    @Override
    public List<SeqInstanceRule> getSeqInstanceRules(String seqDesignId) {
        String url = HTTP + platformUrl + SEQ_RULE_QUERY;
        if (StringUtils.isNotBlank(seqDesignId)) {
            url = url + "?seqDesignId=" + seqDesignId;
        }
        ResponseEntity<ResultResponse> responseEntity = templateUtils.get(url, addHeader(), ResultResponse.class);
        return getData(responseEntity, new TypeReference<List<SeqInstanceRule>>() {
        });
    }

    @Override
    public void saveUseCondition(SeqUseCondition seqUseCondition) {
        String url = HTTP + platformUrl + SEQ_USE_SAVE;
        ResponseEntity<ResultResponse> responseEntity = templateUtils.post(url, addHeader(), seqUseCondition, ResultResponse.class);
        String body = getBody(responseEntity).toString();
        LOG.info("上报使用记录到管控结果为：{}", body);
    }

    /**
     * 解析data
     *
     * @param responseEntity responseEntity
     * @param tTypeReference tTypeReference
     * @return T
     */
    public <T> T getData(ResponseEntity<ResultResponse> responseEntity, TypeReference<T> tTypeReference) {
        ResultResponse resultResponse = Optional.ofNullable(getBody(responseEntity)).orElseThrow(() -> new SequenceException(ExceptionEnum.INTERNAL_SERVER_ERROR));
        Object object = Optional.of(resultResponse)
                .filter(response -> response.getCode().equals(ExceptionEnum.SUCCESS.getResultCode()))
                .map(ResultResponse::getData)
                .orElseThrow(() -> new SequenceException(resultResponse.getCode(), resultResponse.getMsg()));
        return JsonUtils.readValue(JsonUtils.obj2Str(object), tTypeReference);
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
        header.put("tenantId", tenantId);
        return header;
    }
}
