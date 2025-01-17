package com.dlz.framework.db.convertor.dbtype;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import lombok.AllArgsConstructor;

import java.sql.Types;
import java.util.Map;

@AllArgsConstructor
public class TableCloumnMapper implements ITableCloumnMapper {
	final IDlzDao dao;
	@Override
	public Object converObj4Db(String tableName, String clumnName, Object value) {
		Map<String, Integer> map = dao.getTableColumnsInfo(tableName);
		if (map != null) {
			Integer dbClass = map.get(clumnName.toUpperCase());
			if(dbClass==null){
				return value;
			}
			return cover(dbClass, value);
		}
		return value;
	}
	@Override
	public boolean isClumnExists(String tableName, String clumnName) {
		Map<String, Integer> map = dao.getTableColumnsInfo(tableName);
		if (map == null) {
			return false;
		}
		return map.containsKey(clumnName.toUpperCase());
	}

	private static Object cover(Integer dbClass, Object obj) {
		switch (dbClass) {
		case Types.SMALLINT:
		case Types.INTEGER:
			return ValUtil.getInt(obj);
		case Types.DECIMAL:
		case Types.BIGINT:
		case Types.NUMERIC:
			return ValUtil.getLong(obj);
		case Types.DOUBLE:
			return ValUtil.getDouble(obj);
		case Types.FLOAT:
			return ValUtil.getFloat(obj);
		case Types.CHAR:
		case Types.VARCHAR:
			return ValUtil.getStr(obj);
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return ValUtil.getDate(obj);
		default:
			break;
		}
		return obj;
	}
}
