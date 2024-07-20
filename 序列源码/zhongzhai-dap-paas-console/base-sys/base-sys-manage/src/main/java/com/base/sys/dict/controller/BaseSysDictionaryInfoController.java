package com.base.sys.dict.controller;


import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.DictDTO;
import com.base.sys.api.entity.BaseSysDictionary;
import com.base.sys.api.entity.BaseSysDictionaryInfo;
import com.base.sys.dict.dao.BaseSysDictionaryInfoDao;
import com.base.sys.dict.service.BaseSysDictionaryInfoService;
import com.base.sys.dict.service.BaseSysDictionaryService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.base.sys.api.common.ResultUtils.success;

/**
 * 字典详情(BaseSysDictionaryInfo)表控制层
 *
 * @author makejava
 * @since 2021-01-21 14:58:07
 */
@RestController
@RequestMapping("/sys/dictInfo")
public class BaseSysDictionaryInfoController {
    /**
     * 服务对象
     */
    @Resource
    private BaseSysDictionaryInfoService baseSysDictionaryInfoService;

    @Resource
    private BaseSysDictionaryService baseSysDictionaryService;

    @Resource
    private BaseSysDictionaryInfoDao baseSysDictionaryInfoDao;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectAll/{id}")
    public Result selectAll(@PathVariable("id") String id) {
        Map map = new HashMap(1);
        map.put("dictId",id);
        return success( this.baseSysDictionaryInfoService.queryList(map));
    }


    /**
     * 根据对象查询数据
     * @param baseSysDictionary
     * @return
     */
    @PostMapping("/selectAll")
    public Result selectAll(@RequestBody BaseSysDictionary baseSysDictionary){
        Map map = new HashMap(1);
        map.put("dictId",baseSysDictionary.getId());
        return success( this.baseSysDictionaryInfoService.queryList(map));
    }

    /**
     * 新增数据
     * @param dictDto
     * @return
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody DictDTO dictDto) {
        if (dictDto != null) {
            String id = dictDto.getId();
            //用来刷缓存用
            Map<String,String>  cacheMap = new HashMap<>();

            List<BaseSysDictionaryInfo> baseSysDictionaryInfoList = dictDto.getBaseSysDictionaryInfoList();
            if (baseSysDictionaryInfoList != null) {
                for (int i = 0; i <baseSysDictionaryInfoList.size() ; i++) {
                    BaseSysDictionaryInfo esbSysDictionaryInfo = baseSysDictionaryInfoList.get(i);
                    esbSysDictionaryInfo.setDictId(id);
                    if(!StringUtils.isEmpty(esbSysDictionaryInfo.getId())){
                        continue;
                    }
                    if(baseSysDictionaryInfoService.saveObject(esbSysDictionaryInfo)==1){
                        cacheMap.put(esbSysDictionaryInfo.getDicName(),esbSysDictionaryInfo.getDicValue());
                    }
                }
            }
            BaseSysDictionary baseSysDictionary =  baseSysDictionaryService.getObjectById(id);
            if(null != baseSysDictionary){
                DictMapCache.getCacheMap().put(baseSysDictionary.getDicCode(),cacheMap) ;
            }
        }
        return success();
    }

    /**
     * 修改数据
     * @param dictDto
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody DictDTO dictDto) {
        try {
            if (dictDto != null) {
                String id = dictDto.getId();
                List<BaseSysDictionaryInfo> baseSysDictionaryInfoList = dictDto.getBaseSysDictionaryInfoList();
                if (baseSysDictionaryInfoList != null) {

                    //先删除所有数据
                    baseSysDictionaryInfoDao.deleteByDictId(id);

                    //用来刷缓存用
                    Map<String, String> cacheMap = new HashMap<>();
                    for (int i = 0; i < baseSysDictionaryInfoList.size(); i++) {
                        BaseSysDictionaryInfo baseSysDictionaryInfo = baseSysDictionaryInfoList.get(i);
                        baseSysDictionaryInfo.setDictId(id);
                        if (baseSysDictionaryInfoService.saveObject(baseSysDictionaryInfo) == 1) {
                            cacheMap.put(baseSysDictionaryInfo.getDicName(), baseSysDictionaryInfo.getDicValue());
                        }
                    }
                    BaseSysDictionary baseSysDictionary = baseSysDictionaryService.getObjectById(id);
                    if (null != baseSysDictionary) {
                        DictMapCache.getCacheMap().put(baseSysDictionary.getDicCode(), cacheMap);
                    }
                }
            }
            return ResultUtils.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }




}
