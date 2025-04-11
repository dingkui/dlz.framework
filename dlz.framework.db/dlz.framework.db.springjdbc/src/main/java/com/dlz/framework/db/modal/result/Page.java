package com.dlz.framework.db.modal.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "分页对象")
public class Page<T> extends Sort<Page> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @ApiModelProperty(value = "当前页码，从1开始", position = 1)
    private long current = 0;
    @ApiModelProperty(value = "每页条数", position = 2)
    private long size = DEFAULT_PAGE_SIZE;
    @ApiModelProperty(value = "数据总条数", position = 3)
    private long total;
    @ApiModelProperty(value = "总页数", position = 4)
    private long pages;
    @ApiModelProperty(value = "数据集合", position = 5)
    private List<T> records;

    public static <T> Page<T> build(long current, long size, Order... order) {
        return new Page<>(current, size, order);
    }

    public static <T> Page<T> build(Order... order) {
        return new Page<>(order);
    }

    public Page(long current, long size, Order... order) {
        super(order);
        this.setSize(size);
        this.setCurrent(current);
    }

    public Page(Order... order) {
        super(order);
    }
    public Page() {
        super();
    }
    public <E> Page<E> cover(Function<T, E> c) {
        Page<E> page = new Page<>(current,size);
        page.setTotal(this.total);
        page.setRecords(this.records.stream().map(c).collect(Collectors.toList()));
        return page;
    }

    public Page<T> setSize(long size) {
        if (size > 5000) {
            size = 5000;
        }
        this.size = size;
        cnt();
        return this;
    }

    public Page<T> setCurrent(long current) {
        this.current = current;
        return cnt();
    }

    public Page<T> setTotal(long total) {
        this.total = total;
        return cnt();
    }


    public Page<T> doPage(Supplier<Integer> total, Supplier<List<T>> record) {
        setTotal(total.get());
        //是否需要查询列表（需要统计条数并且条数是0的情况不查询，直接返回空列表）
        if (this.total > 0) {
            this.records = record.get();
        } else {
            this.records = new ArrayList<>(0);
        }
        return this;
    }

    private Page<T> cnt() {
        if (size <= 0) {
            setPages(1);
            setCurrent(0);
            return this;
        }
        pages = (total % size == 0 ? total / size : total / size + 1);
        if (pages > 0 && current > pages) {
            setCurrent(pages);
        }
        return this;
    }
}