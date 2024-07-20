package com.base.sys.api.entity;


import com.base.api.entity.BaseEntity;

/**
 * 应用
 */
public class BaseSysApp  extends BaseEntity {

    //名称
    private String name ;

    //编码
    private String code;


    //排序
    private Integer sort;


    //描述
    private String description;

    private String icon;

    private String instructionBookName;

    public String getInstructionBookName() {
        return instructionBookName;
    }

    public void setInstructionBookName(String instructionBookName) {
        this.instructionBookName = instructionBookName;
    }

    public byte[] getProductInstructionBook() {
        return productInstructionBook;
    }

    public void setProductInstructionBook(byte[] productInstructionBook) {
        this.productInstructionBook = productInstructionBook;
    }

    private  byte[] productInstructionBook;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
