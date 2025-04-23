package com.dlz.framework.db.modal.para;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.helper.support.SnowFlake;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.inf.IOperatorInsert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 插入语句生成器
 *
 * @author dk
 */
public class WrapperInsert<T> extends AWrapper<T, MakerInsert> implements IOperatorInsert {
    public static <T> WrapperInsert<T> wrapper(T valueBean) {
        return new WrapperInsert(valueBean);
    }

    private WrapperInsert(T valueBean) {
        super(valueBean);
        setPm(new MakerInsert(getTableName()));
    }

    @Override
    protected void wrapValues(List<Field> fields, T bean) {
        fields.forEach(field -> {
            Object value = FieldReflections.getValue(bean, field);
            if (value != null) {
                getPm().value(BeanInfoHolder.getColumnName(field), value);
            } else {
                final TableId annotation = field.getAnnotation(TableId.class);
                if (annotation == null) {
                    return;
                }
                final IdType type = annotation.type();
                if (type == IdType.AUTO) {
                    return;
                } else {
                    final String columnName = BeanInfoHolder.getColumnName(field);
                    if (type == IdType.ASSIGN_ID) {
                        value = SnowFlake.id();
                    } else if (type == IdType.ASSIGN_UUID) {
                        value = UUID.randomUUID().toString().replace("-", "");
                    } else if (type == IdType.NONE) {
                        value = DBHolder.sequence(getTableName(), 1l);
                    } else {
                        throw new SystemException(columnName + " idType is " + type + " but null");
                    }
                    getPm().value(columnName, value);
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
        String sql = MakerUtil.buildInsertSql(dbName, fields);
        while (valueBeans.size() > 0 && batchSize > 0) {
            if (batchSize > valueBeans.size()) {
                batchSize = valueBeans.size();
            }
            final List<T> ts = valueBeans.subList(0, batchSize);

            List<Object[]> paramValues = ts.stream()
                    .map(v -> MakerUtil.buildInsertParams(v, fields))
                    .collect(Collectors.toList());
            DBHolder.getService().getDao().batchUpdate(sql, paramValues);
            valueBeans = valueBeans.subList(batchSize, valueBeans.size());
        }
        return true;
    }
}
