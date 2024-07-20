package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqRecoveryRecordDao;
import com.dap.sequence.server.entity.SeqRecoveryRecord;
import org.assertj.core.api.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @className SeqRecoveryRecordServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 15:41:46 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqRecoveryRecordServiceImplTest {

    @InjectMocks
    private SeqRecoveryRecordServiceImpl seqRecoveryRecordService;

    @Spy
    private SeqRecoveryRecordDao seqRecoveryRecordDao;

    @Test
    public void selectRecoveryForUpdate() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqObj seqObj = new SeqObj();
        List<SeqRecoveryRecord> seqRecoveryRecords = new ArrayList<SeqRecoveryRecord>() {
            {
                add(new SeqRecoveryRecord() {
                    {
                        setRecoveryStatus("0");
                    }
                });
            }
        };
        when(seqRecoveryRecordDao.selectRecoveryForUpdate(Mockito.any())).thenReturn(seqRecoveryRecords);
         seqRecoveryRecords = seqRecoveryRecordService.selectRecoveryForUpdate(seqParamsDto, seqObj);
        //Assert.assertEquals("1", seqRecoveryRecords.get(0).getRecoveryStatus());
    }

    @Test
    public void saveRecoveryRecord() {
        when(seqRecoveryRecordDao.saveBatchRecovery(Mockito.any())).thenReturn(1);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqObj seqObj = new SeqObj();
        List<Object> seqList = new ArrayList<Object>() {
            {
                add("1");
            }
        };
        int result = seqRecoveryRecordService.saveRecoveryRecord(seqParamsDto, seqObj, seqList);
        //Assert.assertEquals("1", String.valueOf(result));
    }

    @Test
    public void updateRecoveryStatus() {
        doNothing().when(seqRecoveryRecordDao).updateRecoveryStatus(Mockito.any());
        SeqRecoveryRecord seqRecoveryRecord = new SeqRecoveryRecord();
        seqRecoveryRecordService.updateRecoveryStatus(seqRecoveryRecord);
        //Assert.assertNotNull(seqRecoveryRecord.getUpdateDate());
    }

    @Test(expected = SequenceException.class)
    public void updateRecovery_error01() {
        List<SeqRecoveryRecord> seqRecoveryRecords = new ArrayList<>();
        when(seqRecoveryRecordDao.queryRecoveryRecords(Mockito.any())).thenReturn(seqRecoveryRecords);
        seqRecoveryRecordService.updateRecovery(new SeqParamsDto());
    }

    @Test
    public void updateRecovery() {
        List<SeqRecoveryRecord> seqRecoveryRecords = new ArrayList<SeqRecoveryRecord>() {
            {
                add(new SeqRecoveryRecord());
            }
        };
        when(seqRecoveryRecordDao.queryRecoveryRecords(Mockito.any())).thenReturn(seqRecoveryRecords);
        doNothing().when(seqRecoveryRecordDao).updateRecoveryStatus(Mockito.any());
        seqRecoveryRecordService.updateRecovery(new SeqParamsDto());
    }
}