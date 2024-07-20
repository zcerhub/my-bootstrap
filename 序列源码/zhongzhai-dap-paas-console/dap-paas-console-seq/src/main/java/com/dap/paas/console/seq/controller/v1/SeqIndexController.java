package com.dap.paas.console.seq.controller.v1;


import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.seq.dto.SeqServiceStatusDTO;
import com.dap.paas.console.seq.entity.SeqIndexInfo;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqSdkMonitorService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.service.SeqTemplateRuleService;
import com.dap.paas.console.seq.util.SequenceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className SeqIndxController
 * @description 序列概览界面
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列概览界面")
@RestController
@RequestMapping("/seq/index/")
public class SeqIndexController {

    @Autowired
    private SeqDesignService seqDesignSrevice;

    @Autowired
    private SeqSdkMonitorService seqSdkMonitorService;

    @Autowired
    private SeqServiceNodeService seqServiceNodeService;

    @Autowired
    private SeqTemplateRuleService seqTemplateRuleService;

    /**
     * 查询首页面的信息
     * @return 返回首页面集合信息
     */
    @ApiOperation(value = "查询首页面的信息")
    @PostMapping("/queryAll")
    public Result queryCluster() {
        SeqIndexInfo seqIndexInfo = new SeqIndexInfo();

        // 系统数量
        Integer integer = seqDesignSrevice.queryApplicationTotal();
        seqIndexInfo.setOsTotal(Integer.toString(integer != null ? integer : 0));

        // 规则数量
        Integer integer2 = seqTemplateRuleService.queryTempTotal();
        seqIndexInfo.setRulesTotal(Integer.toString(integer2 != null ? integer2 : 0));

        // 设计器数量
        Integer integer1 = seqDesignSrevice.queryDesignTotal();
        seqIndexInfo.setTempTotal(Integer.toString(integer1 != null ? integer1 : 0));

        // SDK信息
        // 正常
        Integer integer3 = seqSdkMonitorService.queryNormalTotal();
        seqIndexInfo.setSdkNormalTotal(Integer.toString(integer3 != null ? integer3 : 0));

        // 异常
        Integer integer4 = seqSdkMonitorService.queryExceptionTotal();
        seqIndexInfo.setSdkExceptionTotal(Integer.toString(integer4 != null ? integer4 : 0));

        // 列表
        seqIndexInfo.setSeqSdkMonitorList(seqSdkMonitorService.queryList(new SeqSdkMonitor()));

        // 服务信息
        // 服务状态信息
        SeqServiceStatusDTO seqServiceStatusDTO = new SeqServiceStatusDTO();
        Map<String, String> map = new HashMap<>();
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> map.put("tenantId", tenantId));
        List<SeqServiceNode> seqServiceNodes = seqServiceNodeService.serviceMonitorNumber(map);
        if (seqServiceNodes != null) {
            int total = 0;
            for (SeqServiceNode seqServiceNode : seqServiceNodes) {
                switch (seqServiceNode.getStatus()) {
                    case "0":
                        break;
                    case "1":
                        seqServiceStatusDTO.setNormalServiceTotal(seqServiceNode.getMachineId());
                        break;
                    case "2":
                        seqServiceStatusDTO.setExceptionServiceTotal(seqServiceNode.getMachineId());
                        break;
                    default:
                        total = +Integer.parseInt(seqServiceNode.getMachineId());
                        break;
                }
            }
            seqServiceStatusDTO.setServiceTotal(Integer.toString(total));
        }
        seqIndexInfo.setSeqServiceStatusDTO(seqServiceStatusDTO);

        // 服务列表
        List<SeqServiceNode> seqServiceNodes1 = seqServiceNodeService.queryList(new SeqServiceNode());
        seqIndexInfo.setSeqServiceNodeList(seqServiceNodes1);
        return ResultUtils.success(ResultEnum.SUCCESS, seqIndexInfo);
    }
}
