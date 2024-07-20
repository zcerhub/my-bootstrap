package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @className SeqCurNum
 * @description 序列计数实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqCurNum extends BaseEntity {

    /**
     * 关联id（序列设计主表id）
     */
    @NotBlank(message = "序列规则ID不能为空")
    @Size(min = 1, max = 32, message = "序列规则长度范围为1~32")
    private String seqInstanceRuleId;

    /**
     *日期
     */
    @NotBlank(message = "序列生成日期不能为空")
    @Size(min = 1, max = 32, message = "序列生成日期长度范围为1~32")
    private String inDay;

    /**
     * 执行次数
     */
    @NotNull(message = "序列生成次数不能为空")
    @Range(min = 0, max = Integer.MAX_VALUE, message = "序列生成次数不合法")
    private Integer seqLock;

    /**
     * 当前号段
     */
    private String curVal;

    /**
     * 序列名称
     */
    private String instanceRuleName;
}
