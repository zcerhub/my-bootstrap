package com.dap.paas.console.seq.controller;

import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.common.util.JsonUtil;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.SeqUseCondition;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import com.dap.paas.console.seq.service.SeqUseConditionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className SeqPlatfromToServer
 * @description TODO
 * @author renle
 * @date 2023/12/13 18:54:16 
 * @version: V23.06
 */

@Api(tags = "序列设计管理-服务端交互")
@RestController
@RequestMapping("/v1/seq/server")
@Validated
public class SeqPlatformToServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqDesignController.class);

    @Autowired
    private SeqDesignService seqDesignService;

    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    @Autowired
    private SeqUseConditionService seqUseConditionService;

    /**
     * 序列列表查询
     *
     * @param status status
     * @return Result
     */
    @ApiOperation("序列设计管理列表")
    @GetMapping("/seq-list")
    public ResultResponse querySeq(HttpServletRequest req, @RequestParam String status) {
        LOGGER.info("节点：{} 开始刷新序列配置信息", req.getHeader("hostIp"));
        Map<String, String> map = new HashMap<>();
        map.put("sequenceStatus", status);
        map.put("tenantId", req.getHeader("tenantId"));
        ResultResponse<List<SeqDesignPo>> listResultResponse = seqDesignService.queryList(map);
        List<SeqDesignPo> seqDesignList = listResultResponse.getData();
        return ResultResponse.success(seqDesignList);
    }

    /**
     * 序列列表查询
     *
     * @param seqDesignId seqDesignId
     * @return Result
     */
    @ApiOperation("序列设计-规则列表")
    @GetMapping("/seq-rules")
    public ResultResponse querySeqRules(HttpServletRequest req, @RequestParam(required = false) String seqDesignId) {
        LOGGER.info("节点：{} 开始刷新序列规则信息", req.getHeader("hostIp"));
        Map<String, String> map = new HashMap<>();
        map.put("seqDesignId", seqDesignId);
        map.put("tenantId", req.getHeader("tenantId"));
        ResultResponse<List<SeqInstanceRule>> listResultResponse = seqInstanceRuleService.queryList(map);
        List<SeqInstanceRule> seqDesignList = listResultResponse.getData();
        return ResultResponse.success(seqDesignList);
    }

    /**
     * 序列列表查询
     *
     * @param seqUseCondition seqUseCondition
     * @return Result
     */
    @ApiOperation("序列设计-规则列表")
    @PostMapping("/seq-use")
    public ResultResponse saveUseCondition(HttpServletRequest req, @RequestBody SeqUseCondition seqUseCondition) {
        LOGGER.info("节点：{} 上报序列使用信息：{}", req.getHeader("hostIp"), JsonUtil.obj2String(seqUseCondition));
        Integer record = seqUseConditionService.saveObject(seqUseCondition);
        return ResultResponse.success("使用情况上报成功");
    }
}
