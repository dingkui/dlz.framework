package com.dlz.test.framework.db.config;

import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.json.JSONList;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.util.DbLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class DlzTestDao implements IDlzDao {


    @Override
    public List<ResultMap> getList(String sql, Object... args) throws DataAccessException {
        return getList(sql, null, args);
    }

    @Override
    public <T> List<T> getList(String sql, RowMapper<T> mapper, Object... args) throws DataAccessException {
        return doDb(() -> sql.startsWith("select count(1)")? (ArrayList<T>)new JSONList("[{\"cnt\":1000}]",ResultMap.class) :new ArrayList<>(),
                (t, r) -> DbLogUtil.generateSqlMessage(t, r, "getList", sql, args));
    }

    @Override
    public int update(String sql, Object... args) {
        return doDb(() -> 1,
                (t, r) -> DbLogUtil.generateSqlMessage(t, r, "update", sql, args));
    }

    @Override
    public Long updateForId(String sql, Object... args) {
        return doDb(() -> 1L, (t, r) -> DbLogUtil.generateSqlMessage(t, r, "updateForId", sql, args));
    }

    @Override
    public void execute(final String sql, Object... args) {
        doDb(() -> 0, (t, r) -> DbLogUtil.generateSqlMessage(t, r, "execute", sql, args));
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return doDb(() -> new int[]{1},
                (t, r) -> DbLogUtil.generateSqlMessage(t, "batchUpdate", sql, batchArgs));
    }

    @Override
    public HashMap<String, Integer> getTableColumnsInfo(String tableName) {
        return CacheUtil.getMemoCache().getAndSet("tableColumnsInfo", tableName, () -> {
            HashMap<String, Integer> infos = new HashMap(){
                @Override
                public boolean containsKey(Object key) {
//                    return !"IS_DELETED".equals( key);
                    return true;
                }
            };
            return VAL.of(infos, SqlHolder.properties.getTableCacheTime());
        });
    }
}
