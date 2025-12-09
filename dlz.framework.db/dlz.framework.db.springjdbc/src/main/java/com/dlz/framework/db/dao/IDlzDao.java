package com.dlz.framework.db.dao;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.fn.DlzFn2;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.util.DbConvertUtil;
import com.dlz.framework.db.util.DbLogUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

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
    List<ResultMap> getList(String sql, Object... args);
    <T> List<T> getList(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;

    default ResultMap getOne(String sql, boolean checkOne, Object... args) {
        List<ResultMap> list = getList(sql, args);
        if (list.size() == 0) {
            return null;
        }
        if (checkOne && list.size() > 1) {
            throw new DbException("查询结果为多条", 1004);
        }
        return list.get(0);
    }

    default <T> T getFistColumn(String sql, Class<T> requiredType, Object... args) {
        return DbConvertUtil.getFistColumn(getOne(sql, false, args), requiredType);
    }

    default <T> T doDb(Supplier<T> s, DlzFn2<Long,T,String> msg) {
        if (msg == null) {
            return s.get();
        }
        long t = System.currentTimeMillis();
        T re=null;
        Exception err = null;
        try {
            re = s.get();
            return re;
        } catch (Exception e) {
            err = e;
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException("sql执行错误:", 1001);
        } finally {
            DbLogUtil.logInfo(msg,t, re,err);
        }
    }

    int update(String sql, Object... args);

    Long updateForId(String sql, Object... args);

    void execute(final String sql, Object... args);

    int[] batchUpdate(String sql, List<Object[]> batchArgs);

    HashMap<String, Integer> getTableColumnsInfo(String tableName);
}
