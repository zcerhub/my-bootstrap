package com.dap.sequence.client.service.impl;

import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.snow.Snowflake;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class SequenceGeneratorClientImplTest {

    @InjectMocks
    private SequenceGeneratorClientImpl sequenceGeneratorClient;
    @Mock
    private Snowflake snowflake;
    @Mock
    private SeqConsumer seqConsumer;
    @Mock
    private SequenceQueueFactory sequenceQueueFactory;



    @BeforeEach
    public void setUp() throws Exception {

    }


    @org.junit.jupiter.api.Test
    void getStringSequence() {
        SeqParamsDto seqParamsDto=new SeqParamsDto();
        seqParamsDto.setSeqCode("dfasdf");
        List<Object> list =new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(i+ RandomUtils.nextInt());
        }
        SequenceQueue sequenceQueue = new SequenceQueue(list, 20, 30, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
        Mockito.lenient().when(sequenceQueueFactory.getSeqArrayFormServer(seqParamsDto,sequenceQueue)).thenReturn(sequenceQueue);
       // Mockito.when(sequenceGeneratorClient.getVariableSequence(seqParamsDto.getSeqCode(),null,null)).thenReturn("888");
        String  seq=sequenceGeneratorClient.getStringSequence("dfasdf");
        Assertions.assertNotNull(seq);
    }

    @org.junit.jupiter.api.Test
    void getDaySwitchSequence() {
        SeqParamsDto seqParamsDto=new SeqParamsDto();
        seqParamsDto.setSeqCode("dfasdf");

        List<Object> list =new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add(i+ RandomUtils.nextInt());
        }
        SequenceQueue sequenceQueue = new SequenceQueue(list, 20, 30, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
        //Mockito.when(sequenceQueueFactory.getSeqArrayFormServer(seqParamsDto,sequenceQueue)).thenReturn(sequenceQueue);
        Mockito.lenient().when(sequenceQueueFactory.getSeqArrayFormServer(seqParamsDto,sequenceQueue)).thenReturn(sequenceQueue);
        String  seq=sequenceGeneratorClient.getDaySwitchSequence("dfasdf",new Date());
        Assertions.assertNotNull(seq);
    }

    @org.junit.jupiter.api.Test
    void getSnowFlake() {
        Mockito.when(snowflake.nextIdStr()).thenReturn("111");
        String  seq=sequenceGeneratorClient.getSnowFlake();
        Assertions.assertNotNull(seq);
    }
}