package com.dlz.framework.db.dao;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.modal.result.ResultMap;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;


/**
 * 数据库操作接口
 *
 * @author kingapex
 * 2010-6-13上午11:05:32
 * <p>
 * 2018-10-17 dk 覆盖query和execute，去掉过多的sql debug日志,添加异常时的sql日志
 */
@Lazy
public interface IDlzDao {
    List<ResultMap> query(String sql,Object... args);

    default List<ResultMap> getList(String sql, Object... args){
        return doDb(()-> query(sql, args), "getList", sql, args);
    }

    default ResultMap getOne(String sql, boolean checkOne, Object... args) {
        return doDb(()-> {
            List<ResultMap> list = query(sql, args);
            if (list.size() == 0) {
                return null;
            }
            if (checkOne && list.size() > 1) {
                throw new DbException("查询结果为多条", 1004);
            }
            return list.get(0);
        }, "getOne", sql, args);
    }
    default <T> T getFistColumn(String sql, Class<T> requiredType, Object... args) {
        return ConvertUtil.getFistColumn(getOne(sql,false, args),requiredType);
    }

    void logInfo(String sql, String method, long t, Object... args);

    default <T> T doDb(Supplier<T> s, String fn, String sql, Object... args) {
        if(fn==null){
            return s.get();
        }
        long t = System.currentTimeMillis();
        try {
            return s.get();
        } finally {
            logInfo(sql, fn, t, args);
        }
    }

    int update(String sql, Object... args);

    Long updateForId(String sql, Object... args);

    void execute(final String sql, Object... args);

    int[] batchUpdate(String sql, List<Object[]> batchArgs);

    HashMap<String, Integer> getTableColumnsInfo(String tableName);
}
