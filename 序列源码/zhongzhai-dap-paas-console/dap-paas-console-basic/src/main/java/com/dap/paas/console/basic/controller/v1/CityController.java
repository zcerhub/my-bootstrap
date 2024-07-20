package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.service.CityService;
import com.dap.paas.console.basic.service.MachineRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区信息管理
 *
 */
@Api(tags = "基础信息-地区信息管理")
@RestController
@RequestMapping("/v1/basic/city")
public class CityController {

    private static final Logger log= LoggerFactory.getLogger(CityController.class);
    @Autowired
    private CityService cityService;
    @Autowired
    private MachineRoomService machineRoomService;

    @PostMapping("/insert")
    public Result insert(@RequestBody City city) {
        try {
            List<City> cityList = cityService.queryList(new HashMap());
            for(City c:cityList){
                if(c.getCityCode().equals(city.getCityCode()) || c.getCityName().equals(city.getCityName())){
                    return ResultUtils.error(ResultEnum.FAILED.getCode(),"已存在");
                }
            }
            cityService.saveObject(city);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增地区失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            City city = cityService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS,city);
        } catch (ServiceException e) {
            log.error("查询地区失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("查询所有的地区")
    @PostMapping("/queryList")
    public Result queryList() {
        Map param = new HashMap<>();
        try {
            List<City> cityList = cityService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, cityList);
        } catch (ServiceException e) {
            log.error("查询地区失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<City> param) {
        try {
            Page citys = cityService.queryPage(param.getPageNo(),param.getPageSize(), param.getRequestObject());
            return ResultUtils.success(ResultEnum.SUCCESS, citys);
        } catch (ServiceException e) {
            log.error("查询分页地区失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody City city) {
        try {
            List<City> cityList = cityService.queryList(new HashMap());
            for(City c:cityList){
                if((!c.getId().equals(city.getId())) && (c.getCityCode().equals(city.getCityCode()) || c.getCityName().equals(city.getCityName()))){
                    return ResultUtils.error(ResultEnum.FAILED.getCode(),"已存在");
                }
            }
            cityService.updateObject(city);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新地区失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            Map map = new HashMap();
            map.put("cityId",id);
            List<MachineRoom> machineRoomList = machineRoomService.queryList(map);
            if(!machineRoomList.isEmpty()){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"该地区有机房，不能删除");
            }
            cityService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除地区失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            cityService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除地区失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
