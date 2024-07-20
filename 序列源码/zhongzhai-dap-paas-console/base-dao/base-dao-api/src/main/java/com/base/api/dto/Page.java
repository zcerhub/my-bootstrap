package com.base.api.dto;

import java.util.List;

/**
 * 返回页面数据
 */
public class Page<T> {
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 总条数
     */
    private Integer  totalCount;

    /**
     * 每页条数据
     */
    private Integer  pageSize;

    /**
     * 当前页数
     */
    private Integer pageNo;


    public List<T> getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
