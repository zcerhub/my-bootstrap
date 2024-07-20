package com.dap.paas.console.seq.dto;

import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @className SeqMachineNodeDTO
 * @description 机器节点信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqMachineNodeDTO implements Serializable {

    private Machine machine;

    private List<SeqServiceNode> nodes = new ArrayList<>();

}
