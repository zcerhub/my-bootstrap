package com.dap.sequence.server.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.server.dao.SeqServiceClusterDao;
import com.dap.sequence.server.entity.SeqServiceCluster;
import com.dap.sequence.server.service.SeqServiceClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 集群管理业务实现类
 * @author: zhangce
 * @create: 7/6/2024 10:00 PM
 **/
@Service
public class SeqServiceClusterServiceImpl extends AbstractBaseServiceImpl<SeqServiceCluster, String> implements SeqServiceClusterService {

    @Autowired
    private SeqServiceClusterDao seqServiceClusterDao;

}
