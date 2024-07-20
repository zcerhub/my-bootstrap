package com.dap.sequence.client.shutdown;


import com.alibaba.fastjson.JSONArray;
import com.dap.sequence.api.commons.Result;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.SequenceUtils;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.entity.SeqInstanceRule;
import com.dap.sequence.client.service.SeqCacheService;
import com.dap.sequence.client.service.SeqDesignService;
import com.dap.sequence.client.service.SeqInstanceRuleService;
import com.dap.sequence.client.utils.NetworkUtil;
import com.dap.sequence.client.utils.OtherUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.dap.sequence.api.constant.SequenceConstant.DATE_RULE;
import static com.dap.sequence.api.constant.SequenceConstant.DAY_BASE;
import static com.dap.sequence.client.core.SequenceQueueFactory.*;

/**
 * @author jiangyong
 * @className ShutdownApplication
 * @description 容器启动顶缓存序号
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */

@Component
public class ApplicationPublishRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationPublishRunner.class);


    @Value("${dap.sequence.isStartInit:true}")
    private String isStartInit;
    @Value("${dap.sequence.consoleUrl:}")
    private String consoleUrl;
    /**
     * 需要预缓存的序列编码
     */
    @Value("${dap.sequence.cacheSeqCode:}")
    private String cacheSeqCode;

    private static final String ON = "true";
    private static final int INT_10 = 10;
    private static final int INT_100 = 100;

    private static final int RETRY_NUM = 3;


    /**
     * 线程名称
     */
    private static ThreadFactory nameFactory = new ThreadFactoryBuilder().setNameFormat("thread-cacheseg-%d").build();

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(INT_10, INT_100, INT_10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20), nameFactory);

    @Autowired
    private SequenceQueueFactory sequenceQueueFactory;

    @Autowired
    private SeqConsumer seqConsumer;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SeqDesignService seqDesignService;
    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    @Autowired
    private SeqCacheService seqCacheService;

    /**
     * 容器启动预缓存序号
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        if (StringUtils.equals(isStartInit, ON)) {
            int num = 0;
            while (num < RETRY_NUM) {
                try {
                    num++;
                    cacheSequence();
                    break;
                } catch (Exception e) {
                    // 预缓存序号异常，则停止服务
                    logger.error("预缓存序号异常：", e);
                    if (num == RETRY_NUM) {
                        logger.error("预缓存序号异常：" + e);
                        System.exit(1);
                    }
                }
            }
        }
    }

    /**
     * 容器启动预缓存序号
     */
    private void cacheSequence() {
        logger.debug("***** 开始顶緩存序号 *****");
        //获取序列信息(包含序列基本信息和规则信息)
        List<SeqObj> seObjList = seqConsumer.getAllSeqDesignObj();
        if (CollectionUtils.isEmpty(seObjList)) {
            return;
        }
        List<String> dateRuleSeqCode = new ArrayList<>();
        seObjList.forEach(seqObj -> seqObj.getRuleInfos().stream()
                .filter(rule -> Objects.equals(rule.getRuleType(), DATE_RULE))
                .findFirst().ifPresent(dateRule -> dateRuleSeqCode.add(seqObj.getSequenceCode())));
        //需要缓存的序号分为2大类
        //1.普通序号(包含替换符和不包含替换符)
        //2.周期序号
        //这2类序号调用的入口都是一个
        executor.execute(() -> {
            try {
                //缓存序列配置
                seObjList.forEach(seqObj -> SequenceQueueFactory.SEQUENCE_DESIGN_MAP.put(seqObj.getSequenceCode(), seqObj));
                //缓存序列
                if (StringUtils.isNotBlank(cacheSeqCode)) {
                    List<String> seqCodeList = Arrays.asList(cacheSeqCode.split(","));
                    seqCodeList.forEach(seqCode -> {
                        SeqParamsDto seqParamsDto = new SeqParamsDto();
                        seqParamsDto.setSeqCode(seqCode);
                        seqParamsDto.setDay(dateRuleSeqCode.contains(seqCode) ? SequenceUtils.getSeqDay(SequenceQueueFactory.SEQUENCE_DESIGN_MAP.get(seqCode).getCallbackMode(), new Date()) : DAY_BASE);
                        getSeqFromServer(seqParamsDto);
                    });
                }
            } catch (Exception e) {
                logger.error("预缓存序号异常:", e);
            }
        });
        logger.debug("***** 结束预缓存序号 *****");


    }

    /**
     * 获取远程服务端一组序列并放入緩存
     *
     * @param seqParamsDto seqParamsDto
     */
    private void getSeqFromServer(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        logger.debug("开始远程获取序列号... ,seqCode = {}",clientCacheKey);
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
        sequenceQueueFactory.getSeqArrayFromServer(seqParamsDto, sequenceQueue);
    }

    /**
     * 定时拉取更新序列规则信息及编排信息
     *
     *
     */
