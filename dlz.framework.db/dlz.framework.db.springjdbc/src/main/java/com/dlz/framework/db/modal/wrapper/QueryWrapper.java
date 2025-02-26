package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.DbInfoCache;
import com.dlz.framework.db.modal.condition.*;
import com.dlz.framework.db.modal.map.ParaMapSearch;
import com.dlz.framework.db.modal.result.Page;

import java.util.List;
import java.util.Map;

/**
 * 查询语句生成器
 *
 * @author dk
 */
public class QueryWrapper<T> extends AWrapper<T> implements ICondition<QueryWrapper<T>>,
        ICondAuto<QueryWrapper<T>>,
        IQueryPageLamda<QueryWrapper<T>, T>,
        ICondAddByLamda<QueryWrapper<T>, T> {
    private T conditionBean;
    private final ParaMapSearch pm;

    public static <T> QueryWrapper<T> wrapper(T conditionBean) {
        return new QueryWrapper(conditionBean);
    }

    public static <T> QueryWrapper<T> wrapper(Class<T> beanClass) {
        return new QueryWrapper(beanClass);
    }

    public QueryWrapper(Class<T> beanClass) {
        super(beanClass);
        pm = new ParaMapSearch(getTableName());
    }

    public QueryWrapper(T conditionBean) {
        super((Class<T>) conditionBean.getClass());
        pm = new ParaMapSearch(getTableName());
        this.conditionBean = conditionBean;
    }


    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    public QueryWrapper<T> auto(Map<String, Object> req) {
        String tableName = DbInfoCache.getTableName(getBeanClass());
        return auto(req, (key)-> DbConvertUtil.isClumnExists(tableName,key));
    }

    @Override
    protected void wrapValue(String columnName, Object value) {
        pm.eq(columnName, value);
    }

    @Override
    public VAL<String, Object[]> buildSql(boolean cnt) {
        generatWithBean(conditionBean);
        if (cnt) {
            return pm.jdbcCnt();
        }
        if (pm.getPage() != null && pm.getPage().getCurrent() == 0) {
            return pm.jdbcSql();
        }
        return pm.jdbcPage();
    }

    @Override
    public QueryWrapper<T> me() {
        return this;
    }

    @Override
    public Page getPage() {
        return pm.getPage();
    }

    @Override
    public QueryWrapper<T> page(Page page) {
        pm.setPage(page);
        return this;
    }

    @Override
    public void addChildren(Condition child) {
        pm.addChildren(child);
    }

    public T queryBean() {
        return DBHolder.doDb(s -> s.getBean(this, true));
    }

    public List<T> queryBeanList() {
        return DBHolder.doDb(s -> s.getBeanList(this));
    }
//    public <T1> List<T1> queryBeanList(Class<T> x) {
//        return queryBeanList().stream().map(bean->{
//            BeanUtil beanUtil = new BeanUtil();
//        }).forEach(t829037s -> {});
//    }

    public Page<T> queryPage() {
        return DBHolder.doDb(s -> s.getPage(this));
    }
}
