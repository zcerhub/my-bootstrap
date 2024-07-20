package com.dap.sequence.client.service;


import com.base.core.service.BaseService;
import com.dap.sequence.api.commons.Result;
import com.dap.sequence.client.entity.SeqDesignPo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @className SeqDesignService
 * @description 序列设计接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqDesignService extends BaseService<SeqDesignPo, String> {

     List<SeqDesignPo> selectCode();

    /**
     * 通过code获取序列
     *
     * @param map map
     * @return SeqDesignPo
     */
    SeqDesignPo getObjectByCode(Map map);

    /**
     * 获取应用数量
     *
     * @return Integer
     */
    Integer queryApplicationTotal();

    /**
     * 获取序列总数
     *
     * @return Integer
     */
    Integer queryDesignTotal();



    /**
     * 批量更新
     *
     * @param map map
     * @return Integer
     */
    Integer updateStatusByIds(Map<String, Object> map);

    /**
     * 批量更新
     *
     * @param seqDesignList seqDesignList
     * @return Integer
     */
    Integer updateObjectBatch(List<SeqDesignPo> seqDesignList);
    /**
     * 根据code查询
     *
     * @param String code
     * @return SeqDesignPo
     */
    SeqDesignPo getLocalObjectByCode(String code);


}
