package com.dap.paas.console.seq.dto;


import com.dap.paas.console.seq.check.ValidGroup;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @className SeqInstallationDTO
 * @description 部署信息
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqInstallationDTO {

    @NotBlank(message = "集群id不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class})
    @Size(min = 1, max = 32, message = "集群id长度范围为1~255", groups = {ValidGroup.Valid.ClusterEnvCheck.class})
    private String clusterId;

    @NotEmpty(message = "集群节点不能为空！", groups = {ValidGroup.Valid.ClusterEnvCheck.class})
    @Valid
    private List<SeqServiceNode> seqServiceNodeList = new ArrayList<>();

}
