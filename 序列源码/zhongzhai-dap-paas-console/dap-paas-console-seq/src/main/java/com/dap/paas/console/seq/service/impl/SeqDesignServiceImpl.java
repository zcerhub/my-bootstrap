package com.dap.paas.console.seq.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.common.util.ExcelUtil;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dao.SeqDesignDao;
import com.dap.paas.console.seq.entity.SeqDesignPo;
import com.dap.paas.console.seq.entity.SeqInstanceRule;
import com.dap.paas.console.seq.entity.base.*;
import com.dap.paas.console.seq.service.SeqDesignService;
import com.dap.paas.console.seq.service.SeqInstanceRuleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
    public Result saveBatch(List<SeqDesignPo> seqDesignList) {
        for (SeqDesignPo seqDesignPo : seqDesignList) {
            if (StringUtils.isBlank(seqDesignPo.getSequenceNumber())) {
                return ResultUtils.error("生成数量不能为空！！");
            }
            if (Integer.parseInt(seqDesignPo.getSequenceNumber()) > 50000 || Integer.parseInt(seqDesignPo.getSequenceNumber()) < 1) {
                return ResultUtils.error("生成数量不能超过50000并且不能小于1！！");
            }

            List<SeqDesignPo> list = seqDesignDao.queryList(new HashMap<>());
            for (SeqDesignPo designPo : list) {
                if (designPo.getSequenceName().equals(seqDesignPo.getSequenceName()) || designPo.getSequenceCode().equals(seqDesignPo.getSequenceCode())) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "序列已存在");
                }
            }
            seqDesignPo.setSequenceStatus(SeqConstant.SEQ_SERVER_ONE);
            this.saveObject(seqDesignPo);
            // 保存规则
            List<Rule> ruleList = seqDesignPo.getRuleInfos();
            for (int i = 0; i < ruleList.size(); i++) {
                Rule rule = ruleList.get(i);
                SeqInstanceRule seqInstanceRule = new SeqInstanceRule();
                seqInstanceRule.setSeqDesignId(seqDesignPo.getId());
                seqInstanceRule.setInstanceRuleName(rule.getRuleName());
                seqInstanceRule.setInstanceRuleType(rule.getRuleType());
                seqInstanceRule.setInstanceRuleInfo(rule.getRuleData().toString());
                seqInstanceRule.setSortNo(i + 1);
                seqInstanceRuleService.saveObject(seqInstanceRule);
            }
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
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
}
