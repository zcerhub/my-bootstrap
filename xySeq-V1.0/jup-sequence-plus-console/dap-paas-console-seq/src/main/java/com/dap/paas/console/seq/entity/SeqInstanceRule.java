package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import com.dap.paas.console.seq.check.LegalValue;
import com.dap.paas.console.seq.check.MultiFieldAssociation;
import com.dap.paas.console.seq.check.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "序列id不能为空！", groups = {ValidGroup.Valid.Create.class})
    @Size(min = 1, max = 32, message = "序列id长度范围为1~32", groups = {ValidGroup.Valid.Create.class})
    private String seqDesignId;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空！", groups = {ValidGroup.Valid.Create.class})
    @Size(min = 1, max = 100, message = "规则名称长度范围为1~100", groups = {ValidGroup.Valid.Create.class})
    private String instanceRuleName;

    /**
     * 规则类型
     */
    @LegalValue(values = {"1", "2", "3", "4", "5", "6", "7"}, message = "规则类型非法", groups = {ValidGroup.Valid.Create.class})
    private String instanceRuleType;

    /**
     * 规则信息
     */
    @NotBlank(message = "规则信息不能为空！", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    @Size(min = 1, max = 4000, message = "规则信息长度范围为1~4000", groups = {ValidGroup.Valid.Create.class, ValidGroup.Valid.Update.class})
    private String instanceRuleInfo;

    /**
     * 排序号
     */
    @NotNull(message = "排序后不能为空！", groups = {ValidGroup.Valid.Create.class})
    @Range(min = 1, max = Integer.MAX_VALUE, message = "排序号非法", groups = {ValidGroup.Valid.Create.class})
    private Integer sortNo;
}
