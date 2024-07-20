package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqDesignDao;
import com.dap.sequence.server.entity.SeqDesignPo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @className SeqDesignServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 14:39:35 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqDesignServiceImplTest {

    @InjectMocks
    private SeqDesignServiceImpl seqDesignService;

    @Spy
    private SeqDesignDao seqDesignDao;

    @Test
    public void getObjectByCode() {
        when(seqDesignDao.getObjectByCode(Mockito.any())).thenReturn(new SeqDesignPo());
        SeqDesignPo seqDesignPo = seqDesignService.getObjectByCode(new HashMap<>());
        Assert.assertNotNull(seqDesignPo);
    }

    @Test
    public void checkAccessKey() {
        when(seqDesignDao.checkAccessKey(Mockito.any())).thenReturn(1);
        boolean result = seqDesignService.checkAccessKey(new HashMap<>());
        Assert.assertTrue(result);
    }
}