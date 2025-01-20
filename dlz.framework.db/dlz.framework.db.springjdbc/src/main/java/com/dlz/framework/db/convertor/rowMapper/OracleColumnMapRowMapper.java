package com.dlz.framework.db.convertor.rowMapper;


import com.dlz.framework.db.modal.ResultMap;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OracleColumnMapRowMapper  extends ResultMapRowMapper{


	@Override
	public ResultMap  mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		ResultMap mapOfColValues = new ResultMap();
		for (int i = 1; i <= columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			Object obj = null;
			String typename= rsmd.getColumnTypeName(i);
			if("NUMBER".equals(typename)){
				int scale = rsmd.getScale(i);
				int precision = rsmd.getPrecision(i);
				if(scale == 0){
					if(precision<=10)
						obj = rs.getInt(i);
					else
						obj = rs.getLong(i);
				}else if(scale>0){
					obj = rs.getDouble(i);
				}else
					obj = rs.getLong(i);
			}else{
				obj = getColumnValue(rs, i);
			}
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}
}
