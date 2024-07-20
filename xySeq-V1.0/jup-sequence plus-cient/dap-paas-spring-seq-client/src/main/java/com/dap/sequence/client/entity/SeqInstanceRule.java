package com.dap.sequence.client.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

import java.util.Objects;

/**
 * @className SeqInstanceRule
 * @description 序列实例规则实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data

public class SeqInstanceRule extends BaseEntity {

    private String type = "ruleType\":\"6";

    /**
     *序列编号
     */
    private String seqDesignId;

    /**
     * 规则名称
     */
    private String instanceRuleName;

    /**
     * 规则类型
     */
    private String instanceRuleType;

    /**
     * 规则信息
     */
    private String instanceRuleInfo;

    /**
     * 排序号
     */
    private Integer sortNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeqInstanceRule)) return false;
        SeqInstanceRule that = (SeqInstanceRule) o;
        return Objects.equals(getType(), that.getType()) && Objects.equals(getSeqDesignId(), that.getSeqDesignId()) && Objects.equals(getInstanceRuleName(), that.getInstanceRuleName()) && Objects.equals(getInstanceRuleType(), that.getInstanceRuleType()) && Objects.equals(getInstanceRuleInfo(), that.getInstanceRuleInfo()) && Objects.equals(getSortNo(), that.getSortNo());
    }


}
