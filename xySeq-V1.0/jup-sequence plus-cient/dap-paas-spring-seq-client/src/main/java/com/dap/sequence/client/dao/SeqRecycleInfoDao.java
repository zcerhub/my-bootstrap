package com.dap.sequence.client.dao;


import com.base.core.dao.BaseDao;
import com.dap.sequence.client.entity.SeqRecycleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: liu
 * @date: 2022/8/30 20:18
 * @description:
 */
public interface SeqRecycleInfoDao extends BaseDao<SeqRecycleInfo, String> {

    /**
     * 保存回收信息
     *
     * @param seqRecycleInfos seqRecycleInfos
     * @return Integer
     */
    Integer insertBatch(@Param("list") List<SeqRecycleInfo> seqRecycleInfos);

    /**
     * 查询序列回收信息
     *
     * @param map map
     * @return List
     */
    List<SeqRecycleInfo> getSeqForUpdate(Map map);

    List<SeqRecycleInfo> selectRecycleCode(Map map);

}
