package com.dlz.framework.db.modal;

import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.para.WrapperInsert;

import java.util.List;

public class DbBatch {
    public <T> boolean insert(List<T> bean) {
        return insert(bean, 1000);
    }

    public <T> boolean insert(List<T> bean, int batchSize) {
        if (bean.size() > 0) {
            return WrapperInsert.wrapper(bean.get(0)).batch(bean, batchSize);
        }
        return false;
    }


    public boolean update(String sql, List<Object[]> valueBeans) {
        return update(sql, valueBeans, 1000);
    }

    public boolean update(String sql, List<Object[]> valueBeans, int batchSize) {
        for (; valueBeans.size() > 0 && batchSize > 0; valueBeans = valueBeans.subList(batchSize, valueBeans.size())) {
            if (batchSize > valueBeans.size()) {
                batchSize = valueBeans.size();
            }
            List<Object[]> paramValues = valueBeans.subList(0, batchSize);
            DBHolder.getDao().batchUpdate(sql, paramValues);
        }
        return true;
    }


}
