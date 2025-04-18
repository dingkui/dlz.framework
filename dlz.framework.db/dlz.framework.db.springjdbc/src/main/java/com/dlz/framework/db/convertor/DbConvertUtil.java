package com.dlz.framework.db.convertor;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.convertor.clumnname.AColumnNameConvertor;
import com.dlz.framework.db.convertor.clumnname.ColumnNameCamel;
import com.dlz.framework.db.convertor.dbtype.ITableColumnMapper;
import com.dlz.framework.db.modal.result.ResultMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库信息转换器
 *
 * @author dingkui 2017-06-26
 *
 */
public class DbConvertUtil {
	/**
	 * 数据库字段名转换器
	 */
	public static AColumnNameConvertor columnMapper = new ColumnNameCamel();
	/**
	 * 数据库字段信息及内容转换
	 */
	public static ITableColumnMapper tableCloumnMapper = null;


	/**
	 * 将值转换成数据库字段对应的数据类型
	 * @param tableName
	 * @param clumnName
	 * @param value
	 * @author dk 2018-09-28
	 * @return
	 */
	public static Object getVal4Db(String tableName,String clumnName,Object value) {
		return tableCloumnMapper==null?value:tableCloumnMapper.converObj4Db(tableName, clumnName, value);
	}

	public static <T> List<T> getColumnList(List<ResultMap> r, Class<T> classs) {
		return r.stream().map((m) -> classs == null ? (T) m : DbConvertUtil.getFistColumn(m, classs)).collect(Collectors.toList());
	}
	/**
	 * 从Map里取得字符串
	 * @param m
	 * @author dk 2015-04-09
	 * @return
	 */
	public static Object getFistColumn(ResultMap m){
		if(m==null){
			return null;
		}
		for(String a: m.keySet()){
			if("ROWNUM_".equals(a)||"rownum".equals(a)){
				continue;
			}
			return m.get(a);
		}
		return null;
	}
	/**
	 * 从Map里取得字符串
	 * @param m
	 * @author dk 2015-04-09
	 * @return
	 */
	public static <T> T getFistColumn(ResultMap m, Class<T> classs){
		return ValUtil.toObj(getFistColumn(m), classs);
	}

	/**
	 * 数据库字段名转换成bean字段名,一般都是下划线转驼峰
	 * @param dbKey
	 * @return
	 */
	public static String clumn2Str(String dbKey) {
		return columnMapper.clumn2Str(dbKey);
	}
	public static String str2Clumn(String beanKey) {
		return columnMapper.str2Clumn(beanKey);
	}

	/**
	 * bean字段名转换成数据库字段名,一般是驼峰转下划线
	 * @param beanKey
	 * @return
	 */
	public static String str2DbClumn(String beanKey) {
		return columnMapper.str2Clumn(beanKey.replaceAll("\\s+", " "));
	}

}
