package com.dap.paas.console.seq.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.common.util.ExcelUtil;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.base.*;
import com.dap.paas.console.seq.feign.SeqDesignFeignApi;
import com.dap.paas.console.seq.service.SeqDesignService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * @className SeqDesignServiceImpl
 * @description 序列配置实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqDesignServiceImpl implements SeqDesignService {
    @Autowired
    private SeqDesignFeignApi seqDesignFeignApi;

    private static final Map<String, String> EXCEL_TITLE = new LinkedHashMap<>();




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
        ResultResponse<Integer> integerResultResponse = seqDesignFeignApi.queryApplicationTotal();
        return integerResultResponse.getData();
    }

    @Override
    public Integer queryDesignTotal() {
        ResultResponse<List<SeqDesignPo>> listResultResponse = seqDesignFeignApi.queryList(new SeqDesignPo());
        return Optional.ofNullable(listResultResponse.getData()).map(List::size).orElse(0);
    }

    @Override
    public void exportExcel(HttpServletResponse response, List<SeqDesignPo> seqDesignList) {

        // 需要写入excel的属性及映射字段名
        Map<String, String> keyMap = MapUtil.inverse(EXCEL_TITLE);

        // 创建sheet，属性映射字段名作为首行名
        Workbook wb = ExcelUtil.creatExcelSheets(Collections.singletonList("1"), new ArrayList<>(keyMap.values()));

        // 根据sheetName写入数据
        ExcelUtil.putData(wb, "1", ExcelUtil.convertListToMapList(seqDesignList, keyMap, SeqDesignPo.class));

        // 下载excel
        ExcelUtil.writeExcel(wb, response, "序列设计管理");
    }

    @Override
    public List<SeqDesignPo> readExcel(MultipartFile file) {

        // 读取excel数据转成对应对象数据集
        return ExcelUtil.readExcelToList(file, EXCEL_TITLE, SeqDesignPo.class, (cell) -> {
            cell.setCellType(STRING);
            String val = cell.getStringCellValue();
            if (val.startsWith("[") && val.endsWith("]")) {
                JSONArray array = JSON.parseArray(val);
                List<Rule> data = new ArrayList<>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String type = obj.getString("ruleType");
                    JSONObject baseInfo = obj.getJSONObject("ruleData");
                    Rule rule = obj.toJavaObject(Rule.class);
                    switch (type) {
                        case "1":
                            StringRuleInfo stringRuleInfo = baseInfo.toJavaObject(StringRuleInfo.class);
                            rule.setRuleData(stringRuleInfo);
                            break;
                        case "2":
                            EnumRuleInfo enumRuleInfo = baseInfo.toJavaObject(EnumRuleInfo.class);
                            rule.setRuleData(enumRuleInfo);
                            break;
                        case "3":
                            DateRuleInfo dateRuleInfo = baseInfo.toJavaObject(DateRuleInfo.class);
                            rule.setRuleData(dateRuleInfo);
                            break;
                        case "4":
                            NumberRuleInfo numberRuleInfo = baseInfo.toJavaObject(NumberRuleInfo.class);
                            rule.setRuleData(numberRuleInfo);
                            break;
                        case "5":
                            SpecialCharRuleInfo specialCharRuleInfo = baseInfo.toJavaObject(SpecialCharRuleInfo.class);
                            rule.setRuleData(specialCharRuleInfo);
                            break;
                        case "6":
                            OptionalRuleInfo optionalRuleInfo = baseInfo.toJavaObject(OptionalRuleInfo.class);
                            rule.setRuleData(optionalRuleInfo);
                            break;
                        case "7":
                            CheckRuleInfo checkRuleInfo = baseInfo.toJavaObject(CheckRuleInfo.class);
                            rule.setRuleData(checkRuleInfo);
                            break;
                        default:
                            break;
                    }
                    data.add(rule);
                }
                return data;
            }
            return val;
        });
    }

    @Override
    public ResultResponse<ExceptionEnum> saveBatch(List<SeqDesignPo> seqDesignList) {
        return seqDesignFeignApi.saveBatch(seqDesignList);
    }

    @Override
    public ResultResponse<SeqDesignPo> getObjectByCode(Map map) {
        return seqDesignFeignApi.getObjectByCode(map);
    }

    @Override
    public ResultResponse<Integer> updateStatusByIds(Map<String, Object> paramMap) {
        paramMap.put("updateDate", new Date());
        return seqDesignFeignApi.updateStatusByIds(paramMap);
    }

    @Override
    public ResultResponse<Integer> updateObjectBatch(List<SeqDesignPo> seqDesignList) {
        return seqDesignFeignApi.updateObjectBatch(seqDesignList);
    }

    @Override
    public ResultResponse<List<SeqDesignPo>> queryList(Map<String, String> map) {
        return seqDesignFeignApi.queryListByMap(map);
    }



    @Override
    public ResultResponse<ExceptionEnum> insert(SeqDesignPo seqDesignPo) {
        return seqDesignFeignApi.insert(seqDesignPo);
    }

    @Override
    public ResultResponse<Page<SeqDesignPo>> queryPage(PageInDto<SeqDesignPo> param) {
        return seqDesignFeignApi.queryPage(param);
    }

    @Override
    public ResultResponse<ExceptionEnum> update(SeqDesignPo seqDesignPo) {
        return seqDesignFeignApi.update(seqDesignPo);
    }

    @Override
    public ResultResponse<ExceptionEnum> delete(String id) {
        return seqDesignFeignApi.delete(id);
    }

    @Override
    public ResultResponse<ExceptionEnum> issue(SeqDesignPo seqDesignPo) {
        return seqDesignFeignApi.issue(seqDesignPo);
    }

    @Override
    public ResultResponse<ExceptionEnum> batchIssue(List<SeqDesignPo> seqDesignPos) {
        return seqDesignFeignApi.batchIssue(seqDesignPos);
    }

    @Override
    public Result pushRules(List<String> codes) {
        return seqDesignFeignApi.pushRules(codes);
    }
}
