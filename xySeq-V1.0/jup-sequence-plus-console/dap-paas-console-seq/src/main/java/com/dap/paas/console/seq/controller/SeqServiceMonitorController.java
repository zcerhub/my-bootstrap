package com.dap.paas.console.seq.controller;


import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dto.SeqServiceMonitorDTO;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className SeqServiceMonitorController
 * @description 序列服务监控
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列服务监控")
@RestController
@RequestMapping("/seq/service/monitor/")
@Validated
public class SeqServiceMonitorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SeqServiceMonitorController.class);

    @Autowired
    private SeqServiceClusterService seqClusterService;

    @Autowired
    private SeqServiceNodeService seqServiceNodeService;

    /**
     * 无条件查询所有集群信息
     *
     * @return 返回集群集合对象
     */
    @ApiOperation(value = "无条件查询所有集群信息")
    @PostMapping("/queryCluster")
    public Result queryCluster() {
        List<SeqServiceCluster> seqServiceClusters = seqClusterService.queryList(new SeqServiceCluster());
        return ResultUtils.success(ResultEnum.SUCCESS, seqServiceClusters);
    }


    /**
     * 根据集群唯一标识查询集群下的节点
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "根据集群唯一标识查询集群下的节点")
    @PostMapping("/queryNodes/{id}")
    public Result queryNodes(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "集群id不合法！") String id) {
        // 定义返回监控对象
        SeqServiceMonitorDTO ssmDto = new SeqServiceMonitorDTO();

        Map<String, String> map = new HashMap<>(2);
        map.put("clusterId", id);
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> map.put("tenantId", tenantId));

        // 查询node状态信息
        List<SeqServiceNode> stringStringMapList = seqServiceNodeService.serviceMonitorNumber(map);

        // 查询集群下node结合信息
        SeqServiceNode ssn = new SeqServiceNode();
        ssn.setClusterId(id);
        List<SeqServiceNode> seqServiceNodes = seqServiceNodeService.queryList(ssn);
        ssmDto.setSeqServiceNodeList(seqServiceNodes);

        int total = 0;
        if (stringStringMapList != null) {
            for (SeqServiceNode seqServiceNode : stringStringMapList) {
                // 停止
                if (!SeqConstant.SEQ_SERVER_ZERO.equals(seqServiceNode.getStatus()) && SeqConstant.SEQ_SERVER_ONE.equals(seqServiceNode.getStatus())) {
                    ssmDto.setNormalServiceTotal(seqServiceNode.getMachineId());
                }

                // 获取的是状态信息所对应的数量
                total += Integer.parseInt(seqServiceNode.getMachineId());
            }
        }
        if (total > 0) {
            int i = 0;
            ssmDto.setServiceTotal(total + "");
            //设置异常
            if (ssmDto.getNormalServiceTotal() != null) {
                i = Integer.parseInt(ssmDto.getNormalServiceTotal());
            }
            ssmDto.setNormalServiceTotal(i + "");
            ssmDto.setExceptionServiceTotal((total - i) + "");
        }
        return ResultUtils.success(ResultEnum.SUCCESS, ssmDto);
    }
}
