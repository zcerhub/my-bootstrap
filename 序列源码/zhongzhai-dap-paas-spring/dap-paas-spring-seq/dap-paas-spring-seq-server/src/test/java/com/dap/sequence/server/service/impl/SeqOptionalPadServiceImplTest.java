package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqOptionalPadDao;
import com.dap.sequence.server.entity.SeqOptionalPad;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @className SeqOptionalPadServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 14:47:08 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqOptionalPadServiceImplTest {

    @InjectMocks
    private SeqOptionalPadServiceImpl seqOptionalPadService;

    @Spy
    private SeqOptionalPadDao seqOptionalPadDao;

    @Test
    public void getOptionalPad() {
        SeqOptionalPad seqOptionalPad = new SeqOptionalPad();
        when(seqOptionalPadDao.getOptionalPad(Mockito.any())).thenReturn(null).thenReturn(seqOptionalPad);
        SeqOptionalPad pad = seqOptionalPadService.getOptionalPad("1", "123");
        Assert.assertNotNull(pad);
    }

    @Test
    public void updateOptionalPad() {
        doNothing().when(seqOptionalPadDao).updateOptionalPad(Mockito.any());
        seqOptionalPadService.updateOptionalPad(new SeqOptionalPad());
    }

    @Test
    public void selectForUpdatePadById() {
        when(seqOptionalPadDao.selectForUpdatePadById(Mockito.any())).thenReturn(new SeqOptionalPad());
        SeqOptionalPad pad = seqOptionalPadService.selectForUpdatePadById("123");
        Assert.assertNotNull(pad);
    }
}