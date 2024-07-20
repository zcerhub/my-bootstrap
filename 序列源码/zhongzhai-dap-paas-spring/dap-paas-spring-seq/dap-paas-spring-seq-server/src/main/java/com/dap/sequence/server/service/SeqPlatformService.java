package com.dap.sequence.server.service;

import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqInstanceRule;
import com.dap.sequence.server.entity.SeqUseCondition;

import java.util.List;

/**
 * @className SeqPlatformService
 * @description TODO
 * @author renle
 * @date 2023/12/13 17:04:13 
 * @version: V23.06
 */
public interface SeqPlatformService {

    /**
     * 是否启用管控
     *
     * @return boolean
     */
    boolean isUsePlatform();

    /**
     * 获取全量序列
     *
     * @return List
     */
    List<SeqDesignPo> getAllSeqDesign();

    /**
     * 获取序列规则配置
     *
     * @return List
     */
    List<SeqInstanceRule> getSeqInstanceRules(String seqDesignId);

    /**
     * 上报使用记录
     *
     * @param seqUseCondition seqUseCondition
     */
    void saveUseCondition(SeqUseCondition seqUseCondition);
}
