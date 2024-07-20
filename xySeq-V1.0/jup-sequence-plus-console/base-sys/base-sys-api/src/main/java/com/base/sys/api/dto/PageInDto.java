package com.base.sys.api.dto;

/**
 * @author Arlo
 * @date 2021/1/15
 **/
public class PageInDto<T> {

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

    @Override
    public String toString() {
        return "PageInDto{" +
                "requestObject=" + requestObject +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
