package com.dap.paas.console.seq.feign;

import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yzp
 * @date 2024/6/21 0021 9:18
 */
@FeignClient(name = "${dap.sequence.seqServerName:seq-cluster}", contextId = "seqDesignFeignApi", url = "${dap.sequence.urls:}")
public interface SeqDesignFeignApi {

    /**
     * 序列新增
     *
     * @param seqDesignPo seqDesignPo
     * @return
     */
    @PostMapping("/v1/seq/design/insert")
    ResultResponse<ExceptionEnum> insert(@RequestBody SeqDesignPo seqDesignPo);

    /**
     * 分页查询当前值
     *
     * @param param param
     * @return
     */
    @PostMapping("/v1/seq/design/queryPage")
    ResultResponse<Page<SeqDesignPo>> queryPage(@RequestBody PageInDto<SeqDesignPo> param);

    /**
     * 序列修改
     *
     * @param seqDesignPo seqDesignPo
     * @return
     */
    @PostMapping("/v1/seq/design/update")
    ResultResponse<ExceptionEnum> update(@RequestBody SeqDesignPo seqDesignPo);

    /**
     * 序列删除
     *
     * @param id id
     * @return
     */
    @PostMapping("/v1/seq/design/delete/{id}")
    ResultResponse<ExceptionEnum> delete(@PathVariable("id") String id);

    /**
     * 序列发布或者下线
     *
     * @param seqDesignPo seqDesignPo
     * @return
     */
    @PostMapping("/v1/seq/design/issue")
    ResultResponse<ExceptionEnum> issue(@RequestBody SeqDesignPo seqDesignPo);

    /**
     * 序列批量发布或者下线
     *
     * @param seqDesignPos seqDesignPos
     * @return
     */
    @PostMapping("/v1/seq/design/batchIssue")
    ResultResponse<ExceptionEnum> batchIssue(@RequestBody List<SeqDesignPo> seqDesignPos);

    /**
     * 序列批量保存
     *
     * @param seqDesignList dataList
     * @return Result
     */
    @PutMapping("/v1/seq/design/saveBatch")
    ResultResponse<ExceptionEnum> saveBatch(List<SeqDesignPo> seqDesignList);

    /**
     * id集合查询序列
     *
     * @param ids
     * @return
     */
    @PostMapping("/v1/seq/design/queryListByIds")
    ResultResponse<List<SeqDesignPo>> queryListByIds(Set<String> ids);

    /**
     * 批量更新
     *
     * @param seqDesignList
     * @return
     */
    @PostMapping("/v1/seq/design/updateObjectBatch")
    ResultResponse<Integer> updateObjectBatch(List<SeqDesignPo> seqDesignList);

    /**
     * 批量更新状态
     *
     * @param paramMap
     * @return
     */
    @PostMapping("/v1/seq/design/updateStatusByIds")
    ResultResponse<Integer> updateStatusByIds(Map<String, Object> paramMap);

    /**
     * 根据code查询
     *
     * @param map
     * @return
     */
    @PostMapping("/v1/seq/design/getObjectByCode")
    ResultResponse<SeqDesignPo> getObjectByCode(Map map);

    /**
     * 查询集合
     *
     * @param seqDesignPo
     * @return
     */
    @PostMapping("/v1/seq/design/queryList")
    ResultResponse<List<SeqDesignPo>> queryList(SeqDesignPo seqDesignPo);

    /**
     * 查询关联应用数量
     *
     * @return
     */
    @PostMapping("/v1/seq/design/queryApplicationTotal")
    ResultResponse<Integer> queryApplicationTotal();

    @PostMapping("/v1/seq/design/queryListByMap")
    ResultResponse<List<SeqDesignPo>> queryListByMap(Map<String, String> map);
    @PostMapping("/v1/seq/design/pushRules")
    Result pushRules(List<String> codes);
}
