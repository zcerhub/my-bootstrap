package com.dap.paas.console.basic.service.impl;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.basic.constant.EndpointConsts;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.dao.beijing.DcServerInfoDao;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.entity.beijing.AppSysInfo;
import com.dap.paas.console.basic.entity.beijing.DcServerInfo;
import com.dap.paas.console.basic.service.CityService;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.service.OrganizationService;
import com.dap.paas.console.basic.utils.SshDeployServerUtil;
import com.dap.paas.console.basic.vo.MachineVo;
import com.dap.paas.console.common.constants.BootConstant;
import com.dap.paas.console.common.constants.CharsetConsts;
import com.dap.paas.console.common.util.jsch.JschOperator;
import com.dap.paas.console.common.util.jsch.ShellResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class MachineServiceImpl extends AbstractBaseServiceImpl<Machine,String> implements MachineService {
    @Autowired
    private MachineRoomService machineRoomService;
    @Autowired
    private CityService cityService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private MachineDao machineDao;
    @Autowired
    private DcServerInfoDao dcServerInfoDao;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer delObjectById(String s)  {
        Integer result=0;
        try{
        dcServerInfoDao.delObjectById(s);
        result = machineDao.delObjectById(s);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer saveObject(Machine obj)  {
        Integer result=0;
        try{
            obj.setId(SnowflakeIdWorker.getID());
            DcServerInfo dcServerInfo = new DcServerInfo();
            BeanUtils.copyProperties(obj,dcServerInfo);
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            dcServerInfo.setCreDate(format);
            dcServerInfo.setUpdDate(format);
            dcServerInfo.setInfo("linux centos7");
            dcServerInfo.setTypeCode("1");
            dcServerInfo.setName(obj.getHostIp());
            Integer hostPort = obj.getHostPort();
            dcServerInfo.setPort(hostPort);
            dcServerInfoDao.saveObject(dcServerInfo);
            result = machineDao.saveObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
            }
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer updateObject(Machine obj)  {
        Integer result=0;
        try{
            DcServerInfo dcServerInfo =  new DcServerInfo();
            dcServerInfo.setUpdDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            dcServerInfoDao.updateObject(dcServerInfo);
            result = machineDao.updateObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public boolean checkSsh(Machine machine) throws IOException {
    	JschOperator jr = null;
        try {
			jr = SshDeployServerUtil.getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
			jr.openChannel(JschOperator.CHANNEL_EXEC);
        }
        catch (Exception iae) {
            return false;
        }
        return true;
    }

    @Override
    public MachineVo getMachineInfo(Machine machine) {
        MachineVo machineVo = new MachineVo();
        MachineRoom machineRoom = machineRoomService.getObjectById(machine.getMachineRoomId());
        if(machineRoom == null){
            machineVo.setCity(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setOrganization(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setComputerRoom(EndpointConsts.MACHINE_EXCEPTION_FILLING);
        }
        else {
            City city = cityService.getObjectById(machineRoom.getCityId());
            Organization organization = organizationService.getObjectById(machineRoom.getOrganizationId());
            if(city == null) {
                machineVo.setCity(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            }else {
                machineVo.setCity(city.getCityName());
            }
            if(organization == null) {
                machineVo.setOrganization(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            }else {
                machineVo.setOrganization(organization.getOrganizationName());
            }
            machineVo.setComputerRoom(machineRoom.getMachineRoomName());
        }
        machineVo.setId(machine.getId());
        machineVo.setHostIp(machine.getHostIp());
        machineVo.setHostRemark(machine.getHostRemark());
        machineVo.setAvailable(machine.getAvailable() == 0 ? "不可用" : "可用");
        machineVo.setOsRelease(machine.getOsRelease());
        machineVo.setOsVersion(machine.getOsVersion());
        machineVo.setHostSshAccount(machine.getHostSshAccount());
        machineVo.setHostPort(machine.getHostPort());
        JschOperator jr = null;
        try {
			jr = SshDeployServerUtil.getConnection(machine.getHostIp(), machine.getHostPort(), machine.getHostSshAccount(), machine.getHostSshPassword());
			jr.openChannel(JschOperator.CHANNEL_EXEC);
            String cmd = "free -m | grep Mem|awk '{print $2,$3}'; df -h ./|awk '{print $2,$5}';top -bcn 1| grep '%Cpu(s)'|awk '{print $8 }';uname -r;uname -p;";
            
            synchronized (this) {
                ShellResult resultExec = jr.executeExec(cmd);
                List<String> result = Arrays.asList(StringUtils.split(resultExec.getShellOutPut(), "\n"));
                String[] mem = result.get(0).split(" ");
                long all = Long.parseLong(mem[0]);
                long used = Long.parseLong(mem[1]);
                float l =(float) used / all;
                float memF = (float) (Math.round(l*10000)/100);
                machineVo.setMemory(all+"M-"+memF+"%");

                if(result.size() >= 3) {
                    String[] disk = result.get(2).split(" ");
                    machineVo.setDisk(disk[0] + "-" + disk[1]);
                }
                else {
                    machineVo.setDisk(EndpointConsts.MACHINE_EXCEPTION_FILLING);
                }

                if(result.size() >= 4) {
                    float cores = 0.0f;
                    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
                    if (pattern.matcher(result.get(3)).matches()) {
                        cores = Float.parseFloat(result.get(3));
                        float cpuF = 100-cores;
                        machineVo.setCpu(String.format("%.1f",cpuF) + "%");
                    }
                    else {
                        machineVo.setCpu(EndpointConsts.MACHINE_EXCEPTION_FILLING);
                    }
                }
                else {
                    machineVo.setCpu(EndpointConsts.MACHINE_EXCEPTION_FILLING);
                }

                if(result.size() >= 6) {
                    machineVo.setOsVersion(result.get(4));
                    if (result.get(5).contains("aarch")) {
                        machineVo.setCoreArch(BootConstant.CPU_ARCHITECTURE_ARM);
                    }
                    else {
                        machineVo.setCoreArch(BootConstant.CPU_ARCHITECTURE_X86);
                    }
                }
                else {
                    machineVo.setCoreArch(BootConstant.CPU_ARCHITECTURE_X86);
                }
            }
        }
        catch (Exception e) {
            machineVo.setMemory(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setDisk(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setCpu(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setOsVersion(EndpointConsts.MACHINE_EXCEPTION_FILLING);
            machineVo.setCoreArch(EndpointConsts.MACHINE_EXCEPTION_FILLING);
        }
        return machineVo;
    }

    @Override
    public Integer updateByUnitId(String id) throws Exception {
        Integer result = machineDao.updateByUnitId(id);
        return result;
    }

    private List<String> getStdOut(InputStream in) {
        InputStream stdOut = new StreamGobbler(in);
        StringBuffer sb = new StringBuffer();
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdOut, CharsetConsts.DEFAULT_ENCODING));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
