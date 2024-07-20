package com.dap.sequence.server.config;

import com.alibaba.fastjson.JSONObject;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.*;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.factory.SeqFlowEngineFactory;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqInstanceRuleService;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.service.SeqSdkMonitorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: 随着容器启动加载规则
 * @Author: kanguangqiang
 * @Date: 2019/12/12 10:28
 * @Version: 1.0.0
 */
@Component
public class SeqPublishRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqPublishRunner.class);

    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    @Autowired
    private SeqDesignService seqDesignService;

    @Autowired
    private SeqPlatformService seqFromPlatformService;

    @Value("${gientech.dap.sequence.start.init:off}")
    private String isStartInit;

    @Autowired
    private AsyncTask asyncTask;

    @Value("${gientech.sequence.clearCacheTime:-2}")
    private String clearCacheTime;

    /**
     * 超过此时间间隔的且状态不等于运行的回收, 单位为秒
     */
    @Value("${gientech.dap.sequence.rcvWorkIdMaxIntervalTime:3600}")
    private int rcvWorkIdMaxIntervalTime;

    @Autowired
    private SeqSdkMonitorService seqSdkMonitorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        publishServer();

        //loadRules();

        if (StringUtils.equals(isStartInit, "on")) {
            cacheSequence();
        }
        //自动回收集群数量大于1024*80，且状态不处于运行中超过 3600 秒
        rcvSnowflakeWorkIdAuto();

    }

    /**
     * 周期补仓
     */
    @Scheduled(cron = "${gientech.dap.sequence.cache.async:0 0/1 * * * ?}")
    public void cacheSequence() {
        LOGGER.info("***** 开始周期补充序列仓库 *****");
        // 获取序列信息
        List<SeqDesignPo> seqDesignPoList = getSeqDesign();
        Map<String, SeqDesignPo> seqDesignPoMap = Optional.ofNullable(seqDesignPoList)
                .map(list -> list.stream()
                        // 缓存数量为0 默认为严格递增序列 服务端和客户端不缓存序号
                        .filter(seqDesignPo -> seqDesignPo.getSequenceNumber() != null && seqDesignPo.getSequenceNumber() != 0)
                        .collect(Collectors.toMap(SeqDesignPo::getId, a -> a, (k1, k2) -> k1)))
                .orElseGet(HashMap::new);
        SeqHolder.getRulesMap().forEach((k, v) -> {
            Optional.ofNullable(seqDesignPoMap.get(k)).ifPresent(seqDesignPo -> {
                if (v.stream().noneMatch(rule -> "6".equals(rule.getRuleType()))) {
                    asyncSeqTask(seqDesignPo);
                }
            });
        });
        LOGGER.info("***** 结束周期补充序列仓库 *****");
    }

    private List<SeqDesignPo> getSeqDesign() {
        List<SeqDesignPo> seqDesignPoList;
        if (seqFromPlatformService.isUsePlatform()) {
            seqDesignPoList = seqFromPlatformService.getAllSeqDesign();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("sequenceStatus", SequenceConstant.DESIGN_STATE_ON);
            seqDesignPoList = seqDesignService.queryList(map);
        }
        return seqDesignPoList;
    }

    private void asyncSeqTask(SeqDesignPo seqDesignPo) {
        try {
            SeqParamsDto seqParamsDto = seqDesignPo.fromSeqDesignPo(seqDesignPo);
            AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
            if (seqFlowEngine == null || seqFlowEngine.getSeqProducer() == null) {
                LOGGER.warn("序列[{}]引擎未加载完成,不进行补仓", seqDesignPo.getSequenceName());
                return;
            }
            SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
            asyncTask.asyncScheduledCreate(seqParamsDto, seqProducer, seqFlowEngine);
        } catch (Exception e) {
            LOGGER.error("cacheSequence {} error.msg = {}", seqDesignPo.getSequenceCode(), e.getMessage(), e);
        }
    }

    /**
     * 加载所有引用规则
     *
     * @throws SequenceException SequenceException
     */
    @Scheduled(cron = "${gientech.dap.sequence.refreshRule:0 0/1 * * * ?}")
    private void publishServer() throws SequenceException {
        loadRules();
        updateSeqHolder();
    }

    private List<SeqInstanceRule> getSeqRules() {
        List<SeqInstanceRule> seqInstanceRules;
        if (seqFromPlatformService.isUsePlatform()) {
            seqInstanceRules = seqFromPlatformService.getSeqInstanceRules(null);
        } else {
            seqInstanceRules = seqInstanceRuleService.queryList(new HashMap<>());
        }
        return seqInstanceRules;
    }

    private void loadRules() {
        List<SeqInstanceRule> collect = getSeqRules();
        List<Rule> ruleInfos = new ArrayList<>();
        for (SeqInstanceRule e : collect) {
            Rule rule = new Rule();
            rule.setRuleId(e.getId());
            rule.setRuleCode(e.getSeqDesignId());
            rule.setRuleName(e.getInstanceRuleName());
            rule.setRuleType(e.getInstanceRuleType());
            rule.setTenantId(e.getTenantId());
            switch (e.getInstanceRuleType()) {
                case "1":
                    // 固定字符串
                    StringRuleInfo stringRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), StringRuleInfo.class);
                    rule.setRuleData(stringRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "2":
                    // 枚举
                    JSONObject jsonObject = JSONObject.parseObject(e.getInstanceRuleInfo());
                    EnumRuleInfo enumRuleInfo = new EnumRuleInfo(jsonObject.getInteger("numberStep"), jsonObject.getString("initData"), jsonObject.getString("enumStore"));
                    rule.setRuleData(enumRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "3":
                    // 日期
                    DateRuleInfo dateRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), DateRuleInfo.class);
                    rule.setRuleData(dateRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "4":
                    // 数字
                    NumberRuleInfo numberRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), NumberRuleInfo.class);
                    rule.setRuleData(numberRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "5":
                    // 特殊字符
                    SpecialCharRuleInfo specialCharRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), SpecialCharRuleInfo.class);
                    rule.setRuleData(specialCharRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "6":
                    // 自选
                    OptionalRuleInfo optionalRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), OptionalRuleInfo.class);
                    rule.setRuleData(optionalRuleInfo);
                    ruleInfos.add(rule);
                    break;
                case "7":
                    // 校验位
                    CheckRuleInfo checkRuleInfo = JSONObject.parseObject(e.getInstanceRuleInfo(), CheckRuleInfo.class);
                    rule.setRuleData(checkRuleInfo);
                    ruleInfos.add(rule);
                    break;
                default:
                    // 类型不匹配
                    LOGGER.error("序列类型不匹配!");
                    break;
            }
        }
        Map<String, List<Rule>> ruleMap = ruleInfos.stream().collect(Collectors.groupingBy(Rule::getRuleCode));
        SeqHolder.getRulesMap().putAll(ruleMap);
        // 清理历史规则
        Set<String> removeKeys = new HashSet<>(SeqHolder.getRulesMap().keySet());
        removeKeys.removeAll(ruleMap.keySet());
        removeKeys.forEach(key -> SeqHolder.getRulesMap().remove(key));
    }

    public void updateSeqHolder() {
        // 加载所有的规则到序号里面
        List<SeqDesignPo> seqDesignPoList = getSeqDesign();
        if (seqDesignPoList.isEmpty()) {
            LOGGER.debug("No seqDesignPoList found.");
            return;
        }
        seqDesignPoList.forEach(seqDesignPo -> {
            try {
                SeqFlowEngineFactory.loadEngine(seqDesignPo);
                AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(SeqKeyUtils.getSeqEngineKey(seqDesignPo.getSequenceCode(), seqDesignPo.getTenantId()));
                seqFlowEngine.refresh(seqDesignPo);
            } catch (SequenceException e) {
                LOGGER.error("updateSeqHolder 更新失败", e);
            }
        });
    }

    /**
     * 回收workId， 默认半小时执行一次
     */
    @Scheduled(cron = "${gientech.dap.sequence.rcvWorkIdCron:0 0/30 * * * ?}")
    private void rcvSnowflakeWorkIdAuto(){
        try {
            LOGGER.info("seq server recycling workId start...");
            final int rcvCount = seqSdkMonitorService.rcvSnowflakeWorkId(rcvWorkIdMaxIntervalTime);
            LOGGER.info("seq server recycling workId end, and recycling total count is {}", rcvCount);
        } catch (Exception e) {
            LOGGER.error("seq server recycling failed, error message: ", e);
        }
    }

}
