package com.dlz.framework.db.modal.result;

import com.dlz.comm.json.JSONMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "前端分页对象")
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @ApiModelProperty(value = "当前页码", position = 1)
    private int pageIndex=-1;
    @ApiModelProperty(value = "每页条数", position = 2)
    private int pageSize = DEFAULT_PAGE_SIZE;
    @ApiModelProperty(value = "数据总条数", position = 3)
    private int count;
    @ApiModelProperty(value = "总页数", position = 4)
    private int pages;
    @ApiModelProperty(value = "数据集合", position = 5)
    private List<T> data;
    @ApiModelProperty(value = "排序字段")
    private String sortField;
    @ApiModelProperty(value = "排序类型")
    private String sortOrder;
    @ApiModelProperty(value = "查询参数 {$xx_key:value} eq:=,lt:<,le:<=,gt:>,ge:>=,ne:<>,in:in,lk:%x%,nl:!%x%,ll:x%,lr:%x,bt:between,nb:!between")
    private JSONMap para=new JSONMap();

    public Page(int pageIndex,int pageSize,String sortField,String sortOrder){
        this.setPageSize(pageSize);
        this.setPageIndex(pageIndex);
        this.sortField=sortField;
        this.sortOrder=sortOrder;
    }
    public Page(int pageIndex,int pageSize){
        this.setPageSize(pageSize);
        this.setPageIndex(pageIndex);
    }
    public Page(){}

    public Page<T> setPageSize(int pageSize) {
        if(pageSize<0 || pageSize>10000){
            pageSize=DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
        setCNT();
        return this;
    }
    public Page<T> setPageIndex(int pageIndex) {
        this.pageIndex=pageIndex;
        return setCNT();
    }
    public Page<T> setCount(int count) {
        this.count=count;
        return setCNT();
    }

    private Page<T> setCNT(){
        pages=(count%pageSize==0?count/pageSize:count/pageSize+1);
        if(pages>0&&pageIndex>pages-1){
            setPageIndex(pages-1);
        }
        return this;
    }

}