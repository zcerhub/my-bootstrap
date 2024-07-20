package com.dap.paas.console.seq.feign;

import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;

/**
 * 序列sdk监控
 *
 * @author yzp
 * @date 2024/7/5 0005 16:17
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqSdkMonitorFeignApi", url = "${dap.sequence.urls:}")
public interface SeqSdkMonitorFeignApi {

    /**
     * 根据状态查询总数
     *
     * @param map
     * @return
     */
    @PostMapping("/v1/seq/sdk/monitor/queryStatusTotal")
    ResultResponse<Integer> queryStatusTotal(@RequestBody  HashMap<String, String> map);

    /**
     * 查询无权限列表
     *
     * @return
     */
    @PostMapping("/v1/seq/sdk/monitor/queryListNoPermission")
    ResultResponse<List<SeqSdkMonitor>> queryListNoPermission();

    /**
     * 修改为无权限
     *
     * @param seqSdkMonitor
     * @return
     */
    @PostMapping("/v1/seq/sdk/monitor/updateNoPermission")
    ResultResponse<Integer> updateNoPermission(@RequestBody SeqSdkMonitor seqSdkMonitor);

    /**
     * 查询集合
     *
     * @param seqSdkMonitor
     * @return
     */
    @PostMapping("/v1/seq/sdk/monitor/queryList")
    ResultResponse<List<SeqSdkMonitor>> queryList(@RequestBody SeqSdkMonitor seqSdkMonitor);


    @GetMapping("/v1/seq/monitor/getObjectById/{id}")
    ResultResponse<SeqSdkMonitor> getObjectById(@PathVariable("id") String id);
}
