package com.base.sys.api.dto;




import java.util.List;

public class MenuDataRuleOperatePermission {

    //菜单按钮授权的树(根据角色查询的树)


    //菜单按钮授权的树(全量的树)
    private List<SysMenuDto> allSysMenuDtoList;

    //数据树 （根据角色查询的数据）


    //数据树 （全量的数据）
    private List<SysMenuDto>  allBaseSysDataList;


    //选中的菜单操作点id集合
    private List<String>  checkedMenuOperateList;


    //选中的数据规则Id集合
    private List<String>  checkedDataRuleIdList;


    public List<String> getCheckedMenuOperateList() {
        return checkedMenuOperateList;
    }

    public void setCheckedMenuOperateList(List<String> checkedMenuOperateList) {
        this.checkedMenuOperateList = checkedMenuOperateList;
    }

    public List<String> getCheckedDataRuleIdList() {
        return checkedDataRuleIdList;
    }

    public void setCheckedDataRuleIdList(List<String> checkedDataRuleIdList) {
        this.checkedDataRuleIdList = checkedDataRuleIdList;
    }


    public List<SysMenuDto> getAllBaseSysDataList() {
        return allBaseSysDataList;
    }

    public void setAllBaseSysDataList(List<SysMenuDto> allBaseSysDataList) {
        this.allBaseSysDataList = allBaseSysDataList;
    }


    public List<SysMenuDto> getAllSysMenuDtoList() {
        return allSysMenuDtoList;
    }

    public void setAllSysMenuDtoList(List<SysMenuDto> allSysMenuDtoList) {
        this.allSysMenuDtoList = allSysMenuDtoList;
    }


}
