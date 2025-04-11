package com.dlz.framework.db.inf;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;

import java.util.List;

/**
 * 添加and or条件
 *
 */
public interface IOperatorQuery extends ISqlPara {
    Page<?> getPage();

    void setPage(Page<?> page);
    default ResultMap query() {
        return DBHolder.doDb(s->s.getMap(this));
    }
    default List<ResultMap> queryList() {
        return DBHolder.doDb(s->s.getMapList(this));
    }
    default Page<ResultMap> queryPage() {
        return DBHolder.doDb(s->s.getPage(this));
    }

    default <T> T query(Class<T> tClass) {
        return DBHolder.doDb(s->s.getBean(this,tClass));
    }
    default <T> List<T> queryList(Class<T> tClass) {
        return DBHolder.doDb(s->s.getBeanList(this,tClass));
    }
    default <T> Page<T> queryPage(Class<T> tClass) {
        return DBHolder.doDb(s->s.getPage(this,tClass));
    }
    default Page<ResultMap> queryPageData() {
        return DBHolder.doDb(s->s.getPage(this));
    }


    default String queryStr() {
        return DBHolder.doDb(s->s.getStr(this));
    }



    default List<String> queryStrList() {
        return DBHolder.doDb(s->s.getStrList(this));
    }
    default Long queryLong() {
        return DBHolder.doDb(s->s.getLong(this));
    }
    default List<Long> queryLongList() {
        return DBHolder.doDb(s->s.getLongList(this));
    }
    default int count() {
        return DBHolder.doDb(s->s.getCnt(this));
    }
}
