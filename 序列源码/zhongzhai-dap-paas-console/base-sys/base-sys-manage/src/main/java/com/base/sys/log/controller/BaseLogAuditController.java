package com.base.sys.log.controller;

import com.base.api.dto.Page;
import com.base.api.entity.PageRequest;
import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.BaseLogAudit;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.async.service.BaseLogAuditAsyncService;
import com.base.sys.auth.log.OperateTypeAspect;
import com.base.sys.log.service.BaseLogAuditService;
import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.utils.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志审计表(BaseLogAudit)表控制层
 *
 * @author makejava
 * @since 2021-01-14 09:40:21
 */
@RestController
@RequestMapping("baseLogAudit")
public class BaseLogAuditController {
    /**
     * 服务对象
     */
    @Resource
    private BaseLogAuditService baseLogAuditService;
    @Autowired
    private TenantManageDao tenantManageDao;
    @Resource
    private BaseSysUserService baseSysUserService;


    private static final Logger log = LoggerFactory.getLogger(BaseLogAuditController.class);

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("queryPage")
    public Result queryPage(@RequestBody PageRequest<BaseLogAudit> pageRequest) {
        BaseLogAudit requestObject = pageRequest.getRequestObject();
        Page page = baseLogAuditService.queryPage(pageRequest.getPageNo(), pageRequest.getPageSize(), requestObject);
        return ResultUtils.success(ResultEnum.SUCCESS, page);
    }

    @GetMapping("selectAll")
    public Result selectAll(@RequestBody BaseLogAudit baseLogAudit) {
        log.info("BaseSysUserController.selectAll() param: page " + baseLogAudit.toString());
        Map<String, Object> paraMap = new HashMap<>();
        List<BaseLogAudit> baseLogAudits = null;
        try {
            paraMap.put("componentType", OperateTypeAspect.BASE);
            baseLogAudits = baseLogAuditService.queryList(paraMap);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("查询失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(baseLogAudits.toString());
        return ResultUtils.success(ResultEnum.SUCCESS, baseLogAudits);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public Result selectOne(@PathVariable String id) {
        log.info("BaseSysUserController.selectOne() param: id:" + id);
        BaseLogAudit baseLogAudit = null;
        try {
            baseLogAudit = baseLogAuditService.getObjectById(id);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("查询失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, baseLogAudit);
    }

    @Autowired
    BaseLogAuditAsyncService operateLogAsyncService;

    /**
     * 新增数据
     *
     * @param baseLogAudit 实体对象
     * @return 新增结果
     */
    @PostMapping("insert")
    public Result insert(@RequestBody BaseLogAudit baseLogAudit, HttpServletRequest request) {

        log.info("BaseSysUserController.insert() param: baseSysUser :" + baseLogAudit.toString());
        try {
            Map<Object, Object> map = new HashMap<>();
            String tenantId = "";
            if (ObjectUtils.isEmpty(baseLogAudit.getTenantId())) {
                tenantId = tenantManageDao.getIdByCodeOnLogin(baseLogAudit.getTenantCode());
            } else {
                tenantId = baseLogAudit.getTenantId();
            }
            map.put("tenantId", tenantId);
            if (ObjectUtils.isEmpty(baseLogAudit.getUserName())) {
                return ResultUtils.error(206, "请求缺少用户信息，请添加");
            }
            map.put("name", baseLogAudit.getUserName());
            BaseSysUser objectByMap = baseSysUserService.getObjectByMap(map);
            AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
            BeanUtils.copyProperties(objectByMap, authenticationUserDto);
            ThreadLocalUtil.put(SysConstant.USER_INFO, authenticationUserDto);
            baseLogAudit.setCreateUserId(objectByMap.getId());
            baseLogAudit.setOperatorUserId(objectByMap.getId());
            baseLogAudit.setOperatorUserName(baseLogAudit.getUserName());
            baseLogAudit.setOperateIp(objectByMap.getId());
            baseLogAudit.setId(SnowflakeIdWorker.getID());
            baseLogAudit.setTenantId(tenantId);
            baseLogAuditService.saveObject(baseLogAudit);
//            operateLogAsyncService.asyncLogAudit(baseLogAudit);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("新增失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }
//
//    /**
//     * 修改数据
//     *
//     * @param baseLogAudit 实体对象
//     * @return 修改结果
//     */
//    @PutMapping("update")
//    public Result update(@RequestBody BaseLogAudit baseLogAudit) {
//        log.info("BaseSysUserController.update() params :"+baseLogAudit.toString());
//        try {
//            baseLogAuditService.updateObject(baseLogAudit);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            log.error("修改失败",e.getMessage());
//            return ResultUtils.error(ResultEnum.FAILED);
//        }
//        return ResultUtils.success(ResultEnum.SUCCESS);
//    }
//
//    /**
//     * 删除数据
//     * @return 删除结果
//     */
//    @DeleteMapping("deleteAll")
//    public Result delete(@RequestBody String input) {
//        log.info("BaseSysUserController.delete() param: idList "+input.toString());
//        try {
//            JSONObject input1 = JSON.parseObject(input);
//            List<String> idList = (List) input1.get("idList");
//            baseLogAuditService.delObjectByIds(idList);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            log.error("删除失败",e.getMessage());
//            return ResultUtils.error(ResultEnum.FAILED);
//        }
//        return ResultUtils.success(ResultEnum.SUCCESS);
//    }
}
