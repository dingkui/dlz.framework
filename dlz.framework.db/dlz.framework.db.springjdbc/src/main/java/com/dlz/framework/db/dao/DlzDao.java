package com.dlz.framework.db.dao;

import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.config.DlzDbProperties;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.modal.ResultMap;
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
    private JdbcTemplate dao;
    private RowMapper<ResultMap> rowMapper;
    private DlzDbProperties dbProperties;

    public DbTypeEnum getDbtype() {
        return dbProperties.getDbtype();
    }

    public DlzDao(JdbcTemplate jdbcTemplate, DlzDbProperties dbProperties) {
        this.dao = jdbcTemplate;
        this.dbProperties = dbProperties;
        DbTypeEnum dbtype = dbProperties.getDbtype();
        if(dbtype == DbTypeEnum.ORACLE){
            rowMapper = new OracleColumnMapRowMapper();
        }else if(dbtype == DbTypeEnum.MYSQL|| dbtype == DbTypeEnum.POSTGRESQL){
            rowMapper = new MySqlColumnMapRowMapper();
        }else{
            rowMapper = new ResultMapRowMapper();
        }
    }
//    private ResultSetExtractor<List<ResultMap>> extractor = JdbcUtil::buildResultMapList;

    private void logInfo(String sql, Object[] args,String methodName,long startTime){
        if(log.isDebugEnabled()) {
            long useTime = System.currentTimeMillis() - startTime;
            if (dbProperties.isShowRunSql()) {
                log.info("{} {}ms sql:{}", methodName, useTime, SqlUtil.getRunSqlByJdbc(sql, args));
            } else {
                log.info("{} {}ms sql:{} {}", methodName, useTime, sql, args);
            }
        }
    }


    public List<ResultMap> getList(String sql, Object... args) throws DataAccessException {
        long t=System.currentTimeMillis();
        try {
            return args.length>0? dao.query(sql, rowMapper, args): dao.query(sql, rowMapper);
        }finally {
            logInfo(sql, args, "getList",t);
        }
    }
    public <T> List<T> getClumnList(String sql, Class<T> requiredType, Object... args)  {
        if(requiredType==null){
            throw new SystemException("requiredType can not be null");
        }
        long t=System.currentTimeMillis();
        try {
            List<ResultMap> query = args.length>0? dao.query(sql, rowMapper, args): dao.query(sql, rowMapper);
            return ConvertUtil.getColumList(query, requiredType);
        }finally {
            logInfo(sql, args, "getClumnList",t);
        }
    }

    public <T> T getClumn(String sql, Class<T> requiredType, Object... args) {
        if(requiredType==null){
            throw new SystemException("requiredType can not be null");
        }
        long t=System.currentTimeMillis();
        try {
            List<ResultMap> query = args.length>0? dao.query(sql, rowMapper, args): dao.query(sql, rowMapper);
            return ConvertUtil.getColum(query, requiredType);
        }finally {
            logInfo(sql, args, "getClumn",t);
        }
    }

    public int update(String sql, Object... args){
        long t=System.currentTimeMillis();
        try {
            int r = args.length > 0 ? dao.update(sql, args) : dao.update(sql);
            return r;
        }finally {
            logInfo(sql, args, "update",t);
        }
    }
    public Long updateForId(String sql, Object... args){
        long t=System.currentTimeMillis();
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            dao.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Object arg : args) {
                    ps.setObject(i++, arg);
                }
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        }finally {
            logInfo(sql, args, "updateForId",t);
        }
    }

    public void execute(final String sql,Object ... args){
        long t=System.currentTimeMillis();
        try {
            if(args.length>0){
                dao.execute(sql, (PreparedStatementCallback<Object>) ps -> {
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i+1, args[i]);
                    }
                    ps.execute();
                    return null;
                });
            }else{
                dao.execute(sql);
            }
        }finally {
            logInfo(sql, args, "execute",t);
        }
    }
    public int[] batchUpdate(String sql, List<Object[]> batchArgs){
        long t=System.currentTimeMillis();
        try {
            return dao.batchUpdate(sql, batchArgs);
        }finally {
            if(log.isDebugEnabled()){
                log.debug("{} ms batchUpdate:{} size:{}",System.currentTimeMillis() - t,sql,batchArgs.size());
            }
        }
    }

    @Override
    public HashMap<String, Integer> getTableColumnsInfo(String tableName) {
        return CacheUtil.getMemoCache().getAndSetForever("tableColumnsInfo",tableName, () -> {
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
            return dao.query(sql, extractor);
        });
    }
}
