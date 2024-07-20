package com.base.sys.menu.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.*;
import com.base.sys.api.dto.*;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.async.aspect.AsynSelectMenuAnnotation;
import com.base.sys.async.service.AsyncCacheService;
import com.base.sys.auth.constant.TokenConstant;
import com.base.sys.auth.token.util.JwtUtil;
import com.base.sys.menu.service.BaseSysDataRuleService;
import com.base.sys.menu.service.BaseSysMenuService;
import com.base.sys.menu.service.BaseSysOperateService;
import com.base.sys.utils.ThreadLocalUtil;
import com.base.sys.utils.UserUtil;
import com.dap.dmc.client.entity.ThirdMenuVo;
import com.dap.dmc.client.service.DmcPermissService;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author QuJing
 * @date 2021/1/14
 */
@RestController
@RequestMapping("/baseSysMenuEntity")
public class BaseSysMenuController {


    private static final Logger log = LoggerFactory.getLogger(
            BaseSysMenuController.class);

    @Resource
    private BaseSysMenuService baseSysMenuService;

    @Resource
    private BaseSysOperateService baseSysOperateService;

    @Resource
    private BaseSysDataRuleService baseSysDataRuleService;

    @Lazy
    @Autowired
    private AsyncCacheService asyncCacheService;


    @Autowired
    private DmcPermissService ucacPermissService;

    /**
     * 根据token和应用编码查询菜单按钮
     */
    @GetMapping("/selectOperate")
    public Result selectOperate(String token, String clientCode) {
        List<SysAppMenuDto> sysButtonList = new ArrayList<>();
        //  菜单按钮List结构
        List<ThirdMenuVo> buttonVOList = ucacPermissService.selectNavAndButternList(token, clientCode, "");
        for (ThirdMenuVo thirdMenuVo : buttonVOList) {
            if (thirdMenuVo.getMenuType().equals("F")) {
                SysAppMenuDto sysAppMenuDtos = new SysAppMenuDto();
                sysAppMenuDtos.setId(thirdMenuVo.getMenuId());
                sysAppMenuDtos.setParentId(thirdMenuVo.getParentId());
                sysAppMenuDtos.setRoutePath(thirdMenuVo.getPath());
                sysAppMenuDtos.setSort(thirdMenuVo.getOrderNum());
                sysAppMenuDtos.setText(thirdMenuVo.getMenuName());
                sysButtonList.add(sysAppMenuDtos);
            }

        }
        return ResultUtils.success(ResultEnum.SUCCESS, sysButtonList);
    }

    /**
     * 根据token和应用编码查询菜单按钮
     */
    @GetMapping("/selectMenu")
    public Result selectMenu(@RequestHeader("Authorization") String auth, String moduleCode) {
        String token=JwtUtil.getToken(auth);
        Claims claims = JwtUtil.parseJWT(token);
        if (null == claims) {
            return ResultUtils.error(ResultEnum.FAILED);
        }
        String authorization=claims.get(TokenConstant.AUTHORIZATION, String.class);
        if(!StringUtils.hasText(authorization)  ){
            try {
                List<SysMenuDto> list = baseSysMenuService.getAllMenuByAppCode(moduleCode);
                return ResultUtils.success(ResultEnum.SUCCESS, list);
            } catch (DaoException e) {
                log.error("查询失败", e);
                return ResultUtils.error(ResultEnum.FAILED);
            }
        }
        token=authorization;
        List<SysAppMenuDto> sysAppMenuDtoTreeList = new ArrayList<>();

        List<SysAppMenuDto> sysAppMenuDtoList = new ArrayList<>();
        //  菜单list结构
        List<ThirdMenuVo> menuVOList = ucacPermissService.selectNavList(token, moduleCode, "");
        //  菜单按钮List结构
        List<ThirdMenuVo> buttonVOList = ucacPermissService.selectNavAndButternList(token, moduleCode, "");

        for (ThirdMenuVo thirdMenuVo : menuVOList) {
            SysAppMenuDto sysAppMenuDtos = new SysAppMenuDto();
            List<String> sysOperateSignList = new ArrayList<>();
            sysAppMenuDtos.setId(thirdMenuVo.getMenuId());
            sysAppMenuDtos.setParentId(thirdMenuVo.getParentId());
            sysAppMenuDtos.setRoutePath(thirdMenuVo.getPath());
            sysAppMenuDtos.setSort(thirdMenuVo.getOrderNum());
            sysAppMenuDtos.setText(thirdMenuVo.getMenuName());
            for (ThirdMenuVo thirdMenuVoButton : buttonVOList) {
                if (thirdMenuVoButton.getMenuType().equals("F") && thirdMenuVoButton.getParentId().equals(thirdMenuVo.getMenuId())) {
                    sysOperateSignList.add(thirdMenuVoButton.getPerms());
                }
            }
            sysAppMenuDtos.setCodeList(sysOperateSignList);

            sysAppMenuDtoList.add(sysAppMenuDtos);
        }
        // 整理对象,生成树
        if (CollectionUtils.isNotEmpty(sysAppMenuDtoList)) {
            for (int i = 0; i < sysAppMenuDtoList.size(); i++) {
                // 一级菜单没有parentId
                if (sysAppMenuDtoList.get(i).getParentId().equals("0")) {
                    sysAppMenuDtoTreeList.add(sysAppMenuDtoList.get(i));
                }
            }
            for (SysAppMenuDto sysAppMenuDto : sysAppMenuDtoTreeList) {
                sysAppMenuDto.setChildren(getAppMenuChild(sysAppMenuDto.getId(), sysAppMenuDtoList));
            }
        }
        return ResultUtils.success(ResultEnum.SUCCESS, sysAppMenuDtoTreeList);


    }

