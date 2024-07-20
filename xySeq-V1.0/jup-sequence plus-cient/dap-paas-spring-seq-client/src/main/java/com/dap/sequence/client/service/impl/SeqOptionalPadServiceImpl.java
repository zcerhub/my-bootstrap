package com.dap.sequence.client.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.client.dao.SeqOptionalPadDao;
import com.dap.sequence.client.entity.SeqOptionalPad;
import com.dap.sequence.client.service.SeqOptionalPadService;
import com.dap.sequence.client.utils.SequenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @className SeqOptionalPadServiceImpl
 * @description 自选补位值
 * @author renle
 * @date 2023/12/21 14:45:29 
 * @version: V23.06
 */

@Service
public class SeqOptionalPadServiceImpl extends AbstractBaseServiceImpl<SeqOptionalPad, String> implements SeqOptionalPadService {

    @Autowired
    private SeqOptionalPadDao seqOptionalPadDao;

    @Override
    public SeqOptionalPad getOptionalPad(String seqCode, String seqVal) {
        SeqOptionalPad query = new SeqOptionalPad();
        query.setSeqCode(seqCode);
        query.setSeqValue(seqVal);
        SeqOptionalPad seqOptionalPad = seqOptionalPadDao.getOptionalPad(query);
        if (seqOptionalPad == null) {
            // 初始化补位值
            seqOptionalPad = query;
            seqOptionalPad.setOptionalValue(0L);
            seqOptionalPad.setPaddindValue(0L);
            seqOptionalPad.setCreateDate(new Date());
            seqOptionalPad.setId(SequenceUtil.getUUID32());
            Integer result = seqOptionalPadDao.saveOptionalPad(seqOptionalPad);
            if (result == null || result == 0) {
                // 已经有别的更新入库 直接查询
                seqOptionalPad = seqOptionalPadDao.getOptionalPad(query);
            }
        }
        return seqOptionalPad;
    }

    @Override
    public void updateOptionalPad(SeqOptionalPad seqOptionalPad) {
        seqOptionalPadDao.updateOptionalPad(seqOptionalPad);
    }

    @Override
    public SeqOptionalPad selectForUpdatePadById(String id) {
        return seqOptionalPadDao.selectForUpdatePadById(id);
    }
}
