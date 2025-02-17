package com.dlz.framework.db.modal.result;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.inf.IChained;
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
@ApiModel(value = "排序对象")
public class Sort<T extends Sort> implements Serializable, IChained<T> {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "排序")
    private List<Order> orders=new ArrayList<>();

    public static Sort buildSort(Order... order){
        return new Sort(order);
    }
    public Sort(Order... order){
        this.orders.addAll( Arrays.asList(order));
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

    public T removeOrder(Predicate<Order> filter) {
        for(int i = this.orders.size() - 1; i >= 0; --i) {
            if (filter.test(this.orders.get(i))) {
                this.orders.remove(i);
            }
        }
        return me();
    }

    public T addOrder(Order... items) {
        return addOrder(Arrays.asList(items));
    }

    public T addOrder(List<Order> items) {
        List<Order> collect = items.stream().filter(o -> o.getColumn() != null).collect(Collectors.toList());
        if(collect.size()>0){
            this.orders.addAll(collect);
        }
        return me();
    }

    @Override
    public T me() {
        return (T)this;
    }
}