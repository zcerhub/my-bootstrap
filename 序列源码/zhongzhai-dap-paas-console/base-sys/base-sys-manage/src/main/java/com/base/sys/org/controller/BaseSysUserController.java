package com.base.sys.org.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.dto.UpdatePasswordDto;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.org.dao.BaseSysUserDao;
import com.base.sys.org.service.BaseSysUserService;
import com.base.sys.utils.VerCodeImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表(BaseSysUser)表控制层
 *
 * @author makejava
 * @since 2021-01-14 09:32:52
 */
@RestController
@RequestMapping("baseSysUser")
public class BaseSysUserController  {
    private static final Logger log= LoggerFactory.getLogger(BaseSysUserController.class);
    /**
     * 服务对象
     */
    @Resource
    private BaseSysUserService baseSysUserService;

    @Resource
    private BaseSysUserDao baseSysUserDao;

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("queryPage")
    public Result queryPage( @RequestBody BaseSysUser baseSysUser) {
        log.info("BaseSysUserController.queryPage() param: page "+baseSysUser.toString());
        Page page=null;
        try {
            if(StringUtils.isEmpty(baseSysUser)){
                baseSysUser=new BaseSysUser();
            }
            baseSysUser.setIsDelete("0");
            page = baseSysUserService.queryUserPage(baseSysUser);
        } catch (Exception e) {
            log.error("查询所有结果失败：",e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(page.toString());
        return ResultUtils.success(ResultEnum.SUCCESS,page);
    }


    @GetMapping("selectAll")
    public Result selectAll() {
        log.info("BaseSysUserController.selectAll() param: baseSysUser ");
        List<BaseSysUser> baseSysUsers=null;
        try {
            baseSysUsers = baseSysUserService.getUserList(null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询所有失败",e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(baseSysUsers.toString());
        return ResultUtils.success(ResultEnum.SUCCESS,baseSysUsers);
    }
    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Result<BaseSysUser> selectOne(String id) {
        log.info("BaseSysUserController.selectOne() param: id:"+id);
        BaseSysUser user=null;
        try {
            user = baseSysUserService.getUserById(id);
            String decrypt = null;
            user.setPassword(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询一个失败：",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS,user);
    }





    /**
     * 新增数据
     * @return 新增结果
     */
    @PostMapping("insert")
    public Result<BaseSysUser> insert(@RequestBody BaseSysUser user, HttpServletRequest httpServletRequest) {
        log.info("BaseSysUserController.insert() param: baseSysUser :"+user.toString());
        try {
            user.setIsDelete("0");
            if (baseSysUserDao.selectAccountTotal(user.getAccount()) > 0) { //判断账号 账号是否存在   验证唯一性
                return ResultUtils.error(ResultEnum.EXIST);
            } else {
                HttpSession session = httpServletRequest.getSession();
                AuthenticationUserDto authenticationUserDTo = (AuthenticationUserDto) session.getAttribute(SysConstant.USER_INFO);
                user.setTenantId(authenticationUserDTo.getTenantId());
                baseSysUserService.saveUser(user);
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
            //添加组织机构关联信息 角色关联信息
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增失败：",e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    /**
     * 修改数据
     * @return 修改结果
     */
    @PutMapping("update")
    public Result<BaseSysUser> update( @RequestBody BaseSysUser userInDto) {
        log.info("BaseSysUserController.update() params :"+userInDto.toString());
        try {
            String aesPwd = null;
            userInDto.setPassword(aesPwd);
            baseSysUserService.updateObject(userInDto);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("修改失败：",e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("delete")
    public Result delete(@RequestParam("idList") List<String> idList) {
        log.info("BaseSysUserController.delete() param: idList "+idList.toString());
        try {
            baseSysUserService.delObjectByIds(idList);
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("删除失败：",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }
//    假删除
    @DeleteMapping("deleteAll")
    public Result deleteOne( @RequestBody JSONObject json) {
        log.info("BaseSysUserController.delete() param:  "+json.toString());
        try {
            List<String> idList = (List) json.get("idList");
            for (int i = 0; i < idList.size(); i++) {
                BaseSysUser baseSysUser = new BaseSysUser();
                baseSysUser.setIsDelete("1");
                baseSysUser.setId(idList.get(i));
                baseSysUserService.updateObject(baseSysUser);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("删除失败：",e.getStackTrace());
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }
    @GetMapping("/check/uniqueness/{name}")
    public Result checkUniqueness(@PathVariable("name")String name) {
        try {
            if (baseSysUserDao.selectAccountTotal(name) > 0){
                return ResultUtils.success(ResultEnum.EXIST);
            }else {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }


    }

   @PostMapping("/update/password")
   public Result updatePasswoed(ServletRequest servletRequest,@RequestBody UpdatePasswordDto updatePasswordDto) {

        return baseSysUserService.updatePassword(servletRequest,updatePasswordDto);
   }

    @GetMapping("/getVarCode")
    public Result<Map> getVarCode(ServletRequest servletRequest) {
        String verifyCode= (String) servletRequest.getAttribute(VerCodeImgUtil.VERIFY_CODE_KEY);
        if(verifyCode!=null){
            VerCodeImgUtil.getVerifyCode(verifyCode);
        }
        String varCode= VerCodeImgUtil.getRandomString(4);
        String base64= VerCodeImgUtil.getVerCodeImage(varCode);
        Map<String,Object> map=new HashMap<>();
        map.put("varCode",base64);
        map.put(VerCodeImgUtil.VERIFY_CODE_KEY,varCode);
        return ResultUtils.success(ResultEnum.SUCCESS,map);
    }

}
