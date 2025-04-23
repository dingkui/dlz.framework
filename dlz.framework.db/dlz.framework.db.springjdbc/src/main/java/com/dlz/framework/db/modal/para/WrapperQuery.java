package com.dlz.framework.db.modal.para;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorQuery;
import com.dlz.framework.db.inf.ISqlMakerPage;
import com.dlz.framework.db.inf.ISqlWrapperSearch;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询语句生成器
 *
 * @author dk
 */
public class WrapperQuery<T> extends AWrapperSearch<WrapperQuery<T>,T, MakerQuery> implements
        ISqlWrapperSearch<WrapperQuery<T>, T>,
        ISqlMakerPage<WrapperQuery<T>>,
        IOperatorQuery {

    public static <T> WrapperQuery<T> wrapper(T conditionBean) {
        return new WrapperQuery(conditionBean);
    }

    public static <T> WrapperQuery<T> wrapper(Class<T> beanClass) {
        return new WrapperQuery(beanClass);
    }

    private WrapperQuery(Class<T> beanClass) {
        super(beanClass);
        setPm(new MakerQuery(getTableName()));
        setAllowFullQuery(true);
    }

    private WrapperQuery(T conditionBean) {
        super(conditionBean);
        setPm(new MakerQuery(getTableName()));
        setAllowFullQuery(true);
    }

    public WrapperQuery<T> select(String... colums) {
        if (colums.length > 0) {
            getPm().select(colums);
        }
        return this;
    }

    public WrapperQuery<T> select(DlzFn<T, ?>... colums) {
        if (colums.length > 0) {
            getPm().select(colums);
        }
        return this;
    }

    /**
     * 自动根据map的键值对添加查询条件
     *
     * @param req:{key:列名，value:值} key值为列名 可带$前缀，如$eq_key:表示 key=key DbOprateEnum=eq
     *                             value值为值
     * @return 返回当前条件对象，支持链式调用
     */
    public WrapperQuery<T> auto(Map<String, Object> req) {
        String tableName = BeanInfoHolder.getTableName(getBeanClass());
        return auto(req, (key)-> BeanInfoHolder.isColumnExists(tableName,key));
    }

    @Override
    public JdbcItem jdbcCnt() {
        generatWithBean(bean);
        return getPm().jdbcCnt();
    }

    @Override
    public WrapperQuery<T> me() {
        return this;
    }

    @Override
    public Page getPage() {
        return getPm().getPage();
    }

    @Override
    public void setPage(Page<?> page) {
        getPm().setPage(page);
    }

    @Override
    public WrapperQuery<T> page(Page page) {
        getPm().setPage(page);
        return this;
    }

    public T queryBean() {
        return DBHolder.doDb(s -> s.getBean(this, true));
    }
    public List<T> queryBeanList() {
        return DBHolder.doDb(s -> s.getBeanList(this));
    }
    public Page<T> queryBeanPage() {
        return DBHolder.doDb(s -> s.getPage(this, this.getBeanClass()));
    }

    public WrapperQuery<T> orderByAsc(DlzFn<T, ?>... column) {
        return sort(Order.ascs(column));
    }

    public WrapperQuery<T> orderByDesc(DlzFn<T, ?>... column) {
        return sort(Order.descs(column));
    }
}
