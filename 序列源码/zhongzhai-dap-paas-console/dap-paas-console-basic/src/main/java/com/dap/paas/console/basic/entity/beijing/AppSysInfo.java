package com.dap.paas.console.basic.entity.beijing;

import com.dap.paas.console.basic.entity.Organization;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: liu
 * @date: 2022/9/6 19:57
 * @description:
 */
public class AppSysInfo extends Organization {
    private String creDate;
    private String updDate;

    public String getCreDate() {

        return creDate;
    }



    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
