package com.dap.sequence.client.holder;


import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.client.core.BaseRuleHandler;
import com.dap.sequence.client.entity.SeqCurNum;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 序列存储持有对象
 * @Author: liu
 */
public class SeqHolder {

    /**
     * 序列存储map key:SeqDesignCode,不能唯一无法区分租户，所以需要SeqDesignCode+@+tenantId 进行区分
     */
    private static ConcurrentHashMap<String, BlockingQueue<Object>>                                                                     map = new ConcurrentHashMap<>();

    /**
     *  设计序列 规则存储map key:SeqDesignId->规则Id唯一  ，无需租户区别
     */
    private static ConcurrentHashMap<String, List<Rule>> rulesMap = new ConcurrentHashMap<>();

    /**
     * 序列引擎存储map  key:SeqDesignCode,不能唯一无法区分租户，所以需要SeqDesignCode+@+tenantId 进行区分
     */
    private static ConcurrentHashMap<String, AbstractSeqFlowEngine> engineMap = new ConcurrentHashMap<>();

    /**
     *序列规则处理器 key: Rule规则类型
     */
    private static ConcurrentHashMap<String, BaseRuleHandler> ruleHandlers = new ConcurrentHashMap<>();

    /**
     *序列规则处理器 key: Rule规则类型
     */
    private static ConcurrentHashMap<String, SeqCurNum> seqCurHandlers = new ConcurrentHashMap<>();

    static {

        ServiceLoader<BaseRuleHandler> baseEntitys = ServiceLoader.load(BaseRuleHandler.class);
        for (BaseRuleHandler base : baseEntitys) {
            ruleHandlers.put(base.getRuleName(), base);
        }
    }

    private SeqHolder() {

    }


    public static ConcurrentHashMap<String, BlockingQueue<Object>> getMap() {
        return map;
    }

    public static void setMap(ConcurrentHashMap<String, BlockingQueue<Object>> map) {
        SeqHolder.map = map;
    }

    public static ConcurrentHashMap<String, AbstractSeqFlowEngine> getEngineMap() {
        return engineMap;
    }

    public static void setEngineMap(ConcurrentHashMap<String, AbstractSeqFlowEngine> engineMap) {
        SeqHolder.engineMap = engineMap;
    }

    public static ConcurrentHashMap<String, List<Rule>> getRulesMap() {
        return rulesMap;
    }

    public static void setRulesMap(ConcurrentHashMap<String, List<Rule>> rulesMap) {
        SeqHolder.rulesMap = rulesMap;
    }

    public static ConcurrentHashMap<String, BaseRuleHandler> getRuleHandlers() {
        return ruleHandlers;
    }

    public static void setRuleHandlers(ConcurrentHashMap<String, BaseRuleHandler> ruleHandlers) {
        SeqHolder.ruleHandlers = ruleHandlers;
    }

    public static AbstractSeqFlowEngine getEngineBySequenceCode(String sequenceCode) {
        if (engineMap != null) {
            return engineMap.get(sequenceCode);
        }
        return null;
    }

    public static BaseRuleHandler getRuleHandlerByName(String sequenceCode) {
        return ruleHandlers.get(sequenceCode);
    }


    public static BlockingQueue<Object> getSequenceMapBySequenceCode(String sequenceCode) {
        if (map != null) {
            return map.get(sequenceCode);
        }
        return null;
    }

    public static BlockingQueue<Object> putSequenceMap(String sequenceCode, BlockingQueue<Object> blockingQueue) {
        if (map != null) {
            return map.put(sequenceCode, blockingQueue);
        }
        return null;
    }

    public static ConcurrentHashMap<String, SeqCurNum> getSeqCulHandlers() {
        return seqCurHandlers;
    }

    public static void setSeqCulHandlers(String id, SeqCurNum seqCurNum) {
        seqCurHandlers.put(id, seqCurNum);
    }

    public static SeqCurNum getSeqCulNum(String id) {
        return seqCurHandlers.get(id);
    }

    public static void clear() {
        rulesMap.clear();
    }
}
