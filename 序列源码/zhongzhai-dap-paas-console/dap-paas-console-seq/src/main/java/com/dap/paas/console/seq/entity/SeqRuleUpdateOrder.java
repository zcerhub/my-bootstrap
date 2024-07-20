package com.dap.paas.console.seq.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @className SeqRuleUpdateOrder
 * @description 序列规则顺序更新
 * @author renle
 * @date 2023/12/20 10:10:00 
 * @version: V23.06
 */

@Data
public class SeqRuleUpdateOrder {

    @NotEmpty(message = "规则列表不能为空！")
    private List<SeqInstanceRule> instanceRuleList;
}
