package com.dap.paas.console.seq.dto;

import com.dap.paas.console.seq.entity.SeqServiceNode;
import lombok.Data;

/**
 * @className SeqServiceNodeVo
 * @description 服务节点
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqServiceNodeVo extends SeqServiceNode {

    private String ip;

    private String unitName;
}
