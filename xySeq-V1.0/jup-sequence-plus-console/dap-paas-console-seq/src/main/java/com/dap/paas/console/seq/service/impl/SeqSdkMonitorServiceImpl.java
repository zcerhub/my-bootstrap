package com.dap.paas.console.seq.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import com.dap.paas.console.seq.feign.SeqSdkMonitorFeignApi;
import com.dap.paas.console.seq.service.SeqSdkMonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @className SeqSdkMonitorServiceImpl
 * @description 序列SDK监控信息实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqSdkMonitorServiceImpl implements SeqSdkMonitorService {

    @Autowired
    private SeqSdkMonitorFeignApi seqSdkMonitorFeignApi;

    @Override
    public Integer queryNormalTotal() {
        HashMap<String, String> map = new HashMap<>();
        map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ONE);
        return seqSdkMonitorFeignApi.queryStatusTotal(map).getData();
    }

    @Override
    public Integer queryExceptionTotal() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ZERO);
        return seqSdkMonitorFeignApi.queryStatusTotal(map).getData();

    }

    @Override
    public ResultResponse<List<SeqSdkMonitor>> queryListNoPermission() {
        return seqSdkMonitorFeignApi.queryListNoPermission();
    }

    @Override
    public ResultResponse<Integer> updateNoPermission(SeqSdkMonitor seqSdkMonitor) {
        return seqSdkMonitorFeignApi.updateNoPermission(seqSdkMonitor);
    }

    @Override
    public List<SeqSdkMonitor> queryList(SeqSdkMonitor seqSdkMonitor) {
        ResultResponse<List<SeqSdkMonitor>> listResultResponse = seqSdkMonitorFeignApi.queryList(seqSdkMonitor);
        return listResultResponse.getData();
    }

    @Override
    public SeqSdkMonitor getObjectById(String id) {
        ResultResponse<SeqSdkMonitor> resultResponse = seqSdkMonitorFeignApi.getObjectById(id);
        return resultResponse.getData();
    }
}
