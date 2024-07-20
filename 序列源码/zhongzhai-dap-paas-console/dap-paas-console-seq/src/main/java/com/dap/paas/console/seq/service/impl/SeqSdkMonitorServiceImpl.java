package com.dap.paas.console.seq.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dao.SeqSdkMonitorDao;
import com.dap.paas.console.seq.entity.SeqSdkMonitor;
import com.dap.paas.console.seq.service.SeqSdkMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @className SeqSdkMonitorServiceImpl
 * @description 序列SDK监控信息实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqSdkMonitorServiceImpl extends AbstractBaseServiceImpl<SeqSdkMonitor, String> implements SeqSdkMonitorService {

    /**
     * 超过一小时停止
     */
    private static final String MONITOR_STATUS_STOP = "0";

    @Autowired
    private SeqSdkMonitorDao seqSdkMonitorDao;

    @Override
    public Integer queryNormalTotal() {
        HashMap<String, String> map = new HashMap<>();
        map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ONE);
        return seqSdkMonitorDao.queryStatusTotal(map);
    }

    @Override
    public Integer queryExceptionTotal() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ZERO);
        return seqSdkMonitorDao.queryStatusTotal(map);

    }

    @Override
    public List<SeqSdkMonitor> queryListNoPermission() {
        return seqSdkMonitorDao.queryListNoPermission();
    }

    @Override
    public Integer updateNoPermission(SeqSdkMonitor seqSdkMonitor) {
        return seqSdkMonitorDao.updateNoPermission(seqSdkMonitor);
    }

    @Override
    public boolean rcvWorIdByClient(SeqSdkMonitor paramWorkData) {
        SeqSdkMonitor updateWorkData = new SeqSdkMonitor();
        updateWorkData.setId(paramWorkData.getId());
        updateWorkData.setUpdateDate(new Date());
        updateWorkData.setStatus(MONITOR_STATUS_STOP);
        return seqSdkMonitorDao.updateAllInfoWithVersion(updateWorkData) > 0;
    }
}
