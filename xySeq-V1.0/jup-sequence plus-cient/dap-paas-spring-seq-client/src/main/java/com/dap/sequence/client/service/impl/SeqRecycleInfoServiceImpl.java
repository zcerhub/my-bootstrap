package com.dap.sequence.client.service.impl;


import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.client.dao.SeqRecycleInfoDao;
import com.dap.sequence.client.entity.SeqRecycleInfo;
import com.dap.sequence.client.service.SeqRecycleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: liu
 * @date: 2022/8/30 20:22
 * @description:
 */
@Service
public class SeqRecycleInfoServiceImpl extends AbstractBaseServiceImpl<SeqRecycleInfo, String> implements SeqRecycleInfoService {

    @Autowired
    private SeqRecycleInfoDao seqRecycleInfoDao;

    @Override
    public Integer insertBatch(List<SeqRecycleInfo> list) {
        return seqRecycleInfoDao.insertBatch(list);
    }

    @Override
    public List<SeqRecycleInfo> getSeqForUpdate(Map<String, Object> map) {
        return seqRecycleInfoDao.getSeqForUpdate(map);
    }

    @Override
    public List<SeqRecycleInfo> selectRecycleCode(Map<String, Object> map) {
        return seqRecycleInfoDao.selectRecycleCode(map);
    }
}
