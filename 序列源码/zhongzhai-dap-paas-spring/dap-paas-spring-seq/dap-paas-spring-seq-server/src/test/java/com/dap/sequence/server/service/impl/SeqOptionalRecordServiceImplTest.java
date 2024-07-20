package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqOptionalRecordDao;
import com.dap.sequence.server.entity.SeqCurNum;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.service.SeqCurNumService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @className SeqOptionalRecordServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 14:52:31 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqOptionalRecordServiceImplTest {

    @InjectMocks
    private SeqOptionalRecordServiceImpl seqOptionalRecordService;

    @Spy
    private SeqOptionalRecordDao seqOptionalRecordDao;

    @Spy
    private SeqCurNumService seqCurNumService;

    @Test
    public void queryRecoveryOptional() {
        List<SeqOptionalRecord> seqOptionalRecords = new ArrayList<SeqOptionalRecord>() {
            {
                add(new SeqOptionalRecord() {
                    {
                        setSerialNumber("1");
                    }
                });
            }
        };
        when(seqOptionalRecordDao.getRecoveryOptional(Mockito.any(), Mockito.anyInt())).thenReturn(seqOptionalRecords);
        SeqParamsDto seqParamsDto = new SeqParamsDto() {
            {
                setRequestNumber(0);
            }
        };
        SeqObj seqObj = new SeqObj() {
            {
                setRequestNumber(1);
            }
        };
        LinkedList<Object> linkedList =  seqOptionalRecordService.queryRecoveryOptional(seqParamsDto, seqObj);
        Assert.assertEquals(1, linkedList.size());
    }

    @Test(expected = SequenceException.class)
    public void selectedOptionalRecord_error01() {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord();
        when(seqOptionalRecordDao.selectForUpdate(Mockito.any())).thenReturn(optionalRecord);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqOptionalRecordService.selectedOptionalRecord(seqParamsDto);
    }

    @Test(expected = SequenceException.class)
    public void selectedOptionalRecord_error02() {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord() {
            {
                setOptionalStatus("1");
            }
        };
        when(seqOptionalRecordDao.selectForUpdate(Mockito.any())).thenReturn(optionalRecord);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqOptionalRecordService.selectedOptionalRecord(seqParamsDto);
    }

    @Test(expected = SequenceException.class)
    public void selectedOptionalRecord_error03() {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord() {
            {
                setOptionalStatus("0");
                setFilterStatus("0");
                setOptionalValue(9L);
            }
        };
        when(seqOptionalRecordDao.selectForUpdate(Mockito.any())).thenReturn(optionalRecord);
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("10");
            }
        };
        when(seqCurNumService.getObjectById(Mockito.any())).thenReturn(seqCurNum);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqOptionalRecordService.selectedOptionalRecord(seqParamsDto);
    }

    @Test(expected = SequenceException.class)
    public void selectedOptionalRecord_error04() {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord() {
            {
                setOptionalStatus("0");
                setFilterStatus("0");
                setOptionalValue(9L);
            }
        };
        when(seqOptionalRecordDao.selectForUpdate(Mockito.any())).thenReturn(optionalRecord);
        SeqCurNum seqCurNum = new SeqCurNum() {
            {
                setCurVal("1");
            }
        };
        when(seqCurNumService.getObjectById(Mockito.any())).thenReturn(seqCurNum);
        doThrow(new SequenceException("123")).when(seqOptionalRecordDao).updateRecordStatus(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqOptionalRecordService.selectedOptionalRecord(seqParamsDto);
    }

    @Test(expected = SequenceException.class)
    public void cancelOptionalRecord() {
        doThrow(new SequenceException("123")).when(seqOptionalRecordDao).updateRecordStatus(Mockito.any());
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqOptionalRecordService.cancelOptionalRecord(seqParamsDto);
    }

    @Test
    public void getRecordByValue() {
        when(seqOptionalRecordDao.getRecordByValue(Mockito.any())).thenReturn(new ArrayList<>());
        List<SeqOptionalRecord> seqOptionalRecords = seqOptionalRecordService.getRecordByValue("1", 1, 1);
        Assert.assertNotNull(seqOptionalRecords);
    }

    @Test
    public void updateFilterStatus() {
        doNothing().when(seqOptionalRecordDao).updateFilterStatus(Mockito.any());
        seqOptionalRecordService.updateFilterStatus(new ArrayList<>());
    }
}