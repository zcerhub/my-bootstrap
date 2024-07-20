package com.dap.paas.console.basic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.config.UcacConfig;
import com.dap.paas.console.basic.controller.v1.BaseContainerInfoController;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.dao.MachineRoomDao;
import com.dap.paas.console.basic.dao.beijing.DcDcInfoDao;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.utils.UcacUtil;
import com.dap.paas.console.basic.vo.DataCenterVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MachineRoomServiceImpl extends AbstractBaseServiceImpl<MachineRoom, String> implements MachineRoomService {
    private static final Logger log = LoggerFactory.getLogger(BaseContainerInfoController.class);
    @Autowired
    private MachineRoomDao machineRoomDao;
    @Autowired
    private TenantManageDao tenantManageDao;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DcDcInfoDao dcDcInfoDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private UcacConfig ucacConfig;

    @Override
    public Integer delObjectById(String s) {
        Integer result = 0;
        try {
            machineRoomDao.delObjectById(s);
//            result = dcDcInfoDao.delObjectById(s);
        } catch (DaoException e) {
            e.printStackTrace();
            log.error("", e);
        }
        return result;
    }

    @Override
    public Integer saveObject(MachineRoom obj) {
        Integer result = 0;
        try {
            obj.setId(SnowflakeIdWorker.getID());
//            DcDcInfo dcDcInfo = new DcDcInfo();
//            BeanUtils.copyProperties(obj, dcDcInfo);
//            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//            dcDcInfo.setUpdDate(format);
//            dcDcInfo.setCreDate(format);
//            dcDcInfo.setState("1");
//            City BaseCity = cityDao.getObjectById(obj.getCityId());
//            dcDcInfo.setAreaId(Integer.parseInt(BaseCity.getCityCode()));
//            dcDcInfo.setAreaFullName(BaseCity.getCityName());
//            dcDcInfoDao.saveObject(dcDcInfo);
            result = machineRoomDao.saveObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("", e);
        }
        return result;
    }


    @Override
    public Integer updateObject(MachineRoom obj) {
        Integer result = 0;
        try {
//            DcDcInfo dcDcInfo = new DcDcInfo();
//            BeanUtils.copyProperties(obj, dcDcInfo);
//            dcDcInfo.setUpdDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            dcDcInfoDao.updateObject(dcDcInfo);
            result = machineRoomDao.updateObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
            log.error("", e);
        }
        return result;
    }

    @Override
    public boolean syncMachineRoom() {
        String authorization = UserUtil.getAuthorization();
        if (StringUtils.isEmpty(authorization)) {
            throw new ServiceException("authorization is empty");
        }
        TenantManageEntity tenant = tenantManageDao.getObjectById(UserUtil.getUser().getTenantId());
        if (tenant == null || StringUtils.isEmpty(tenant.getTenantCode())) {
            throw new ServiceException("tenant is empty");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);
        ResponseEntity<String> responseEntity = restTemplate.exchange(ucacConfig.getAddress()+ UcacUtil.ROOM_API, HttpMethod.GET,
                new HttpEntity<String>(headers), String.class, tenant.getTenantCode());
        log.info("查询数据中心返回码{},查询数据中心返回数据{}",responseEntity.getStatusCode(),responseEntity.getBody());
        if (!responseEntity.getStatusCode().is2xxSuccessful() || StringUtils.isEmpty(responseEntity.getBody())) {
            throw new ServiceException("get app from ucac failed:" + responseEntity.getStatusCode());
        }

        List<DataCenterVo> dataCenterVOList = JSONObject.parseArray(responseEntity.getBody(), DataCenterVo.class);
        if (CollectionUtils.isEmpty(dataCenterVOList)) {
            return false;
        }
        dataCenterVOList.stream().forEach(chenter->{
            MachineRoom room=new MachineRoom();
            room.setMachineRoomCode(chenter.getDataCenterCode());
            List<MachineRoom> roomList=machineRoomDao.queryList(room);
            if(null ==roomList || roomList.size()==0){
                room.setMachineRoomName(chenter.getDataCenterName());
                room.setDescription(chenter.getLogicDataCenterCode()+"-"+chenter.getLogicDataCenterName());
                  machineRoomDao.saveObject(room);
            }
        });
        return true;
    }


}
