package com.dap.paas.console.seq.controller.v1;


import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.utils.SocketUtils;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import com.dap.paas.console.seq.service.SeqSdkMonitorService;
import com.dap.paas.console.seq.util.ConstantUtil;
import com.dap.paas.console.seq.util.SequenceUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @className SeqSdkMonitorController
 * @description 序列SDK监控
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Api(tags = "序列SDK监控")
@RestController
@RequestMapping("/seq/sdk/monitor/")
@Validated
public class SeqSdkMonitorController {

    private final static Logger LOG = LoggerFactory.getLogger(SeqSdkMonitorController.class);

    protected static final int TIMEOUT = 180;

    private final ExecutorService executorService = new ThreadPoolExecutor(50, 50, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(500),
            new ThreadFactoryBuilder().setNameFormat("SeqSdkMonitor-pool-thread-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private SeqSdkMonitorService seqSdkMonitorService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据应用唯一标识获取对应SDK
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "根据应用唯一标识获取对应SDK")
    @PostMapping("/queryList/{id}")
    public Result queryList(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "标识id不合法！") String id) {
        SeqSdkMonitor ssm = new SeqSdkMonitor();
        ssm.setServiceId(id);
        ssm.setStatus(ConstantUtil.MONITOR_STATUS_DISCARDED);
//        获取所有非废弃状态的客户端
        List<SeqSdkMonitor> seqSdkMonitors = seqSdkMonitorService.queryList(ssm);

        if (CollectionUtils.isEmpty(seqSdkMonitors)) {
            return ResultUtils.error("当前集群没有适配的客户端信息");
        }

        List<Future<Boolean>> resultFutureList = new ArrayList<>(seqSdkMonitors.size());
        for (SeqSdkMonitor node : seqSdkMonitors) {
            resultFutureList.add(executorService.submit(() -> {
                if (SocketUtils.isRunning(node.getHostIp(), Integer.parseInt(node.getPort()))) {
                    node.setStatus(SeqConstant.SEQ_NODE_STATUS_RUN);
                } else {
                    node.setStatus(SeqConstant.SEQ_NODE_STATUS_STOP);
                }
                return true;
            }));
        }
        for (Future<Boolean> resultFuture : resultFutureList) {
            try {
                resultFuture.get(TIMEOUT, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOG.error("redis start fail " + e.getMessage());
            }
        }
        return ResultUtils.success(ResultEnum.SUCCESS, seqSdkMonitors);
    }

    /**
     * sdk缓冲区
     *
     * @param id id
     * @return Result
     */
    @ApiOperation(value = "sdk缓冲区")
    @PostMapping("/getSeq/buffer/{id}")
    public Result getSeqBuffer(@PathVariable("id") @Pattern(regexp = "^\\d{1,32}$", message = "标识id不合法！") String id) {
        SeqSdkMonitor seqSdkMonitor = seqSdkMonitorService.getObjectById(id);
        if (Objects.isNull(seqSdkMonitor)) {
            return ResultUtils.error("查不到对应的SDK数据");
        }
        String url = SeqConstant.SEQ_SERVER_SWITCH_HTTP + seqSdkMonitor.getHostIp() + ":" + seqSdkMonitor.getPort();
        if (StringUtils.isNotBlank(seqSdkMonitor.getContextPath())) {
            url += seqSdkMonitor.getContextPath();
        }
        url += SeqConstant.SEQ_SDK_SEQ_BUFFER_URL;
        Map<String, String> reqMap = new HashMap<>(5);
        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(url, reqMap, Result.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return ResultUtils.success(responseEntity.getBody().getData());
        }
        return ResultUtils.error("请求序列客户端应用失败：" + responseEntity.getStatusCodeValue());
    }

    /**
     * 查询雪花算法workID使用列表
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("查询雪花算法workID使用列表")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<SeqSdkMonitor> param) {
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(tenantId -> param.getRequestObject().setTenantId(tenantId));
        Page monitorList = seqSdkMonitorService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
        return ResultUtils.success(ResultEnum.SUCCESS, monitorList);
    }

    /**
     * 回收workId
     *
     * @param param param
     * @return Result
     */
    @ApiOperation("workId回收接口")
    @PostMapping("/rcvWorkId")
    public Result rcvWorkId(@RequestBody @Validated SeqSdkMonitor param) {
        if(StringUtils.isBlank(param.getId())){
            return ResultUtils.error(ResultEnum.SEQ_SNOW_INFO_EMPTY);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, seqSdkMonitorService.rcvWorIdByClient(param));
    }
}
