package com.dap.sequence.server.controller.v1;

import com.dap.sequence.api.core.SeqServerProducer;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqServiceNode;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqPlatformService;
import com.dap.sequence.server.service.SeqServiceNodeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @className DemoControllerTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 17:26:03 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class DemoControllerTest {

    @InjectMocks
    private DemoController demoController;

    @Spy
    private SeqDesignService seqDesignService;

    @Spy
    private SeqServerProducer seqServerProducer;

    @Spy
    private SeqServiceNodeService seqServiceNodeService;

    @Spy
    private SeqPlatformService seqFromPlatformService;

    @Test
    public void ceshi() {
        when(seqFromPlatformService.getSeqInstanceRules(Mockito.any())).thenReturn(new ArrayList<>());
        demoController.ceshi("123");
    }

    @Test
    public void getTest() {
        when(seqDesignService.getObjectById(Mockito.any())).thenReturn(new SeqDesignPo());
        String seq = demoController.getTest("123");
        Assert.assertEquals("", seq);
    }

    @Test
    public void getStringSeq() {
        List<SeqDesignPo> designPos = new ArrayList<SeqDesignPo>() {
            {
                add(new SeqDesignPo());
            }
        };
        when(seqDesignService.queryList(Mockito.any(SeqDesignPo.class))).thenReturn(designPos);
        SeqTransferDto seqTransferDto = new SeqTransferDto() {
            {
                setList(new ArrayList<Object>() {
                    {
                        add(1);
                    }
                });
            }
        };
        when(seqServerProducer.getSeqFormServer(Mockito.any())).thenReturn(seqTransferDto);
        ResultResponse resultResponse = demoController.getStringSeq("123", "123");
        Assert.assertEquals(ExceptionEnum.SUCCESS.getResultCode(), resultResponse.getCode());
    }

    @Test
    public void getStringIps() {
        when(seqServiceNodeService.getObjectByClusterName(Mockito.any())).thenReturn(new SeqServiceNode());
        List<SeqServiceNode> listNode = new ArrayList<SeqServiceNode>() {
            {
                add(new SeqServiceNode() {
                    {
                        setHostIp("127.0.0.1");
                        setPort(80);
                    }
                });
            }
        };
        when(seqServiceNodeService.queryList(Mockito.anyMap())).thenReturn(listNode);
        ResultResponse resultResponse = demoController.getStringIps("123");
        Assert.assertEquals(ExceptionEnum.SUCCESS.getResultCode(), resultResponse.getCode());
    }
}