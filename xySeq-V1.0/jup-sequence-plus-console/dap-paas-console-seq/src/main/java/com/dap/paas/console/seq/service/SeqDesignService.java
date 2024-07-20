package com.dap.paas.console.seq.service;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
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
public interface SeqDesignService{

    /**
     * 序列新增
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    ResultResponse<ExceptionEnum> insert(SeqDesignPo seqDesignPo);

    /**
     * 序列列表查询
     *
     * @param param param
     * @return Result
     */
    ResultResponse<Page<SeqDesignPo>> queryPage(PageInDto<SeqDesignPo> param);

    /**
     * 序列修改
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    ResultResponse<ExceptionEnum> update(SeqDesignPo seqDesignPo);

    /**
     * 序列删除
     *
     * @param id id
     * @return Result
     */
    ResultResponse<ExceptionEnum> delete(String id);

    /**
     * 序列发布或者下线
     *
     * @param seqDesignPo seqDesignPo
     * @return Result
     */
    ResultResponse<ExceptionEnum>  issue(SeqDesignPo seqDesignPo);

    /**
     * 批量发布和下线
     *
     * @param seqDesignPos seqDesignPos
     * @return Result
     */
    ResultResponse<ExceptionEnum> batchIssue(List<SeqDesignPo> seqDesignPos);


    /**
     * 通过code获取序列
     *
     * @param map map
     * @return SeqDesignPo
     */
    ResultResponse<SeqDesignPo> getObjectByCode(Map map);

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
    ResultResponse<ExceptionEnum> saveBatch(List<SeqDesignPo> seqDesignList);

    /**
     * 批量更新
     *
     * @param map map
     * @return Integer
     */
    ResultResponse<Integer> updateStatusByIds(Map<String, Object> map);

    /**
     * 批量更新
     *
     * @param seqDesignList seqDesignList
     * @return Integer
     */
    ResultResponse<Integer> updateObjectBatch(List<SeqDesignPo> seqDesignList);

    /**
     * 查询集合
     *
     * @param map
     * @return
     */
    ResultResponse<List<SeqDesignPo>> queryList(Map<String, String> map);

    Result pushRules(List<String> codes);
}
