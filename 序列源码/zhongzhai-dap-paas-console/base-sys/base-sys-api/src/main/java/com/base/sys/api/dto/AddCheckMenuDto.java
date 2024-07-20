package com.base.sys.api.dto;

import java.util.List;

public class AddCheckMenuDto {
    private String roleId;
    private List<TreeNodeData> funcSave;
    private List<TreeNodeData> dataSave;



    public List<TreeNodeData> getFuncSave() {
        return funcSave;
    }

    public void setFuncSave(List<TreeNodeData> funcSave) {
        this.funcSave = funcSave;
    }

    public List<TreeNodeData> getDataSave() {
        return dataSave;
    }

    public void setDataSave(List<TreeNodeData> dataSave) {
        this.dataSave = dataSave;
    }






    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

//    public List<AddAppMenuDto> getMenuCheck() {
//        return menuCheck;
//    }
//
//    public void setMenuCheck(List<AddAppMenuDto> menuCheck) {
//        this.menuCheck = menuCheck;
//    }
//
//    public List<AddAppMenuDto> getDataCheck() {
//        return dataCheck;
//    }
//
//    public void setDataCheck(List<AddAppMenuDto> dataCheck) {
//        this.dataCheck = dataCheck;
//    }
}
