package com.dap.sequence.server.core;

import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.service.SeqDesignService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.util.ReflectionUtils.setField;

/**
 * @className DefaultSeqFlowEngineTest
 * @description TODO
 * @author renle
 * @date 2024/02/05 09:05:03 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultSeqFlowEngineTest {

    //@InjectMocks
    private static DefaultSeqFlowEngine defaultSeqFlowEngine = Mockito.mock(DefaultSeqFlowEngine.class);

    private static SeqDesignService seqDesignService =  Mockito.mock(SeqDesignService.class);

    @BeforeClass
    public static void before() {
        defaultSeqFlowEngine.setSeqObj(new SeqObj());
        setField(getField(defaultSeqFlowEngine, "seqDesignService"), defaultSeqFlowEngine, seqDesignService);
    }

    private static Field getField(DefaultSeqFlowEngine defaultSeqFlowEngine, String fieldName) {
        try {
            Field field = defaultSeqFlowEngine.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loadConf() {
        AbstractSeqFlowEngine abstractSeqFlowEngine = Mockito.spy(defaultSeqFlowEngine);
        when(abstractSeqFlowEngine.getSeqObj()).thenReturn(null);
        SeqDesignPo seqDesignPo = new SeqDesignPo() {
            {
                setServerCacheThreshold("10");
                setClientCacheThreshold("10");
            }
        };
        when(seqDesignService.getObjectByCode(Mockito.anyMap())).thenReturn(seqDesignPo);
        abstractSeqFlowEngine.loadConf();
    }

    @Test
    public void loadConf_01() {
        AbstractSeqFlowEngine abstractSeqFlowEngine = Mockito.spy(defaultSeqFlowEngine);
        when(abstractSeqFlowEngine.getSeqObj()).thenReturn(null);
        SeqDesignPo seqDesignPo = new SeqDesignPo() {
            {
                setId("10");
                setServerCacheThreshold("10");
                setClientCacheThreshold("10");
            }
        };
        when(seqDesignService.getObjectByCode(Mockito.anyMap())).thenReturn(seqDesignPo);
        abstractSeqFlowEngine.loadConf();
    }

    @Test
    public void testLoadConf() {
        AbstractSeqFlowEngine abstractSeqFlowEngine = Mockito.spy(defaultSeqFlowEngine);
        when(abstractSeqFlowEngine.getSeqObj()).thenReturn(null);
        SeqDesignPo seqDesignPo = new SeqDesignPo() {
            {
                setId("10");
                setServerCacheThreshold("10");
                setClientCacheThreshold("10");
            }
        };
        abstractSeqFlowEngine.loadConf(seqDesignPo);
    }
}