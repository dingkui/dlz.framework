package com.dlz.framework.db.modal.result;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@ApiModel(value = "分页对象")
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
    @ApiModelProperty(value = "排序", position = 6)
    private List<Order> orders = new ArrayList<>();

    public static <T> Page<T> create(int pageIndex,int pageSize,Order... order){
        return new Page<>(pageIndex,pageSize,order);
    }

    public Page(int pageIndex,int pageSize,Order... order){
        this.setPageSize(pageSize);
        this.setPageIndex(pageIndex);
        this.orders = Arrays.asList(order);
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

    @JsonIgnore
    public String getSortSql() {
        if(orders ==null|| orders.size()==0){
            return null;
        }
        return " order by "+orders.stream()
                .map(o-> ConvertUtil.str2Clumn(o.getColumn())+(o.isAsc()?" asc":" desc"))
                .collect(Collectors.joining(","));
    }

    public Page<T> removeOrder(Predicate<Order> filter) {
        for(int i = this.orders.size() - 1; i >= 0; --i) {
            if (filter.test(this.orders.get(i))) {
                this.orders.remove(i);
            }
        }
        return this;
    }

    public Page<T> addOrder(Order... items) {
        return addOrder(Arrays.asList(items));
    }

    public Page<T> addOrder(List<Order> items) {
        List<Order> collect = items.stream().filter(o -> o.getColumn() != null).collect(Collectors.toList());
        if(collect.size()>0){
            this.orders.addAll(collect);
        }
        return this;
    }
}