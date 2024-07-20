package com.dap.sequence.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dap.sequence.api.dto.*;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.dao.SeqDesignDao;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.entity.SeqInstanceRule;
import com.dap.sequence.client.enums.RuleTypeEnum;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;
import com.dap.sequence.client.factory.SeqFlowEngineFactory;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqCacheService;
import com.dap.sequence.client.service.SeqDesignService;
import com.dap.sequence.client.service.SeqInstanceRuleService;
import com.dap.sequence.client.utils.OtherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dap.sequence.client.core.SequenceQueueFactory.*;

@Service
public class SeqCacheServiceImpl implements SeqCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeqCacheServiceImpl.class);
    @Autowired
    private SeqDesignDao seqDesignDao;
    @Autowired
    private SeqDesignService seqDesignService;
    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    @Autowired
    private SeqLocalProducerImpl seqServerProducer;

    @Autowired
    private SequenceQueueFactory sequenceQueueFactory;

    @Value("${dap.sequence.tenantId:}")
    private String tenantId;

    @Override
    public String mapToCacheAndDb(Map<String, Object> map) {
        LinkedHashMap design = (LinkedHashMap) map.get("design");
        String design1 = JSON.toJSONString(design);
        SeqDesignPo seqDesignPo =  JSON.parseObject(design1, SeqDesignPo.class);
        String designId = seqDesignPo.getId();
        List<SeqInstanceRule> rules  = JSONArray.parseArray(map.get("rule").toString(),SeqInstanceRule.class);
        //将序列规则添加至缓存
        Seq_Design_MAP.put(seqDesignPo.getSequenceCode(),seqDesignPo);
        Seq_rule_MAP.put(designId,rules);
        updateSeqHolder();
        //在缓存中生成序列
        SeqParamsDto seqParamsDto = seqDesignPo.fromSeqDesignPo(seqDesignPo);
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(seqParamsDto.clientCacheKey());

        //将序列规则添加至数据库
        SeqDesignPo oldDesign = seqDesignService.getLocalObjectByCode(seqDesignPo.getSequenceCode());
        if (oldDesign == null){
            seqDesignService.saveObject(seqDesignPo);
            }else if(oldDesign != null && !design.equals(oldDesign)){
            int a =  seqDesignService.updateObject(seqDesignPo);
        }
        rules.forEach((rule) -> {
            SeqInstanceRule oldRule = seqInstanceRuleService.getObjectById(rule.getId());
            if (oldRule == null){
                seqInstanceRuleService.saveObject(rule);
            } else if(oldRule != null && !rule.equals(oldRule)){
                int a =  seqInstanceRuleService.updateObject(rule);
                //若序列规则发生变化，则需要刷新缓存
            }
        });
        loadRules();
        if (OtherUtil.isQueueEmpty(sequenceQueue)) {
            //若缓存中不存在此规则的序列则进行生成
            LOGGER.warn("sequenceQueue 不存在从服务端获取的, seqCode:{} ", seqParamsDto.clientCacheKey());
            sequenceQueueFactory.FirstLocalCreateSeq(seqDesignPo,seqParamsDto);
        } else {
            //若存在则对备用缓存中的序列进行重新生成
            sequenceQueueFactory.refreshLocalCache(seqDesignPo,seqParamsDto);
        }

        LOGGER.info("{}序列规则与编排信息已同步", seqDesignPo.getSequenceName());
        return (seqDesignPo.getSequenceCode()+"规则已同步完成");

    }

    @Override
    public void loadRules() {
        //Seq_rule_MAP
        //查询最新的序列设计中的序列规则信息
        //Map<String, List<Rule>> currentRuleMap = new HashMap<>(Seq_rule_MAP.size());
        //查询最新的序列配置对应的规则
        List<SeqInstanceRule> instanceRules = seqInstanceRuleService.queryList(new HashMap(1));
        Map<String, List<SeqInstanceRule>> seqDesignMap = instanceRules.stream().collect(Collectors.groupingBy(SeqInstanceRule::getSeqDesignId));
        Map<String, List<Rule>> currentRuleMap = new HashMap<>(seqDesignMap.size());
        //将最新的序列设计中的序列规则进行转换
        for (Map.Entry<String, List<SeqInstanceRule>> entry : seqDesignMap.entrySet()) {
            currentRuleMap.put(entry.getKey(), convert2Rules(entry.getValue()));
        }
        //将转换后的序列定义中的序列规则放入到缓存中
        SeqHolder.getRulesMap().putAll(currentRuleMap);
        //key为SeqDesignId,计算出带清理的key,然后从缓存中rulesMap中remove
        Set<String> allKeys = new HashSet<>(SeqHolder.getRulesMap().keySet());
        allKeys.removeAll(currentRuleMap.keySet());
        allKeys.forEach(key -> SeqHolder.getRulesMap().remove(key));
    }

    @Override
    public void updateSeqHolder() {
        if (Seq_Design_MAP.isEmpty()) {
            LOGGER.debug("No seqDesignPoList found.");
            return;
        }
        Seq_Design_MAP.forEach((seqCode,seqDesignPo) ->
        {
            try {
                SeqFlowEngineFactory.loadEngine(seqDesignPo);
                AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(SeqKeyUtils.getSeqEngineKey(seqDesignPo.getSequenceCode(), seqDesignPo.getTenantId()));
                seqFlowEngine.refresh(seqDesignPo);
            } catch (SequenceException e) {
                LOGGER.error("updateSeqHolder 更新失败", e);
            }
        });
    }

    private List<Rule> convert2Rules(List<SeqInstanceRule> instanceRules) {
        List<Rule> rules = new ArrayList<>();
        for (SeqInstanceRule instRule : instanceRules) {
            Rule rule = new Rule();
            rule.setRuleId(instRule.getId());
            rule.setRuleCode(instRule.getSeqDesignId());
            rule.setRuleName(instRule.getInstanceRuleName());
            rule.setRuleType(instRule.getInstanceRuleType());
            rule.setTenantId(instRule.getTenantId());
            switch (RuleTypeEnum.getRuleTypeEnum(instRule.getInstanceRuleType())) {
                case FIXED_STR_RULE:
                    // 固定字符串
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), StringRuleInfo.class));
                    rules.add(rule);
                    break;
                case ENUM_RULE:
                    // 枚举
                    JSONObject jsonObject = JSON.parseObject(instRule.getInstanceRuleInfo());
                    EnumRuleInfo enumRuleInfo = new EnumRuleInfo(jsonObject.getInteger("numberStep"), jsonObject.getString("initData"), jsonObject.getString("enumStore"));
                    rule.setRuleData(enumRuleInfo);
                    rules.add(rule);
                    break;
                case DATE_RULE:
                    // 日期
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), DateRuleInfo.class));
                    rules.add(rule);
                    break;
                case NUM_RULE:
                    // 数字
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), NumberRuleInfo.class));
                    rules.add(rule);
                    break;
                case SPECIAL_STR_RULE:
                    // 特殊字符
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), SpecialCharRuleInfo.class));
                    rules.add(rule);
                    break;
                case SELF_OPTION_RULE:
                    // 自选
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), OptionalRuleInfo.class));
                    rules.add(rule);
                    break;
                case VALIDATE_RULE:
                    // 校验位
                    rule.setRuleData(convert2RuleInfo(instRule.getInstanceRuleInfo(), CheckRuleInfo.class));
                    rules.add(rule);
                    break;
                default:
                    // 类型不匹配
                    LOGGER.error("序列类型不匹配!");
                    break;
            }
        }
        return rules;
    }
    static <T> T convert2RuleInfo(String instRuleJson, Class<T> t) {
        return JSONObject.parseObject(instRuleJson, t);
    }
}

