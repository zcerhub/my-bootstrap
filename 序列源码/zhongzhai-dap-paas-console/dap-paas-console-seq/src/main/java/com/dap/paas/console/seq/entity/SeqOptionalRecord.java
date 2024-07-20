package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @className SeqOptionalRecord
 * @description 自选实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqOptionalRecord extends BaseEntity {

    private static final long serialVersionUID = 199556413971963378L;

    /**
     * 序列编号
     */
    @NotBlank(message = "序列编号不能为空！")
    @Size(min = 1, max = 64, message = "序列编号长度范围为1~64")
    private String seqCode;

    /**
     * 补位值
     */
    private Long paddindValue;

    /**
     * 客户输入参数（如生日、888）
     */
    @NotBlank(message = "自选参数不能为空！")
    @Size(min = 1, max = 64, message = "自选参数长度范围为1~64")
    private String seqValue;

    /**
     * 序列规则实例ID
     */
    private String instanceRuleId;

    /**
     * 自选状态1.已使用   2.已回收
     */
    private String optionalStatus;
}

