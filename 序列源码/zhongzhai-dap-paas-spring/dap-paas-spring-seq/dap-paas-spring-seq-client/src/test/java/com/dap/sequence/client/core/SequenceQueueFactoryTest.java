package com.dap.sequence.client.core;

import com.alibaba.fastjson.JSONObject;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.obj.SeqObj;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class SequenceQueueFactoryTest {

    @InjectMocks
    SequenceQueueFactory sequenceQueueFactory;
    @Mock
    SeqConsumer seqConsumer;

    @Test
    void getCurrentDaySwirchSequenceBase() {
        SeqParamsDto seqParamsDto=new SeqParamsDto();
        seqParamsDto.setSeqCode("dfasdf");
        Date date=new Date();
        List<Object> list =new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(i+ RandomUtils.nextInt());
        }


        String requestStr= "{\"id\":\"1748221824268206080\",\"sequenceName\":\"测试\",\"sequenceCode\":\"dfasdf\",\"sequenceApplicationId\":\"1559497497343967232\",\"sequenceApplicationName\":\"核心平台\",\"sequenceNumber\":100,\"requestNumber\":10,\"sequenceDesc\":\"\",\"callbackMode\":\"1\"}";
        SeqObj seqDesignPo=JSONObject.parseObject(requestStr, SeqObj.class);
        seqDesignPo.setSequenceCode(seqParamsDto.getSeqCode());
        Mockito.when(seqConsumer.getSeqDesignObj(seqParamsDto.getSeqCode())).thenReturn(seqDesignPo);

        //SequenceQueue sequenceQueue = new SequenceQueue(list, 20, 30, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
        SeqTransferDto seqTransferDto=new SeqTransferDto();
        seqTransferDto.setList(list);
        Mockito.when(seqConsumer.getSeqFormServer(seqParamsDto)).thenReturn(seqTransferDto);
        Object  seq=sequenceQueueFactory.getSequenceBase(seqParamsDto,date);
        Assertions.assertNotNull(seq);
    }


    @Test
    void getNextDaySwirchSequenceBase() {
        SeqParamsDto seqParamsDto=new SeqParamsDto();
        seqParamsDto.setSeqCode("dfasdf");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        Date date=calendar.getTime();
        String requestStr= "{\"id\":\"1748221824268206080\",\"sequenceName\":\"测试\",\"sequenceCode\":\"dfasdf\",\"sequenceApplicationId\":\"1559497497343967232\",\"sequenceApplicationName\":\"核心平台\",\"sequenceNumber\":100,\"requestNumber\":10,\"sequenceDesc\":\"\",\"callbackMode\":\"1\"}";
        SeqObj seqDesignPo=JSONObject.parseObject(requestStr, SeqObj.class);
        seqDesignPo.setSequenceCode(seqParamsDto.getSeqCode());
        Mockito.when(seqConsumer.getSeqDesignObj(seqParamsDto.getSeqCode())).thenReturn(seqDesignPo);

        SeqTransferDto seqTransferDto=new SeqTransferDto();
        List<Object> list =new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(i+ RandomUtils.nextInt());
        }
        seqTransferDto.setList(list);
        Mockito.when(seqConsumer.getSeqFormServer(seqParamsDto)).thenReturn(seqTransferDto);
        Object  seq=sequenceQueueFactory.getSequenceBase(seqParamsDto,date);
        Assertions.assertNotNull(seq);
    }

}