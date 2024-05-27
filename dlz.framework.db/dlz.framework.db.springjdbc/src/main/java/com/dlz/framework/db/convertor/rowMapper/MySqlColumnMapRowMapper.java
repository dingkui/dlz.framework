package com.dlz.framework.db.convertor.rowMapper;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.support.JdbcUtils;

import com.dlz.framework.db.modal.ResultMap;

public class MySqlColumnMapRowMapper extends ResultMapRowMapper{

	@Override
	public ResultMap  mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		ResultMap mapOfColValues = new ResultMap();
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i).toLowerCase());
			Object obj;
			String typename= rsmd.getColumnTypeName(i).toUpperCase();
			if("DECIMAL".equals(typename)){
				obj = rs.getDouble(i);
			}else{
				obj = getColumnValue(rs, i);
			}
			 
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}
}
