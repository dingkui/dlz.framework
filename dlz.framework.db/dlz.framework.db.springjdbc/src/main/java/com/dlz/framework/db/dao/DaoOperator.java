package com.dlz.framework.db.dao;

import com.dlz.comm.exception.SystemException;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.db.modal.items.SqlItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class DaoOperator implements IDlzDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<ResultMap> rowMapper;
    private DbTypeEnum dbtype;

    public DbTypeEnum getDbtype() {
        return dbtype;
    }

    public DaoOperator(JdbcTemplate jdbcTemplate, DbTypeEnum dbtype) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbtype = dbtype;
        if(dbtype== DbTypeEnum.ORACLE){
            rowMapper = new OracleColumnMapRowMapper();
        }else if(dbtype== DbTypeEnum.MYSQL||dbtype== DbTypeEnum.POSTGRESQL){
            rowMapper = new MySqlColumnMapRowMapper();
        }else{
            rowMapper = new ResultMapRowMapper();
        }
    }
//    private ResultSetExtractor<List<ResultMap>> extractor = JdbcUtil::buildResultMapList;


    public List<ResultMap> getList(String sql, Object... args) throws DataAccessException {
        log.debug("getList:{} {}",sql,args);
        if(args.length>0){
            return  jdbcTemplate.query(sql, rowMapper,args);
        }
        return jdbcTemplate.query(sql, rowMapper);
    }
    public <T> List<T> getList(String sql, Class<T> requiredType, Object... args)  {
        if(requiredType==null){
            throw new SystemException("requiredType can not be null");
        }
        log.debug("getList type:{}:{} {}",requiredType,sql,args);
        if(args.length>0){
            return jdbcTemplate.queryForList(sql, requiredType,args);
        }
        return jdbcTemplate.queryForList(sql, requiredType);
    }

    public <T> T getObj(String sql, Class<T> requiredType, Object... args) {
        if(requiredType==null){
            throw new SystemException("requiredType can not be null");
        }
        log.debug("getObj type:{}:{} {}",requiredType,sql,args);
        if(args.length>0){
            return jdbcTemplate.queryForObject(sql, requiredType, args);
        }
        return jdbcTemplate.queryForObject(sql, requiredType);
    }

    public int update(String sql, Object... args){
        log.debug("update:{} {}",sql,args);
        if(args.length>0){
            return jdbcTemplate.update(sql, args);
        }
        return jdbcTemplate.update(sql);
    }
    public Long updateForId(String sql, Object... args){
        log.debug("updateForId:{} {}",sql,args);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object arg : args) {
                ps.setObject(i++, arg);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

//    public void executeStoredProcedureExample(String param1, String param2) {
//        String storedProcedureName = "your_procedure_name";
//
//        StoredProcedure procedure = new StoredProcedure(jdbcTemplate, storedProcedureName) {
//            @Override
//            protected void declareParameters() {
//                declareParameter(new SqlParameter("param1", Types.VARCHAR));
//                declareParameter(new SqlParameter("param2", Types.VARCHAR));
//                declareParameter(new SqlOutParameter("outputParam", Types.INTEGER));
//            }
//        };
//
//        Map<String, Object> result = procedure.execute(param1, param2);
//        int outputParam = (int) result.get("outputParam");
//        System.out.println("Output Parameter: " + outputParam);
//    }


    public void execute(final String sql,Object ... args){
        log.debug("execute:{} {}",sql,args);
        if(args.length>0){
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Object>) ps -> {
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i+1, args[i]);
                }
                ps.execute();
                return null;
            });
        }
        jdbcTemplate.execute(sql);
    }
    public int[] batchUpdate(String sql, List<Object[]> batchArgs){
        log.debug("batchUpdate:{} size:{}",sql,batchArgs.size());
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }



    @Override
    public HashMap<String, Integer> getTableColumsInfo(String tableName) {
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
        return jdbcTemplate.query(sql, extractor);
    }
}