    public List<SysAppMenuDto> getAppMenuChild(String id, List<SysAppMenuDto> allSysAppMenuDto) {
        // 子菜单
        List<SysAppMenuDto> childList = new ArrayList<>();
        for (SysAppMenuDto sysAppMenuDto : allSysAppMenuDto) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!sysAppMenuDto.getParentId().equals("0")) {
                if (sysAppMenuDto.getParentId().equals(id)) {
                    childList.add(sysAppMenuDto);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (SysAppMenuDto sysAppMenuDto : childList) {
            if (!StringUtils.isEmpty(sysAppMenuDto.getParentId())) {
                // 递归
                sysAppMenuDto.setChildren(getAppMenuChild(sysAppMenuDto.getId(), allSysAppMenuDto));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id) {
        BaseSysMenu baseSysMenuEntity = null;
        try {
            baseSysMenuEntity = baseSysMenuService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, baseSysMenuEntity);
    }


    /**
     * 新增数据
     *
     * @param baseSysMenu 实体对象
     * @return 新增结果
     */
    @AsynSelectMenuAnnotation(title = "菜单", action = "菜单")
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysMenu baseSysMenu) {
        try {

            //如果父菜单id是null,则说明这个菜单是一级菜单
            if (StringUtils.isEmpty(baseSysMenu.getParentId())) {
                //查询所有的一级菜单
                List<BaseSysMenu> menus = baseSysMenuService.getMenuByParentId(null);
                baseSysMenu.setSort(menus.size() + 1);
                baseSysMenu.setParentId("");
                baseSysMenu.setIsLeaf(SysMenuEnum.LEAF.getCode());
                //新增
                if (1 == baseSysMenuService.saveObject(baseSysMenu)) {
                    return ResultUtils.success(ResultEnum.SUCCESS);
                }
            } else {
                //拿到父菜单
                BaseSysMenu parentMenu = baseSysMenuService.getObjectById(baseSysMenu.getParentId());

                //看父菜单是否叶子节点，如果是叶子节点，则把父菜单改为非叶子节点
                if (SysMenuEnum.LEAF.getCode().equals(parentMenu.getIsLeaf())) {
                    parentMenu.setIsLeaf(SysMenuEnum.NO_LEAF.getCode());
                    baseSysMenuService.updateObject(parentMenu);
                }
                //查询出该父菜单下的所有子节点
                List<BaseSysMenu> menus = baseSysMenuService.getMenuByParentId(parentMenu.getId());
                baseSysMenu.setSort(menus.size() + 1);
                baseSysMenu.setIsLeaf(SysMenuEnum.LEAF.getCode());
                //新增
                if (1 == baseSysMenuService.saveObject(baseSysMenu)) {
                    return ResultUtils.success(ResultEnum.SUCCESS);
                }

            }
        } catch (ServiceException | DaoException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }


    /**
     * 修改数据
     *
     * @param sysMenuDto 实体对象
     * @return 修改结果, 仅修改 描述等属性，不能修改排序
     */
    @AsynSelectMenuAnnotation(title = "菜单", action = "菜单")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody SysMenuDto sysMenuDto) {
        try {
            if (sysMenuDto != null) {
                if (!StringUtils.hasLength(sysMenuDto.getParentId())) {
                    sysMenuDto.setParentId("");
                }
                BaseSysMenu baseSysMenuEntity = new BaseSysMenu();
                BeanUtils.copyProperties(sysMenuDto, baseSysMenuEntity);
                if (1 == baseSysMenuService.updateObject(baseSysMenuEntity)) {
                    asyncCacheService.asyncWriteUserCache();
                    return ResultUtils.success(ResultEnum.SUCCESS);
                }
            }

        } catch (ServiceException | DaoException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }


    /**
     * 菜单移动位置
     *
     * @param sysMenuDto 实体对象
     * @return 修改结果
     */
    @AsynSelectMenuAnnotation(title = "菜单", action = "菜单")
    @PutMapping("/updateInfo")
    public Result updateInfo(@RequestBody SysMenuDto sysMenuDto) {
        boolean updateOldStatus = true;
        boolean updateNewStatus = true;
        try {
            //原始 parentId
            String parentId = sysMenuDto.getParentId();
            //新的  parentId
            String newParentId = sysMenuDto.getNewParentId();
            //原排序
            int oldSort = sysMenuDto.getSort();
            // 菜单id
            String menuId = sysMenuDto.getMenuId();
            //新位置前面的menuId
//            String preMenuId=  sysMenuDto.getPreMenuId();
            String preMenuId = "";
            // 分别修改原始同级菜单的排序、新同级菜单的排序
            //修改该菜单原始位置同级别的顺序
            updateOldStatus = updateOldMenu(parentId, menuId, oldSort);

            updateNewStatus = updateNewMenu(newParentId, menuId, preMenuId);
            if (updateOldStatus && updateNewStatus) {
                //刷缓存
                asyncCacheService.asyncWriteUserCache();
                return ResultUtils.success(ResultEnum.SUCCESS);
            } else {
                return ResultUtils.error(ResultEnum.FAILED);
            }
        } catch (ServiceException | DaoException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    /**
     * 更新该菜单在新位置的数据，及新位置同级菜单的整体排序
     *
     * @param newParentId
     * @param meunId
     * @param preMenuId
     * @return
     * @throws DaoException
     */
    private boolean updateNewMenu(String newParentId, String meunId, String preMenuId) throws DaoException {
        boolean updateNewStatus = true;
        // 查询该菜单同级的所有菜单
        List<BaseSysMenu> baseSysMenuList = baseSysMenuService.getMenuByParentId(newParentId);
        int newSort = 1;
        if (StringUtils.isEmpty(preMenuId)) {
            newSort = 1;
        } else {
            BaseSysMenu preMenu = baseSysMenuService.getObjectById(preMenuId);
            if (null != preMenu) {
                newSort = preMenu.getSort() + 1;
            }
        }
        BaseSysMenu sysMenu = new BaseSysMenu();
        sysMenu.setSort(newSort);
        sysMenu.setId(meunId);
        sysMenu.setParentId(newParentId);
        baseSysMenuService.updateObject(sysMenu);

        //需要重新排序的菜单
        List<BaseSysMenu> menus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseSysMenuList)) {
            for (BaseSysMenu menu : baseSysMenuList) {
                if (menu.getSort() >= newSort) {
                    menu.setSort(menu.getSort() + 1);
                    menus.add(menu);
                }
            }
            //批量更新该同级的所有菜单
            for (BaseSysMenu menu : menus) {
                baseSysMenuService.updateObject(menu);
            }
        }
        return updateNewStatus;
    }


    /**
     * 把菜单从原来的位置上移走
     *
     * @param parentId
     * @param menuId
     */
    private boolean updateOldMenu(String parentId, String menuId, int sort) throws DaoException {
        boolean updateStatus = true;

        // 查询该菜单同级的所有菜单
        if (StringUtils.isEmpty(parentId)) {
            parentId = null;
        }
        List<BaseSysMenu> baseSysMenuList = baseSysMenuService.getMenuByParentId(parentId);
        //需要重新排序的菜单
        List<BaseSysMenu> menus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseSysMenuList)) {
            for (BaseSysMenu menu : baseSysMenuList) {
                if (menu.getSort() > sort) {
                    menu.setSort(menu.getSort() - 1);
                    menus.add(menu);
                }
            }
            //批量更新该同级的所有菜单
            for (BaseSysMenu menu : menus) {
                if (1 != baseSysMenuService.updateObject(menu)) {
                    updateStatus = false;
                }
            }
        }
        return updateStatus;
    }


    /**
     * 菜单移动位置
     *
     * @param
     * @return 修改结果
     */
    @AsynSelectMenuAnnotation(title = "菜单", action = "菜单")
    @PutMapping("/updateSort")
    public Result updateSort(@RequestBody JSONObject input) {
        boolean updateOldStatus = true;
        boolean updateNewStatus = true;
        try {
            if (!StringUtils.isEmpty(input)) {
                String id = (String) input.get("id");
                String targetId = (String) input.get("targetId");
                //          center   bottom   top
                String position = (String) input.get("position");

                //被挪动的菜单对象
                BaseSysMenu selfMenu = baseSysMenuService.getObjectById(id);
                //目标对象
                BaseSysMenu targetMenu = baseSysMenuService.getObjectById(targetId);
                Integer oldMenuSort = selfMenu.getSort();
                Integer targetMenuSort = targetMenu.getSort();
                String oldParentId = selfMenu.getParentId();
                String newParentId = targetMenu.getParentId();

                if (StringUtils.isEmpty(oldParentId)) {
                    oldParentId = "";
                }
                if (StringUtils.isEmpty(newParentId)) {
                    newParentId = "";
                }
                //在目标节点的里面
                if ("inner".equals(position)) {
                    //老节点的排序
                    List<BaseSysMenu> oldMenuList = baseSysMenuService.getMenuByParentId(selfMenu.getParentId());
                    if (CollectionUtils.isNotEmpty(oldMenuList)) {
                        for (BaseSysMenu menu : oldMenuList) {
                            if (menu.getSort() > selfMenu.getSort()) {
                                menu.setSort(menu.getSort() - 1);
                                baseSysMenuService.updateObject(menu);
                            }
                        }
                    }

                    Map map = new HashMap();
                    map.put("parentId", newParentId);
                    Integer count = baseSysMenuService.selectTotal(map);
                    //修改被移动节点的parentId和新的排序
                    selfMenu.setParentId(targetId);
                    selfMenu.setSort(count + 1);
                    baseSysMenuService.updateObject(selfMenu);
                } else if ("before".equals(position)) {
                    //在目标节点的上面
                    if (oldParentId.equals(newParentId)) {
                        //修改同级别其他的排序
                        List<BaseSysMenu> menuList = baseSysMenuService.getMenuByParentId(targetMenu.getParentId());
                        //假如目标节点 在老节点 上面
                        if (oldMenuSort > targetMenuSort) {
                            for (BaseSysMenu menu : menuList) {
                                if (menu.getSort() < oldMenuSort && menu.getSort() >= targetMenuSort) {
                                    //将目标节点及后面的都加1
                                    menu.setSort(menu.getSort() + 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                            //同级别的排序  此时，被挪动的节点在目标节点的前面
                            selfMenu.setSort(targetMenuSort);
                            baseSysMenuService.updateObject(selfMenu);
                        } else {
                            //假如目标节点 在老节点 下面
                            for (BaseSysMenu menu : menuList) {
                                if (menu.getSort() > oldMenuSort && menu.getSort() < targetMenuSort) {
                                    //将目标节点及后面的都减1
                                    menu.setSort(menu.getSort() - 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                            selfMenu.setSort(targetMenuSort - 1);
                            baseSysMenuService.updateObject(selfMenu);
                        }

                    } else {
                        //跨节点的排序  分别修改新节点和老节点的排序
                        //新节点的排序
                        List<BaseSysMenu> menuList = baseSysMenuService.getMenuByParentId(targetMenu.getParentId());

                        for (BaseSysMenu menu : menuList) {
                            if (menu.getSort() >= targetMenu.getSort()) {
                                //将目标节点及后面的都加1
                                menu.setSort(menu.getSort() + 1);
                                baseSysMenuService.updateObject(menu);
                            }
                        }

                        //老节点的排序
                        List<BaseSysMenu> oldMenuList = baseSysMenuService.getMenuByParentId(selfMenu.getParentId());
                        if (CollectionUtils.isNotEmpty(oldMenuList)) {
                            for (BaseSysMenu menu : oldMenuList) {
                                if (menu.getSort() > selfMenu.getSort()) {
                                    menu.setSort(menu.getSort() - 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                        }
                        //修改被移动节点的排序
                        selfMenu.setParentId(targetMenu.getParentId());
                        selfMenu.setSort(targetMenuSort);
                        baseSysMenuService.updateObject(selfMenu);
                    }
                } else {
                    //在目标节点的下面
                    if (oldParentId.equals(newParentId)) {
                        //修改同级别其他的排序
                        List<BaseSysMenu> menuList = baseSysMenuService.getMenuByParentId(targetMenu.getParentId());
                        //同级别的排序  此时，被挪动的节点在目标节点的后面

                        //假如目标节点 在老节点  下面
                        if (oldMenuSort < targetMenuSort) {

                            for (BaseSysMenu menu : menuList) {
                                if (menu.getSort() > oldMenuSort && menu.getSort() <= targetMenuSort) {
                                    //将目标节点及后面的都加1
                                    menu.setSort(menu.getSort() - 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                            selfMenu.setSort(targetMenuSort);
                            baseSysMenuService.updateObject(selfMenu);
                        } else {

                            for (BaseSysMenu menu : menuList) {
                                if (menu.getSort() < oldMenuSort && menu.getSort() > targetMenuSort) {
                                    //将目标节点及后面的都加1
                                    menu.setSort(menu.getSort() + 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                            //同级别的排序  此时，被挪动的节点在目标节点的前面
                            selfMenu.setSort(targetMenuSort);
                            baseSysMenuService.updateObject(selfMenu);
                        }


                    } else {
                        //跨节点的排序  分别修改新节点和老节点的排序
                        //新节点的排序
                        List<BaseSysMenu> menuList = baseSysMenuService.getMenuByParentId(targetMenu.getParentId());
                        for (BaseSysMenu menu : menuList) {
                            if (menu.getSort() > targetMenuSort) {
                                //将目标节点及后面的都加1
                                menu.setSort(menu.getSort() + 1);
                                baseSysMenuService.updateObject(menu);
                            }
                        }
//
//                        //老节点的排序
                        List<BaseSysMenu> oldMenuList = baseSysMenuService.getMenuByParentId(selfMenu.getParentId());
                        if (CollectionUtils.isNotEmpty(oldMenuList)) {
                            for (BaseSysMenu menu : oldMenuList) {
                                if (menu.getSort() > menu.getSort()) {
                                    menu.setSort(menu.getSort() - 1);
                                    baseSysMenuService.updateObject(menu);
                                }
                            }
                        }
                        //跨节点排序
                        selfMenu.setParentId(newParentId);
                        selfMenu.setSort(targetMenuSort + 1);
                        baseSysMenuService.updateObject(selfMenu);
                    }
                }
            }

            if (updateOldStatus && updateNewStatus) {
                //刷缓存
                asyncCacheService.asyncWriteUserCache();
                return ResultUtils.success(ResultEnum.SUCCESS);
            } else {
                return ResultUtils.error(ResultEnum.FAILED);
            }
        } catch (ServiceException | DaoException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @AsynSelectMenuAnnotation(title = "菜单", action = "菜单")
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            //查询该菜单是否是叶子节点，如果非叶子节点，不能删除
            BaseSysMenu baseSysMenu = baseSysMenuService.getObjectById(id);
            if (null == baseSysMenu) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "该菜单id不存在");
            }
            int thisSort = baseSysMenu.getSort();
            if (SysMenuEnum.NO_LEAF.getCode().equals(baseSysMenu.getIsLeaf())) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "该菜单非叶子菜单，不能删除");
            }
            // 查询改菜单下面是否有操作点，如果有操作点，不能删除
            List<BaseSysOperate> operateList = baseSysOperateService.getOperateByMenuId(id);
            if (CollectionUtils.isNotEmpty(operateList)) {
                return ResultUtils.error(ResultEnum.FAILED.getCode(), "该菜单下有操作点，不能删除");
            }
            // 查询该菜单同级的所有排序
            List<BaseSysMenu> baseSysMenuList = baseSysMenuService.getMenuByParentId(baseSysMenu.getParentId());

            // 删除该菜单
            int deleteResout = baseSysMenuService.delObjectById(id);
            //需要重新排序的菜单
            List<BaseSysMenu> menus = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(baseSysMenuList)) {
                for (BaseSysMenu menu : baseSysMenuList) {
                    if (menu.getSort() > thisSort) {
                        menu.setSort(menu.getSort() - 1);
                        menus.add(menu);
                    }
                }
                //批量更新该同级的所有菜单
                for (BaseSysMenu menu : menus) {
                    baseSysMenuService.updateObject(menu);
                }
            } else {
                //如果统计菜单下没有子菜单，将父菜单改为叶子节点
                BaseSysMenu sysMenu = new BaseSysMenu();
                sysMenu.setId(baseSysMenu.getParentId());
                sysMenu.setIsLeaf(SysMenuEnum.LEAF.getCode());
                baseSysMenuService.updateObject(sysMenu);
            }

            if (1 == deleteResout) {
                asyncCacheService.asyncWriteUserCache();
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (ServiceException | DaoException e) {
            log.error("删除失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }


    /**
     * 查询所有菜单
     *
     * @return
     */
    @GetMapping("/selectAllMenu")
    public Result selectAllenu() {
        List<SysMenuDto> sysMenuDtos = null;
        try {
            sysMenuDtos = baseSysMenuService.getAllMenu();
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询菜单失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, sysMenuDtos);
    }


    /**
     * 查询所有菜单和操作点
     *
     * @return
     */
    @GetMapping("/selectAllMenuAndOperate")
    public Result selectAllMenuAndOperate() {
        List<SysMenuDto> list = new ArrayList<>();
        try {
            list = baseSysMenuService.getAllMenuAndOperate();
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询菜单和按钮", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


    /**
     * 查询所有菜单和数据规则
     *
     * @return
     */
    @GetMapping("/selectAllMenuAndDataRule")
    public Result selectAllMenuAndDataRule() {
        List<SysMenuDataRuleDto> list = new ArrayList<>();
        try {
            list = baseSysMenuService.getAllMeunAndDataRule();
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询菜单和数据权限失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


    /**
     * 查询所有菜单和数据规则
     *
     * @return
     */
    @GetMapping("/selectOperateDataRuleByMenuId/{menuId}")
    public Result selectOperateDataRuleByMenuId(@PathVariable("menuId") String menuId) {
        OperateAndDataRuleDto operateAndDataRuleDto = new OperateAndDataRuleDto();
        try {
            //查询操作点
            List<BaseSysOperate> operateList = baseSysOperateService.getOperateByMenuId(menuId);
            //查询数据规则
            List<BaseSysDataRule> dataRuleList = baseSysDataRuleService.getSysDataListByMenuId(menuId);

            operateAndDataRuleDto.setDataRuleList(dataRuleList);
            operateAndDataRuleDto.setOperateList(operateList);

        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询按钮菜单id失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, operateAndDataRuleDto);
    }

    /**
     * 新增权限菜单唯一查询
     *
     * @param map 菜单对象
     * @return Result对象
     */
    @PostMapping("/selectMenuList")
    public Result selectMeumList(@RequestBody Map map) {
        return baseSysMenuService.selectMenuList(map);
    }


    /**
     * 根据应用返回菜单
     *
     * @param baseSysMenu
     * @return
     */
    @PostMapping("/selectMenuByApp")
    public Result selectMeumByApp(@RequestBody BaseSysMenu baseSysMenu) {
        List<SysMenuDto> list = null;
        try {
            list = baseSysMenuService.getAllMenuByAppId(baseSysMenu);
        } catch (DaoException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }

    @GetMapping("/selectMenuByAppCode")
    public Result selectMenuByAppCode(String code) {
        List<SysMenuDto> list = null;
        try {
            list = baseSysMenuService.getAllMenuByAppCode(code);
        } catch (DaoException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


}

