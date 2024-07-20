package com.dap.paas.console.basic.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.dao.MachineRoomDao;
import com.dap.paas.console.basic.dao.beijing.DcDcInfoDao;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.service.MachineRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MachineRoomServiceImpl extends AbstractBaseServiceImpl<MachineRoom, String> implements MachineRoomService {
    @Autowired
    private MachineRoomDao machineRoomDao;
    @Autowired
    private DcDcInfoDao dcDcInfoDao;
    @Autowired
    private CityDao cityDao;

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
}
