package com.dlz.framework.db.convertor.dbtype;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.holder.BeanInfoHolder;
import lombok.AllArgsConstructor;

import java.sql.Types;
import java.util.Map;

@AllArgsConstructor
public class TableColumnMapper implements ITableColumnMapper {
	final IDlzDao dao;
	@Override
	public Object converObj4Db(String tableName, String clumnName, Object value) {
		Map<String, Integer> map = BeanInfoHolder.getTableColumnsInfo(tableName);
		if (map != null) {
			Integer dbClass = map.get(clumnName.toUpperCase());
			if(dbClass==null){
				return value;
			}
			return cover(dbClass, value);
		}
		return value;
	}

	private static Object cover(Integer dbClass, Object obj) {
		switch (dbClass) {
		case Types.SMALLINT:
		case Types.INTEGER:
			return ValUtil.toInt(obj);
		case Types.DECIMAL:
		case Types.BIGINT:
		case Types.NUMERIC:
			return ValUtil.toLong(obj);
		case Types.DOUBLE:
			return ValUtil.toDouble(obj);
		case Types.FLOAT:
			return ValUtil.toFloat(obj);
		case Types.CHAR:
		case Types.VARCHAR:
			return ValUtil.toStr(obj);
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			return ValUtil.toDate(obj);
		default:
			break;
		}
		return obj;
	}
}
