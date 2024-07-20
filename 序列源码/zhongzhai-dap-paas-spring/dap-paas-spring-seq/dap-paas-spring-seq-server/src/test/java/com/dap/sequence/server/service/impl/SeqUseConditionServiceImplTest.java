package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.utils.EnvUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @className SeqUseConditionServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 16:10:08 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqUseConditionServiceImplTest {

    @InjectMocks
    private SeqUseConditionServiceImpl seqUseConditionService;

    @Spy
    private SeqPlatformService seqFromPlatformService;

    @Test
    public void saveUseCondition_01() {
        seqUseConditionService.saveUseCondition(new ArrayList<>(), null, null, null);
    }

    @Test
    public void saveUseCondition_02() {
        try(MockedStatic<EnvUtils> envUtilsMockedStatic = Mockito.mockStatic(EnvUtils.class)) {
            envUtilsMockedStatic.when(EnvUtils::getAddress).thenReturn("127.0.0.1");
        }
        when(seqFromPlatformService.isUsePlatform()).thenReturn(true);
        doNothing().when(seqFromPlatformService).saveUseCondition(Mockito.any());
        List<Object> obj = new ArrayList<Object>() {
            {
                add(1);
            }
        };
        seqUseConditionService.saveUseCondition(obj, null, new SeqDesignPo(), null);
    }
}