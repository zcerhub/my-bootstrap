package com.dap.sequence.server.core;

import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.service.SeqUseConditionService;
import com.dap.sequence.server.utils.EnvUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

/**
 * @className AsyncTaskTest
 * @description TODO
 * @author renle
 * @date 2024/02/05 08:43:53 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class AsyncTaskTest {

    @InjectMocks
    private AsyncTask asyncTask;

    @Spy
    private SeqUseConditionService seqUseConditionService;

    @Test
    public void asyncCreate() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqProducer seqProducer = Mockito.mock(SeqProducer.class);
        AbstractSeqFlowEngine seqFlowEngine = Mockito.spy(AbstractSeqFlowEngine.class);
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceNumber(10);
                setServerCacheThreshold(30);
            }
        });
        asyncTask.asyncCreate(seqParamsDto, seqProducer, seqFlowEngine);
        Mockito.verify(seqProducer, times(1)).createSeqAndCache(Mockito.any(), Mockito.any());
    }

    @Test
    public void asyncCreate_01() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqProducer seqProducer = Mockito.mock(SeqProducer.class);
        AbstractSeqFlowEngine seqFlowEngine = Mockito.spy(AbstractSeqFlowEngine.class);
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceNumber(10);
                setServerCacheThreshold(100);
            }
        });
        asyncTask.asyncCreate(seqParamsDto, seqProducer, seqFlowEngine);
        Mockito.verify(seqProducer, times(0)).createSeqAndCache(Mockito.any(), Mockito.any());
    }

    @Test
    public void saveUseInfo() {
        try (MockedStatic<EnvUtils> envUtilsMockedStatic = Mockito.mockStatic(EnvUtils.class)) {
            envUtilsMockedStatic.when(() -> EnvUtils.getIpAddress(Mockito.any(HttpServletRequest.class))).thenReturn("127.0.0.1");
        }
        doNothing().when(seqUseConditionService).saveUseCondition(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        List<Object> obj = new ArrayList<>();
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqDesignPo seqDesignPo = new SeqDesignPo();
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        asyncTask.saveUseInfo(obj, seqParamsDto, seqDesignPo, httpServletRequest);
    }

    @Test
    public void asyncScheduledCreate() {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        SeqProducer seqProducer = Mockito.mock(SeqProducer.class);
        AbstractSeqFlowEngine seqFlowEngine = Mockito.spy(AbstractSeqFlowEngine.class);
        seqFlowEngine.setSeqObj(new SeqObj() {
            {
                setSequenceNumber(10);
                setServerCacheThreshold(30);
            }
        });
        asyncTask.asyncScheduledCreate(seqParamsDto, seqProducer, seqFlowEngine);
        Mockito.verify(seqProducer, times(1)).createSeqAndCache(Mockito.any(), Mockito.any());
    }
}