package com.dap.paas.console.seq.holder;


import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.rules.CheckRuleHandler;
import com.dap.paas.console.seq.rules.DateRuleHandler;
import com.dap.paas.console.seq.rules.EnumRuleHandler;
import com.dap.paas.console.seq.rules.NumberRuleHandler;
import com.dap.paas.console.seq.rules.OptionalRuleHandler;
import com.dap.paas.console.seq.rules.SpecialCharRuleHandler;
import com.dap.paas.console.seq.rules.StringRuleHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className SeqHolder
 * @description 序列Holder
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqHolder {

    /**
     * 序列存储map
     */
    private static ConcurrentHashMap<String, BlockingQueue<Object>> map = new ConcurrentHashMap<>();

    /**
     * 序列规则处理器
     */
    private static ConcurrentHashMap<String, BaseRuleHandler> ruleHandlers = new ConcurrentHashMap<>();

    static {
        // init rule handler
        DateRuleHandler dateRuleHandler = new DateRuleHandler();
        EnumRuleHandler enumRuleHandler = new EnumRuleHandler();
        NumberRuleHandler numberRuleHandler = new NumberRuleHandler();
        StringRuleHandler stringRuleHandler = new StringRuleHandler();
        SpecialCharRuleHandler specialCharRuleHandler = new SpecialCharRuleHandler();
        OptionalRuleHandler optionalRuleHandler = new OptionalRuleHandler();
        CheckRuleHandler checkRuleHandler = new CheckRuleHandler();
        ruleHandlers.put(dateRuleHandler.getRuleName(), dateRuleHandler);
        ruleHandlers.put(enumRuleHandler.getRuleName(), enumRuleHandler);
        ruleHandlers.put(numberRuleHandler.getRuleName(), numberRuleHandler);
        ruleHandlers.put(stringRuleHandler.getRuleName(), stringRuleHandler);
        ruleHandlers.put(specialCharRuleHandler.getRuleName(), specialCharRuleHandler);
        ruleHandlers.put(optionalRuleHandler.getRuleName(), optionalRuleHandler);
        ruleHandlers.put(checkRuleHandler.getRuleName(), checkRuleHandler);
    }

    private SeqHolder() {

    }

    public static ConcurrentHashMap<String, BlockingQueue<Object>> getMap() {
        return map;
    }

    public static void setMap(ConcurrentHashMap<String, BlockingQueue<Object>> map) {
        SeqHolder.map = map;
    }

    public static BaseRuleHandler getRuleHandlerByName(String sequenceCode) {
        if (ruleHandlers != null) {
            return ruleHandlers.get(sequenceCode);
        }
        return null;
    }


    public static BlockingQueue getSequenceMapBySequenceCode(String sequenceCode) {
        if (map != null) {
            return map.get(sequenceCode);
        }
        return null;
    }

    public static BlockingQueue putSequenceMap(String sequenceCode, BlockingQueue blockingQueue) {
        if (map != null) {
            return map.put(sequenceCode, blockingQueue);
        }
        return null;
    }


}
