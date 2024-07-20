package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.server.dao.SeqCurNumDao;
import com.dap.sequence.server.entity.SeqCurNum;
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
 * @className SeqCurNumServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 14:29:21 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqCurNumServiceImplTest {

    @InjectMocks
    private SeqCurNumServiceImpl seqCurNumService;

    @Spy
    private SeqCurNumDao seqCurNumDao;

    @Test
    public void getObjectByCode() {
        SeqCurNum seq = new SeqCurNum() {
            {
                setCurVal("1");
            }
        };
        when(seqCurNumDao.getObjectByCode(Mockito.any())).thenReturn(seq);
        SeqCurNum seqCurNum = seqCurNumService.getObjectByCode(new HashMap<>());
        Assert.assertEquals("1", seqCurNum.getCurVal());
    }

    @Test
    public void selectForUpdateById() {
        SeqCurNum seq = new SeqCurNum() {
            {
                setCurVal("1");
            }
        };
        when(seqCurNumDao.selectForUpdateById(Mockito.any())).thenReturn(seq);
        SeqCurNum seqCurNum = seqCurNumService.selectForUpdateById("123");
        Assert.assertEquals("1", seqCurNum.getCurVal());
    }

    @Test
    public void selectForUpdateBySeq() {
        SeqCurNum seqCurNum = new SeqCurNum();
        when(seqCurNumDao.selectForUpdateBySeq(Mockito.any())).thenReturn(null).thenReturn(seqCurNum);
        when(seqCurNumDao.saveObject(Mockito.any())).thenReturn(0);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        Rule rule = new Rule();
        SeqCurNum curNum = seqCurNumService.selectForUpdateBySeq(seqParamsDto, rule);
        Assert.assertNotNull(curNum);
    }
}