package com.dap.sequence.server.service.impl;

import com.dap.sequence.server.dao.SeqInstanceRuleDao;
import com.dap.sequence.server.entity.SeqInstanceRule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @className SeqInstanceRuleServiceImplTest
 * @description TODO
 * @author renle
 * @date 2024/02/04 14:44:18 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class SeqInstanceRuleServiceImplTest {

    @InjectMocks
    private SeqInstanceRuleServiceImpl seqInstanceRuleService;

    @Spy
    private SeqInstanceRuleDao seqInstanceRuleDao;

    @Test
    public void getInstanceRuleList() {
        when(seqInstanceRuleDao.getInstanceRuleList(Mockito.any())).thenReturn(new ArrayList<>());
        List<SeqInstanceRule> rules = seqInstanceRuleService.getInstanceRuleList(new HashMap<>());
        Assert.assertEquals(0, rules.size());
    }
}