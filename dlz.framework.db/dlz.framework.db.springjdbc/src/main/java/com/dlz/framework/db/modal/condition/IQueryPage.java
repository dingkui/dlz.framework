package com.dlz.framework.db.modal.condition;

import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

/**
 * 添加and or条件
 * @param <T>
 */
public interface IQueryPage<T extends IQueryPage> {
    T mine();
    Page getPage();
    T page(Page page);

    default  <T1> T orderByAsc(MFunction<T1, ?> column) {
        return orderByAsc(Reflections.getFieldName(column));
    }
    default T orderByAsc(String column) {
        return page(column, "ASC",0);
    }
    default <T1> T orderByDesc(MFunction<T1, ?> column) {
        return orderByDesc(Reflections.getFieldName(column));
    }
    default T orderByDesc(String column) {
        return page(column, "DESC",0);
    }
    default T page(String column,String order,int size) {
        Page pmPage = getPage();
        if(pmPage==null){
            pmPage=new Page<>();
        }
        if(column!=null){
            getPage().setSortField(column);
        }
        if(order!=null){
            getPage().setSortOrder(order);
        }
        if(size>0){
            getPage().setPageSize(size);
        }
        return page(pmPage);
    }
}
