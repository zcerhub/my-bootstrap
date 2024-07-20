package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqUseCondition;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ReflectionUtils.setField;

/**
 * @className SeqPlatformServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 15:19:52 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqPlatformServiceImplTest {

    @InjectMocks
    private SeqPlatformServiceImpl service;

    private static Field getField(SeqPlatformServiceImpl platformService, String fieldName) {
        try {
            Field field = platformService.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void isUsePlatform() {
        setField(getField(service, "platformUrl"), service, "127.0.0.1:11111");
        boolean result = service.isUsePlatform();
        Assert.assertFalse(result);
    }

    @Test(expected = Exception.class)
    public void getAllSeqDesign_error() {
        List<SeqDesignPo> seqDesignPos = service.getAllSeqDesign();
    }

    @Test(expected = Exception.class)
    public void getSeqInstanceRules() {
        service.getSeqInstanceRules("123");
    }

    @Test
    public void getData() {
        ResultResponse resultResponse = new ResultResponse() {
            {
                setCode(ExceptionEnum.SUCCESS.getResultCode());
                setData(new ArrayList<SeqDesignPo>() {
                    {
                        add(new SeqDesignPo());
                    }
                });
            }
        };
        ResponseEntity<ResultResponse> responseEntity = ResponseEntity.of(Optional.of(resultResponse));
        List<SeqDesignPo> seqDesignPos = service.getData(responseEntity, new TypeReference<List<SeqDesignPo>>() {
        });
        Assert.assertEquals(1, seqDesignPos.size());
    }

    @Test(expected = Exception.class)
    public void saveUseCondition() {
        service.saveUseCondition(new SeqUseCondition());
    }
}