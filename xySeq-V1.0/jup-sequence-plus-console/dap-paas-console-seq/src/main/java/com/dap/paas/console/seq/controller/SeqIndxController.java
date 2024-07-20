package com.dap.paas.console.seq.controller;


import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.seq.dto.SeqServiceStatusDTO;
import com.dap.paas.console.seq.entity.SeqIndexInfo;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqSdkMonitorService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.service.SeqTemplateRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 序列概览界面
 */

@Api(tags = "序列概览界面")
@RestController
@RequestMapping("/seq/index/")
public class SeqIndxController {

    private final static Logger log = LoggerFactory.getLogger(SeqIndxController.class);

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
        try {
            SeqIndexInfo seqIndexInfo = new SeqIndexInfo();
            //系统数量
            Integer integer = seqDesignSrevice.queryApplicationTotal();
            seqIndexInfo.setOsTotal(Integer.toString(integer != null ? integer:0));

            //规则数量
            Integer integer2 = seqTemplateRuleService.queryTempTotal();
            seqIndexInfo.setRulesTotal(Integer.toString(integer2 != null ? integer2:0));

            //设计器数量
            Integer integer1 = seqDesignSrevice.queryDesignTotal();
            seqIndexInfo.setTempTotal(Integer.toString(integer1 != null ? integer1:0));

            //SDK信息
            //正常
            Integer integer3 = seqSdkMonitorService.queryNormalTotal();
            seqIndexInfo.setSdkNormalTotal(Integer.toString(integer3 != null ? integer3:0));
            //异常
            Integer integer4 = seqSdkMonitorService.queryExceptionTotal();
            seqIndexInfo.setSdkExceptionTotal(Integer.toString(integer4 != null ? integer4:0));
            //列表
            seqIndexInfo.setSeqSdkMonitorList(seqSdkMonitorService.queryList(new SeqSdkMonitor()));

            //服务信息
            //服务状态信息
            SeqServiceStatusDTO seqServiceStatusDTO = new SeqServiceStatusDTO();
            Map map = new HashMap();
            map.put("tenantId", UserUtil.getUser().getTenantId());
            List<SeqServiceNode> seqServiceNodes = seqServiceNodeService.serviceMonitorNumber(map);
            if (seqServiceNodes != null) {
                int  total = 0 ;
                for (int i = 0; i < seqServiceNodes.size(); i++) {
                    SeqServiceNode seqServiceNode = seqServiceNodes.get(i);
                    if("0".equals(seqServiceNode.getStatus())){

                    }else if("1".equals(seqServiceNode.getStatus())){
                        seqServiceStatusDTO.setNormalServiceTotal(seqServiceNode.getMachineId());
                    }else if("2".equals(seqServiceNode.getStatus())){
                        seqServiceStatusDTO.setExceptionServiceTotal(seqServiceNode.getMachineId());
                    }else {
                        total = + Integer.parseInt(
                                seqServiceNode.getMachineId() == null && "".equals(seqServiceNode.getMachineId() )
                                ? "0":seqServiceNode.getMachineId()
                        )  ;
                    }
                }
                seqServiceStatusDTO.setServiceTotal(Integer.toString(total));
            }
            seqIndexInfo.setSeqServiceStatusDTO(seqServiceStatusDTO);

            //服务列表
            List<SeqServiceNode> seqServiceNodes1 = seqServiceNodeService.queryList(new SeqServiceNode());
            seqIndexInfo.setSeqServiceNodeList(seqServiceNodes1);

            return ResultUtils.success(ResultEnum.SUCCESS, seqIndexInfo);
        } catch (Exception  e) {
            log.error("无条件查询所有集群信息失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
