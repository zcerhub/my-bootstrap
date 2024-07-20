package com.dap.sequence.client.service.impl;


import com.base.core.service.impl.AbstractBaseServiceImpl;

import com.dap.sequence.client.dao.SeqDesignDao;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.service.SeqDesignService;
import com.dap.sequence.client.service.SeqInstanceRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @className SeqDesignServiceImpl
 * @description 序列配置实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqDesignServiceImpl extends AbstractBaseServiceImpl<SeqDesignPo, String> implements SeqDesignService {

    private static final Map<String, String> EXCEL_TITLE = new LinkedHashMap<>();

    @Autowired
    private SeqDesignDao seqDesignDao;

    @Autowired
    private SeqInstanceRuleService seqInstanceRuleService;

    static {
        EXCEL_TITLE.put("序列名称", "sequenceName");
        EXCEL_TITLE.put("序列编号", "sequenceCode");
        EXCEL_TITLE.put("应用id", "sequenceApplicationId");
        EXCEL_TITLE.put("描述", "sequenceDesc");
        EXCEL_TITLE.put("应用名称", "sequenceApplicationName");
        EXCEL_TITLE.put("生成数量", "sequenceNumber");
        EXCEL_TITLE.put("状态", "sequenceStatus");
        EXCEL_TITLE.put("规则", "sequenceRule");
        EXCEL_TITLE.put("序列规则对象链表", "ruleInfos");
        EXCEL_TITLE.put("回拨模式", "callbackMode");
    }

    @Override
    public Integer queryApplicationTotal() {
        return seqDesignDao.queryApplicationTotal();
    }

    @Override
    public Integer queryDesignTotal() {
        List<SeqDesignPo> seqDesignPos = seqDesignDao.queryList(new SeqDesignPo());
        return Optional.ofNullable(seqDesignPos).map(List::size).orElse(0);
    }

    @Override
    public List<SeqDesignPo> selectCode() {
        return seqDesignDao.selectCode();
    }

    @Override
    public SeqDesignPo getObjectByCode(Map map) {
        return seqDesignDao.getObjectByCode(map);
    }

    @Override
    public Integer updateStatusByIds(Map<String, Object> paramMap) {
        paramMap.put("updateDate", new Date());
        return seqDesignDao.updateStatusByIds(paramMap);
    }

    @Override
    public Integer updateObjectBatch(List<SeqDesignPo> seqDesignList) {
        return seqDesignDao.updateObjectBatch(seqDesignList);
    }


    @Override
    public SeqDesignPo getLocalObjectByCode(String code) {
        return seqDesignDao.getLocalObjectByCode(code);
    }


}
