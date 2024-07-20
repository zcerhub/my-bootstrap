package com.base.api.entity;

/**
 * @author Arlo
 * @date 2021/1/15
 **/
public class PageRequest<T extends BaseEntity> {

    private T requestObject;

    private int pageNo;

    private int pageSize;

    public T getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(T requestObject) {
        this.requestObject = requestObject;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
