package com.dap.sequence.server.controller.v1;

import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqDesignService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @className SeqCallBackControllerTest
 * @description TODO
 * @author renle
 * @date 2024/02/02 11:02:12 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqCallBackControllerTest {

    @InjectMocks
    private SeqCallBackController seqCallBackController;

    @Spy
    private SeqDesignService seqDesignService;

    @Spy
    private SeqProducer seqProducer;

    @Test
    public void shutdownCallbackSeq() {
        List<SeqDesignPo>  seqDesignPos = new ArrayList<SeqDesignPo>() {
            {
                add( new SeqDesignPo() {
                    {
                        setSequenceCode("123");
                        setTenantId("123");
                    }
                });
            }
        };
        when(seqDesignService.queryList(Mockito.any(SeqDesignPo.class))).thenReturn(seqDesignPos);
        SeqCallbackDto seqCallbackDto = new SeqCallbackDto() {
            {
                setDay("123");
                setTenantId("123");
                setSequenceCode("123");
            }
        };
        BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(1);
        SeqHolder.getMap().put("123###123@123", blockingQueue);
        AbstractSeqFlowEngine seqFlowEngine = new AbstractSeqFlowEngine() {
            @Override
            public void loadConf() {
            }

            @Override
            public void loadConf(SeqDesignPo seqDesignPo) {
            }
        };
        seqFlowEngine.setSeqProducer(new AtomicReference<SeqProducer>() {
            {
                set(seqProducer);
            }
        });
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceCode("123");
            }
        });
        SeqHolder.getEngineMap().put("123@123", seqFlowEngine);
        HttpEntity<SeqCallbackDto> request = new HttpEntity<>(seqCallbackDto);
        ResultResponse resultResponse = seqCallBackController.shutdownCallbackSeq(request, null);
        Assert.assertEquals("回收成功", resultResponse.getData().toString());
    }
}