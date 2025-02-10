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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@ApiModel(value = "分页对象")
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @ApiModelProperty(value = "当前页码，从1开始", position = 1)
    private long current=0;
    @ApiModelProperty(value = "每页条数", position = 2)
    private long size = DEFAULT_PAGE_SIZE;
    @ApiModelProperty(value = "数据总条数", position = 3)
    private long total;
    @ApiModelProperty(value = "总页数", position = 4)
    private long pages;
    @ApiModelProperty(value = "数据集合", position = 5)
    private List<T> records;
    @ApiModelProperty(value = "排序", position = 6)
    private List<Order> orders=new ArrayList<>();

    public static <T> Page<T> build(long current,long size,Order... order){
        return new Page<>(current,size,order);
    }
    public static <T> Page<T> build(Order... order){
        return new Page<>(order);
    }

    public Page(long current,long size,Order... order){
        this(order);
        this.setSize(size);
        this.setCurrent(current);
    }
    public Page(Order... order){
        this.orders.addAll( Arrays.asList(order));
    }
    public Page(){
    }

    public Page<T> setSize(long size) {
        if(size>5000){
            size=5000;
        }
        this.size = size;
        cnt();
        return this;
    }
    public Page<T> setCurrent(long current) {
        this.current=current;
        return cnt();
    }
    public Page<T> setTotal(long total) {
        this.total=total;
        return cnt();
    }


    public Page<T> doPage(Supplier<Integer> total, Supplier<List<T>> record) {
        setTotal(total.get());
        //是否需要查询列表（需要统计条数并且条数是0的情况不查询，直接返回空列表）
        if(this.total>0){
            this.records = record.get();
        }else{
            this.records = new ArrayList<>(0);
        }
        return this;
    }

    private Page<T> cnt(){
        if(size<=0){
            setPages(1);
            setCurrent(0);
            return this;
        }
        pages=(total%size==0?total/size:total/size+1);
        if(pages>0&&current>pages){
            setCurrent(pages);
        }
        return this;
    }

    @JsonIgnore
    public String getSortSql() {
        if(orders ==null|| orders.size()==0){
            return null;
        }
        return " order by "+orders.stream()
                .map(o-> ConvertUtil.str2DbClumn(o.getColumn())+(o.isAsc()?" asc":" desc"))
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