//    @Scheduled(cron = "${dap.sequence.cache.async:0/1 * * * * ?}")
    @Scheduled(cron = "${dap.sequence.cache.async:0 0/1 * * * ?}")
    public void getRulesFromConsole() throws SequenceException {
        logger.info("***** 开始周期获取序列规则及编排信息 *****");
        // 获取规则
        List<SeqDesignPo> LocalDesigns= seqDesignService.selectCode();
        List<String> codes = new ArrayList<>();
        LocalDesigns.forEach((design) -> {
            codes.add(design.getSequenceCode());
        });
        // 获取新的规则信息及编排信息
        Result<?> result  =  restTemplate.postForObject(NetworkUtil.AssemblyHttp(consoleUrl, OtherUtil.REFRESH_RULES), codes, Result.class);
        Map<String,Object> newValue = (Map<String, Object>) result.getData();
        List<SeqInstanceRule> rules  = JSONArray.parseArray(newValue.get("rule").toString(),SeqInstanceRule.class);
        List<SeqDesignPo> designs = JSONArray.parseArray(newValue.get("design").toString(),SeqDesignPo.class);
        //对编排信息与规则信息进行更新
        try{
            contrast(rules,designs);
        }catch (Exception e) {
            e.printStackTrace();
        }


        logger.info("***** 周期获取序列规则及编排信息结束 *****");
    }
    public void contrast(List<SeqInstanceRule> rules,List<SeqDesignPo> designs){
        designs.forEach((design) -> {
            //SeqDesignPo oldDesign = seqDesignService.getLocalObjectByCode(design.getSequenceCode());
            List<SeqInstanceRule> designRules = new ArrayList<>();
            //根据规则id获取编排规则
            rules.forEach((rule) -> {
                if (rule.getSeqDesignId().equals(design.getId())){
                    designRules.add(rule);
                }
            });
            //将获取的编排信息与内存中的编排信息进行对比，不一样则进行库与内存的更新
            if (Seq_rule_MAP.get(design.getId()) == null || !designRules.equals(Seq_rule_MAP.get(design.getId()))){
                designRules.forEach((rule) ->{
                    SeqInstanceRule oldRule = seqInstanceRuleService.getObjectById(rule.getId());
                    if (oldRule == null){
                        seqInstanceRuleService.saveObject(rule);
                    }else if(!rule.equals(oldRule)){
                        seqInstanceRuleService.updateObject(rule);
                    }
                });
                Seq_rule_MAP.put(design.getId(),designRules);
                //加载规则
                seqCacheService.loadRules();
                //将获取到的规则信息与本地缓存中的规则信息对比，不一样则进行库与内存的更新
                if(Seq_Design_MAP.get(design.getSequenceCode()) == null || !design.equals(Seq_Design_MAP.get(design.getSequenceCode()))){
                    SeqDesignPo oldDesign = seqDesignService.getLocalObjectByCode(design.getSequenceCode());
                    if (oldDesign == null){
                        seqDesignService.saveObject(design);
                    }else if(!design.equals(oldDesign)){
                        int a =  seqDesignService.updateObject(design);
                    }
                    seqDesignService.updateObject(design);
                    Seq_Design_MAP.put(design.getSequenceCode(),design);
                    //加载所有的规则到序号里面
                    seqCacheService.updateSeqHolder();
                }
                //判断缓存中是都存在序列，不存在则生成，存在则更新备用缓存
                SeqParamsDto seqParamsDto = design.fromSeqDesignPo(design);
                SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(seqParamsDto.clientCacheKey());
                if (OtherUtil.isQueueEmpty(sequenceQueue)) {
                    //若缓存中不存在此规则的序列则进行生成
                    logger.warn("sequenceQueue 不存在本地生成, seqCode:{} ", seqParamsDto.clientCacheKey());
                    sequenceQueueFactory.FirstLocalCreateSeq(design,seqParamsDto);
                } else {
                    //若存在则对备用缓存中的序列进行重新生成
                    //return getSequenceFromLocalCache(seqParamsDto, sequenceQueue);
                    sequenceQueueFactory. refreshLocalCache(design,seqParamsDto);
                }
            }

        });
    }
}


