//package com.dlz.framework.db.cache;
//
//import com.dlz.comm.util.ExceptionUtils;
//import com.dlz.comm.util.ValUtil;
//import com.dlz.framework.db.SqlUtil;
//import com.dlz.framework.db.convertor.dbtype.ATableCloumnMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Lazy
//@Slf4j
//public class TableCloumnCache extends ATableCloumnMapper {
//	@Autowired
//	DataSource dataSource;
//	public TableCloumnCache() {
//		super(TableCloumnCache.class.getSimpleName());
//		dbOperator = new DbOperator() {
//			protected HashMap<String, Integer> getFromDb(String tableName) {
//				// 查询表结构定义；返回表定义Map
//				String sql = "select * from " + tableName.toUpperCase() + " where 1=0";
//				Connection connection=null;
//				Statement statement = null;
//				ResultSet resultSet = null;
//
//				HashMap<String, Integer> infos = new HashMap<>();
//				try{
//					connection = dataSource.getConnection();
//					statement = connection.prepareStatement(sql);
//					resultSet = statement.executeQuery(sql);
//					ResultSetMetaData rsmd = resultSet.getMetaData();
//					int columnCount = rsmd.getColumnCount();
//					for (int i = 1; i < columnCount + 1; i++) {
//						String columnLabel = CovertUtil.clumn2Str(rsmd.getColumnLabel(i));
//						infos.put(columnLabel, rsmd.getColumnType(i));
//					}
//					return infos;
//				}catch (SQLException e){
//					log.error(ExceptionUtils.getStackTrace(e));
//					return infos;
//				}finally {
//					if(resultSet != null){
//						try {
//							resultSet.close();
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//					}
//					if(statement != null){
//						try {
//							statement.close();
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//					}
//					if(connection != null){
//						try {
//							connection.close();
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		};
//	}
//
//	@Override
//	public Object converObj4Db(String tableName, String clumnName, Object value) {
//		Map<String, Integer> map = get(tableName.toUpperCase());
//		if (map != null) {
//			Integer dbClass = map.get(CovertUtil.clumn2Str(clumnName));
//			if(dbClass==null){
//				return value;
//			}
//			return cover(dbClass, value);
//		}
//		return value;
//	}
//	@Override
//	public boolean isClumnExists(String tableName, String clumnName) {
//		Map<String, Integer> map = get(tableName.toUpperCase());
//		if (map == null) {
//			return false;
//		}
//		return map.containsKey(CovertUtil.clumn2Str(clumnName));
//	}
//
//	private static Object cover(Integer dbClass, Object obj) {
//		switch (dbClass) {
//		case Types.SMALLINT:
//		case Types.INTEGER:
//			return ValUtil.toInt(obj);
//		case Types.DECIMAL:
//		case Types.BIGINT:
//		case Types.NUMERIC:
//			return ValUtil.toLong(obj);
//		case Types.DOUBLE:
//			return ValUtil.toDouble(obj);
//		case Types.FLOAT:
//			return ValUtil.toFloat(obj);
//		case Types.CHAR:
//		case Types.VARCHAR:
//			return ValUtil.toStr(obj);
//		case Types.DATE:
//		case Types.TIME:
//		case Types.TIMESTAMP:
//			return ValUtil.toDate(obj);
//		default:
//			break;
//		}
//		return obj;
//	}
//}
