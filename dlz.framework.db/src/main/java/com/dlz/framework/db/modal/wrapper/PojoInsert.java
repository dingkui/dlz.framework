package com.dlz.framework.db.modal.wrapper;

import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.para.AParaPojo;
import com.dlz.framework.db.inf.IExecutorInsert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 插入语句生成器
 *
 * @author dk
 */
public class PojoInsert<T> extends AParaPojo<T, TableInsert> implements IExecutorInsert {
    public static <T> PojoInsert<T> wrapper(T valueBean) {
        return new PojoInsert(valueBean);
    }

    private PojoInsert(T valueBean) {
        super(valueBean);
        setPm(new TableInsert(getTableName()));
    }

    @Override
    protected void wrapValues(List<Field> fields, T bean) {
        fields.forEach(field -> {
            Object value = FieldReflections.getValue(bean, field);
            if (value != null) {
                getPm().value(BeanInfoHolder.getColumnName(field), value);
            } else {
                value = WrapperBuildUtil.getIdValue(field, getTableName());
                if (value != null) {
                    FieldReflections.setValue(bean, field, value);
                    getPm().value(BeanInfoHolder.getColumnName(field), value);
                }
            }
        });
    }

    public boolean batch(List<T> valueBeans) {
        return batch(valueBeans, 1000);
    }

    public boolean batch(List<T> valueBeans, int batchSize) {
        String dbName = BeanInfoHolder.getTableName(getBeanClass());
        final List<Field> fields = BeanInfoHolder.getBeanFields(getBeanClass());
        String sql = WrapperBuildUtil.buildInsertSql(dbName, fields);
        while (valueBeans.size() > 0 && batchSize > 0) {
            if (batchSize > valueBeans.size()) {
                batchSize = valueBeans.size();
            }
            final List<T> ts = valueBeans.subList(0, batchSize);

            List<Object[]> paramValues = ts.stream()
                    .map(v -> WrapperBuildUtil.buildInsertParams(dbName,v, fields))
                    .collect(Collectors.toList());
            DBHolder.getDao().batchUpdate(sql, paramValues);
            valueBeans = valueBeans.subList(batchSize, valueBeans.size());
        }
        return true;
    }
}
