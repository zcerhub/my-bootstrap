package com.base.sys.dict.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.entity.PageRequest;
import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.entity.BaseSysDictionary;
import com.base.sys.api.entity.BaseSysDictionaryInfo;
import com.base.sys.dict.dao.BaseSysDictionaryDao;
import com.base.sys.dict.dao.BaseSysDictionaryInfoDao;
import com.base.sys.dict.service.BaseSysDictionaryInfoService;
import com.base.sys.dict.service.BaseSysDictionaryService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.base.sys.api.common.ResultUtils.success;

/**
 * 系统字典表(BaseSysDictionary)表控制层
 *
 * @author makejava
 * @since 2021-01-21 14:57:10
 */
@RestController
@RequestMapping("/sys/dict")
public class BaseSysDictionaryController {
    /**
     * 服务对象
     */
    @Resource
    private BaseSysDictionaryService baseSysDictionaryService;

    @Resource
    private BaseSysDictionaryDao baseSysDictionaryDao;

    @Resource
    private BaseSysDictionaryInfoService baseSysDictionaryInfoService;

    @Resource
    private BaseSysDictionaryInfoDao baseSysDictionaryInfoDao;

    /**
     * 分页查询
     * @param pageRequest
     * @return
     */
    @PostMapping("/selectPage")
    public Result selectAll(@RequestBody PageRequest<BaseSysDictionary> pageRequest ) {

        return success(this.baseSysDictionaryService.queryPage(pageRequest.getPageNo(),pageRequest.getPageSize(),pageRequest.getRequestObject()));
    }


    /**
     * 新增数据
     *
     * @param baseSysDictionary 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysDictionary baseSysDictionary) {
        return success(this.baseSysDictionaryService.saveObject(baseSysDictionary));
    }

    /**
     * 修改数据
     *
     * @param baseSysDictionary 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysDictionary baseSysDictionary) {

        return success(this.baseSysDictionaryService.updateObject(baseSysDictionary));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestBody JSONObject idList) {
        try {
            if (idList != null) {
                List<String> ids = (List<String>) idList.get("ids");

                for (int i = 0; i < ids.size(); i++) {
                    //刷缓存
                    BaseSysDictionary baseSysDictionary =  baseSysDictionaryService.getObjectById(ids.get(i));
                    if(null != baseSysDictionary){
                        DictMapCache.getCacheMap().remove(baseSysDictionary.getDicCode());
                    }
                    //删除主表数据
                    this.baseSysDictionaryService.delObjectById(ids.get(i));
                    //删除子表数据
                    baseSysDictionaryInfoDao.deleteByDictId(ids.get(i));
                }

            }
            return success();
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }


    }

    /**
     *  根据字典编码查询字典内容  （查询缓存）
     * @param baseSysDictionary
     * @return
     */
    @GetMapping("/getDictValue")
    public Result getDictValue( BaseSysDictionary baseSysDictionary) {
        Map map = new HashMap();
        if (baseSysDictionary != null) {
            String dictCode = baseSysDictionary.getDicCode();
            if(StringUtils.isEmpty(dictCode)){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"dictCode为空");
            }
            map = DictMapCache.get(dictCode);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,map);
    }




    /**
     *  查询所有的字典项
     * @return
     */
    @GetMapping("/getAlllDictValue")
    public Result getAlllDictValue( ) {
        Map<String,Map<String,String >> dictMap = new HashMap();
        BaseSysDictionaryInfo baseSysDictionaryInfo = new BaseSysDictionaryInfo();
        // 查询字典表 查询所有
        List<BaseSysDictionary> baseSysDictionaryList=  baseSysDictionaryService.queryList(new HashMap());
        if(!CollectionUtils.isEmpty(baseSysDictionaryList)){
            for(BaseSysDictionary baseSysDictionary:baseSysDictionaryList){
                Map<String,String> valueMap = new LinkedHashMap();
                // 查询 dictInfo
                baseSysDictionaryInfo.setDictId(baseSysDictionary.getId());
                List<BaseSysDictionaryInfo>   baseSysDictionaryInfos=  baseSysDictionaryInfoService.queryList(baseSysDictionaryInfo);
                if(!CollectionUtils.isEmpty(baseSysDictionaryInfos)){
                    for (BaseSysDictionaryInfo info:baseSysDictionaryInfos){
                        valueMap.put(info.getDicName(),info.getDicValue());
                    }
                }
                dictMap.put(baseSysDictionary.getDicCode(),valueMap) ;
            }
        }
        return ResultUtils.success(ResultEnum.SUCCESS,dictMap);
    }

    @GetMapping("/check/uniqueness/{name}")
    public Result checkUniqueness(@PathVariable("name")String name){
        try {
            if (baseSysDictionaryDao.selectDictCodeTotal(name)>0){
                return ResultUtils.success(ResultEnum.EXIST);
            }else {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }

        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
}
