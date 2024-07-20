package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqSdkMonitorDao;
import com.dap.sequence.server.entity.SeqSdkMonitor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @className SeqSdkMonitorServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 16:54:17 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqSdkMonitorServiceImplTest {

    @InjectMocks
    private SeqSdkMonitorServiceImpl service;

    @Spy
    private SeqSdkMonitorDao seqSdkMonitorDao;

    @Test
    public void initWorkId_01() {
        when(seqSdkMonitorDao.queryMaxWorkId(Mockito.any())).thenReturn(null);
        when(seqSdkMonitorDao.saveObject(Mockito.any())).thenReturn(1);
        SeqSdkMonitor seqSdkMonitor = new SeqSdkMonitor();
        SeqSdkMonitor monitor = service.initWorkId(seqSdkMonitor);
        Assert.assertEquals("0", monitor.getWorkId().toString());
    }

    @Test
    public void initWorkId_02() {
        when(seqSdkMonitorDao.queryMaxWorkId(Mockito.any())).thenReturn(1);
        when(seqSdkMonitorDao.updateStatus(Mockito.anyMap())).thenReturn(1);
        List<SeqSdkMonitor> list = new ArrayList<SeqSdkMonitor>() {
            {
                add(new SeqSdkMonitor() {
                    {
                        setWorkId(1);
                    }
                });
            }
        };
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(list);
        SeqSdkMonitor seqSdkMonitor = new SeqSdkMonitor();
        SeqSdkMonitor monitor = service.initWorkId(seqSdkMonitor);
        Assert.assertEquals("1", monitor.getWorkId().toString());
    }

    @Test
    public void initWorkId_03() {
        when(seqSdkMonitorDao.queryMaxWorkId(Mockito.any())).thenReturn(1);
        when(seqSdkMonitorDao.updateStatus(Mockito.anyMap())).thenReturn(0);
        List<SeqSdkMonitor> list = new ArrayList<SeqSdkMonitor>() {
            {
                add(new SeqSdkMonitor() {
                    {
                        setWorkId(1);
                    }
                });
            }
        };
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(list);
        when(seqSdkMonitorDao.updateAllInfoWithVersion(Mockito.any())).thenReturn(1);
        SeqSdkMonitor seqSdkMonitor = new SeqSdkMonitor();
        SeqSdkMonitor monitor = service.initWorkId(seqSdkMonitor);
        Assert.assertEquals("1", monitor.getWorkId().toString());
    }

    @Test
    public void initWorkId_04() {
        when(seqSdkMonitorDao.queryMaxWorkId(Mockito.any())).thenReturn(1);
        when(seqSdkMonitorDao.updateStatus(Mockito.anyMap())).thenReturn(0);
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(new ArrayList<>());
        when(seqSdkMonitorDao.queryMaxWorkId(Mockito.anyMap())).thenReturn(99);
        when(seqSdkMonitorDao.saveObject(Mockito.any())).thenReturn(1);
        SeqSdkMonitor seqSdkMonitor = new SeqSdkMonitor();
        SeqSdkMonitor monitor = service.initWorkId(seqSdkMonitor);
        Assert.assertEquals("100", monitor.getWorkId().toString());
    }

    @Test
    public void liveWorkId() {
        List<SeqSdkMonitor> list = new ArrayList<SeqSdkMonitor>() {
            {
                add(new SeqSdkMonitor() {
                    {
                        setWorkId(1);
                    }
                });
            }
        };
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(list);
        when(seqSdkMonitorDao.updateAllInfoWithVersion(Mockito.any())).thenReturn(1);
        boolean result = service.liveWorkId(new SeqSdkMonitor());
        Assert.assertTrue(result);
    }

    @Test
    public void rcvSnowflakeWorkId() {
        when(seqSdkMonitorDao.updateStatus(Mockito.anyMap())).thenReturn(1);
        int result = service.rcvSnowflakeWorkId(100);
        Assert.assertEquals(1, result);
    }

    @Test
    public void rcvWorIdByClient_01() {
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(new ArrayList<>());
        int result = service.rcvWorIdByClient(new SeqSdkMonitor());
        Assert.assertEquals(0, result);
    }

    @Test
    public void rcvWorIdByClient_02() {
        List<SeqSdkMonitor> list = new ArrayList<SeqSdkMonitor>() {
            {
                add(new SeqSdkMonitor() {
                    {
                        setWorkId(1);
                    }
                });
            }
        };
        when(seqSdkMonitorDao.queryList(Mockito.anyMap())).thenReturn(list);
        when(seqSdkMonitorDao.updateAllInfoWithVersion(Mockito.any())).thenReturn(1);
        int result = service.rcvWorIdByClient(new SeqSdkMonitor());
        Assert.assertEquals(1, result);
    }
}