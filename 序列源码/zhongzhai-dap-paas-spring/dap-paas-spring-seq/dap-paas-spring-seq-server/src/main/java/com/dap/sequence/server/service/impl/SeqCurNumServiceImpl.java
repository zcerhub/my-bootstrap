package com.dap.sequence.server.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.server.dao.SeqCurNumDao;
import com.dap.sequence.server.entity.SeqCurNum;
import com.dap.sequence.server.service.SeqCurNumService;
import com.dap.sequence.server.utils.SequenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: cao
 * Data: 2022/2/7 14:57
 * @Descricption:
 */
@Service
public class SeqCurNumServiceImpl extends AbstractBaseServiceImpl<SeqCurNum, String> implements SeqCurNumService {

    @Autowired
    private SeqCurNumDao seqCurNumDao;

    @Override
    public SeqCurNum getObjectByCode(Map<String, String> map) {
        return seqCurNumDao.getObjectByCode(map);
    }

    @Override
    public SeqCurNum selectForUpdateById(String id) {
        return seqCurNumDao.selectForUpdateById(id);
    }

    @Override
    public SeqCurNum selectForUpdateBySeq(SeqParamsDto seqParamsDto, Rule rule) {
        SeqCurNum query = new SeqCurNum();
        query.setSeqInstanceRuleId(rule.getRuleId());
        query.setInDay(seqParamsDto.getDay());
        SeqCurNum seqCurNum = seqCurNumDao.selectForUpdateBySeq(query);
        if (Objects.isNull(seqCurNum)) {
            seqCurNum = new SeqCurNum();
            seqCurNum.setId(SequenceUtil.getUUID32());
            seqCurNum.setInDay(seqParamsDto.getDay());
            seqCurNum.setSeqInstanceRuleId(rule.getRuleId());
            seqCurNum.setTenantId(seqParamsDto.getTenantId());
            seqCurNum.setSeqLock(0);
            seqCurNum.setCurVal("0");
            Integer result = seqCurNumDao.saveObject(seqCurNum);
            if (result == null || result == 0) {
                seqCurNum = seqCurNumDao.selectForUpdateBySeq(query);
            }
        }
        return seqCurNum;
    }
}
