package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqServiceNodeDao;
import com.dap.sequence.server.entity.SeqServiceNode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @className SeqServiceNodeServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 16:05:18 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqServiceNodeServiceImplTest {

    @InjectMocks
    private SeqServiceNodeServiceImpl seqServiceNodeService;

    @Spy
    private SeqServiceNodeDao seqServiceNodeDao;

    @Test
    public void getObjectByClusterName() {
        when(seqServiceNodeDao.getObjectByClusterName(Mockito.anyString())).thenReturn(new SeqServiceNode());
        SeqServiceNode seqServiceNode = seqServiceNodeService.getObjectByClusterName("123");
        Assert.assertNotNull(seqServiceNode);
    }
}