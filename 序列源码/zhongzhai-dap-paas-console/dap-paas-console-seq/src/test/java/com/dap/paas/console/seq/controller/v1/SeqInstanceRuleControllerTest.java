package com.dap.paas.console.seq.controller.v1;

import com.alibaba.fastjson.JSONObject;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class SeqInstanceRuleControllerTest {

    @InjectMocks
    private SeqInstanceRuleController seqInstanceRuleController;
    @Mock
    private SeqDesignService seqDesignService;
    @Mock
    private SeqInstanceRuleService seqInstanceRuleService;

    @org.junit.jupiter.api.Test
    void queryPage() {
        PageInDto<SeqInstanceRule> param =new PageInDto<>();
        SeqInstanceRule seqInstanceRule=new SeqInstanceRule();
        seqInstanceRule.setSeqDesignId("1748221824268206080");
        param.setRequestObject(seqInstanceRule);
        seqInstanceRuleController.queryPage(param);
    }

    @org.junit.jupiter.api.Test
    void insert() {
        String requestStr="{\"instanceRuleInfo\":\"{\\\"ruleType\\\":\\\"4\\\",\\\"numberStep\\\":1,\\\"numberStart\\\":\\\"1\\\",\\\"numberEnd\\\":\\\"100\\\",\\\"numberCircle\\\":\\\"1\\\",\\\"numberFormat\\\":\\\"00000\\\",\\\"isThreshold\\\":true,\\\"threshold\\\":50}\",\"id\":\"1748668117214003200\"}\n";
        SeqInstanceRule seqInstanceRule= JSONObject.parseObject(requestStr, SeqInstanceRule.class);
        seqInstanceRule.setInstanceRuleType("2");
        SeqInstanceRule query = new SeqInstanceRule();
        query.setSeqDesignId(seqInstanceRule.getSeqDesignId());
        List<SeqInstanceRule>  seqInstanceRules=new ArrayList<>();
        seqInstanceRules.add(seqInstanceRule);
        Mockito.when(seqInstanceRuleService.queryList(query)).thenReturn(seqInstanceRules);
        seqInstanceRuleController.insert(seqInstanceRule);

    }

    @org.junit.jupiter.api.Test
    void update() {
        String requestStr="{\"instanceRuleInfo\":\"{\\\"ruleType\\\":\\\"4\\\",\\\"numberStep\\\":1,\\\"numberStart\\\":\\\"1\\\",\\\"numberEnd\\\":\\\"100\\\",\\\"numberCircle\\\":\\\"1\\\",\\\"numberFormat\\\":\\\"00000\\\",\\\"isThreshold\\\":true,\\\"threshold\\\":50}\",\"id\":\"1748668117214003200\"}\n";
        SeqInstanceRule seqInstanceRule= JSONObject.parseObject(requestStr, SeqInstanceRule.class);
        seqInstanceRuleController.update(seqInstanceRule);
    }

    @org.junit.jupiter.api.Test
    void delete() {
        String id="1748668117214003200";
        String requestStr="{\"instanceRuleInfo\":\"{\\\"ruleType\\\":\\\"4\\\",\\\"numberStep\\\":1,\\\"numberStart\\\":\\\"1\\\",\\\"numberEnd\\\":\\\"100\\\",\\\"numberCircle\\\":\\\"1\\\",\\\"numberFormat\\\":\\\"00000\\\",\\\"isThreshold\\\":true,\\\"threshold\\\":50}\",\"id\":\"1748668117214003200\"}\n";
        SeqInstanceRule seqInstanceRule= JSONObject.parseObject(requestStr, SeqInstanceRule.class);
        Mockito.when(seqInstanceRuleService.getObjectById(id)).thenReturn(seqInstanceRule);

        Map<String, String> map = new HashMap<>(2);
        map.put("seqDesignId", seqInstanceRuleService.getObjectById(id).getSeqDesignId());
        List<SeqInstanceRule>  seqInstanceRules=new ArrayList<>();
        seqInstanceRules.add(seqInstanceRule);
        Mockito.when(seqInstanceRuleService.queryList(map)).thenReturn(seqInstanceRules);
        seqInstanceRuleController.delete(id);
    }
}