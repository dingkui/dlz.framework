package com.dlz.framework.db.dao;

import com.dlz.framework.db.config.DlzDbProperties;
import com.dlz.framework.db.convertor.rowMapper.MySqlColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.OracleColumnMapRowMapper;
import com.dlz.framework.db.convertor.rowMapper.ResultMapRowMapper;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.modal.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 覆写jdbctemlate ，使用LowerCaseColumnMapRowMapper
 * @author kingapex
 * 2010-6-13上午11:05:32
 * 
 * 2018-10-17 dk 覆盖query和execute，去掉过多的sql debug日志,添加异常时的sql日志
 */
@Slf4j

public class MyJdbcTemplate{
	private DlzDbProperties dbProperties;
	private JdbcTemplate jdbcTemplate;

	public MyJdbcTemplate(JdbcTemplate jdbcTemplate, DlzDbProperties dbProperties) {
		this.dbProperties = dbProperties;
		this.jdbcTemplate = jdbcTemplate;
	}

	protected RowMapper<ResultMap> getMyColumnMapRowMapper() {
		if(dbProperties.getDbtype()== DbTypeEnum.ORACLE){
			return new OracleColumnMapRowMapper();
		}else if(dbProperties.getDbtype()== DbTypeEnum.MYSQL||dbProperties.getDbtype()== DbTypeEnum.POSTGRESQL){
			return new MySqlColumnMapRowMapper();
		}else{
			return new ResultMapRowMapper();
		}
	}
	public List<ResultMap> myqueryForList(String sql, Object... args) throws DataAccessException {
		if(args.length>0){
			return  jdbcTemplate.query(sql, getMyColumnMapRowMapper(),args);
		}
		return jdbcTemplate.query(sql, getMyColumnMapRowMapper());
	}
	public <T> List<T> myqueryForList(String sql, Class<T> requiredType, Object... args)  {
		if(args.length>0){
			return jdbcTemplate.queryForList(sql, requiredType,args);
		}
		return jdbcTemplate.queryForList(sql, requiredType);
	}
	public <T> T myqueryForObject(String sql, Class<T> requiredType, Object... args)  {
		if(args.length>0){
			return jdbcTemplate.queryForObject(sql, requiredType,args);
		}
		return jdbcTemplate.queryForObject(sql, requiredType);
	}
	public int myupdate(String sql, Object... args){
		if(args.length>0){
			return jdbcTemplate.update(sql, args);
		}
		return jdbcTemplate.update(sql);
	}
	public Number myupdateForId(String sql, Object... args){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			for (Object arg : args) {
				ps.setObject(i++, arg);
			}
			return ps;
		}, keyHolder);
		return keyHolder.getKey();
	}
	public void myexecute(final String sql){
		jdbcTemplate.execute(sql);
	}


}
