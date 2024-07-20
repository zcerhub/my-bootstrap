package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqRecycleInfoDao;
import com.dap.sequence.server.entity.SeqRecycleInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @className SeqRecycleInfoServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 15:56:22 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqRecycleInfoServiceImplTest {

    @InjectMocks
    private SeqRecycleInfoServiceImpl seqRecycleInfoService;

    @Spy
    private SeqRecycleInfoDao seqRecycleInfoDao;

    @Test
    public void insertBatch() {
        when(seqRecycleInfoDao.insertBatch(Mockito.any())).thenReturn(1);
        int result = seqRecycleInfoService.insertBatch(new ArrayList<>());
        Assert.assertEquals(1, result);
    }

    @Test
    public void getSeqForUpdate() {
        when(seqRecycleInfoDao.getSeqForUpdate(Mockito.any())).thenReturn(new ArrayList<>());
        List<SeqRecycleInfo> infos = seqRecycleInfoService.getSeqForUpdate(new HashMap<>());
        Assert.assertEquals(0, infos.size());
    }
}