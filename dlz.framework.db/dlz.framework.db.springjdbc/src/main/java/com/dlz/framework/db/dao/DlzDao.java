package com.dlz.framework.db.dao;

import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.framework.db.modal.result.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class DlzDao implements IDlzDao {
    private final JdbcTemplate dao;
    private final RowMapper<ResultMap> rowMapper;

    public DlzDao(JdbcTemplate jdbcTemplate) {
        this.dao = jdbcTemplate;
        DbTypeEnum dbtype = SqlHolder.properties.getDbtype();
        if (dbtype == DbTypeEnum.ORACLE) {
            rowMapper = new OracleColumnMapRowMapper();
        } else if (dbtype == DbTypeEnum.MYSQL || dbtype == DbTypeEnum.POSTGRESQL) {
            rowMapper = new MySqlColumnMapRowMapper();
        } else {
            rowMapper = new ResultMapRowMapper();
        }
        DbLogUtil.showCaller = SqlHolder.properties.getLog().isShowCaller();
        DbLogUtil.showRunSql = SqlHolder.properties.getLog().isShowRunSql();
        DbLogUtil.showResult = SqlHolder.properties.getLog().isShowResult();
    }

    @Override
    public List<ResultMap> query(String sql, Object... args) throws DataAccessException {
        return args.length > 0 ? dao.query(sql, rowMapper, args) : dao.query(sql, rowMapper);
    }

    @Override
    public int update(String sql, Object... args) {
        return doDb(() -> args.length > 0 ? dao.update(sql, args) : dao.update(sql),
                (t,r) -> DbLogUtil.generateSqlMessage(t,r, "update", sql, args));
    }

    @Override
    public Long updateForId(String sql, Object... args) {
        return doDb(() -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            dao.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Object arg : args) {
                    ps.setObject(i++, arg);
                }
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() == null) {
                log.warn("无自动增长主键");
                return null;
            }
            return keyHolder.getKey().longValue();
        }, (t,r) -> DbLogUtil.generateSqlMessage(t,r, "updateForId", sql, args));
    }

    @Override
    public void execute(final String sql, Object... args) {
        doDb(() -> {
            if (args.length > 0) {
                dao.execute(sql, (PreparedStatementCallback<Object>) ps -> {
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                    ps.execute();
                    return null;
                });
            } else {
                dao.execute(sql);
            }
            return 0;
        }, (t,r) -> DbLogUtil.generateSqlMessage(t,r, "execute", sql, args));
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return doDb(() -> dao.batchUpdate(sql, batchArgs),
                (t,r) -> DbLogUtil.generateSqlMessage(t, "batchUpdate", sql, batchArgs));
    }

    @Override
    public HashMap<String, Integer> getTableColumnsInfo(String tableName) {
        return CacheUtil.getMemoCache().getAndSet("tableColumnsInfo", tableName, () -> {
            // 查询表结构定义；返回表定义Map
            String sql = "select * from " + tableName + " limit 0";
            ResultSetExtractor<HashMap<String, Integer>> extractor = rs -> {
                HashMap<String, Integer> infos = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i < columnCount + 1; i++) {
                    String columnLabel = rsmd.getColumnLabel(i).toUpperCase();
                    infos.put(columnLabel, rsmd.getColumnType(i));
                }
                return infos;
            };
            return VAL.of(dao.query(sql, extractor), SqlHolder.properties.getTableCacheTime());
        });
    }
}
