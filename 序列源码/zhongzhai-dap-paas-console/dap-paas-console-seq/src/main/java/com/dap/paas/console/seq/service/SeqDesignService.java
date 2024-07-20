package com.dap.paas.console.seq.service;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.seq.entity.SeqDesignPo;
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
     * 导出excel
     *
     * @param response response
     * @param seqDesignList seqDesignList
     */
    void exportExcel(HttpServletResponse response, List<SeqDesignPo> seqDesignList);

    /**
     * 读取excel内容
     *
     * @param file file
     * @return List
     */
    List<SeqDesignPo> readExcel(MultipartFile file);

    /**
     * 序列批量保存
     *
     * @param seqDesignList seqDesignList
     * @return Result
     */
    Result saveBatch(List<SeqDesignPo> seqDesignList);

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

}
