package com.dlz.framework.db.convertor;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.convertor.clumnname.AColumnNameConvertor;
import com.dlz.framework.db.convertor.clumnname.ColumnNameCamel;
import com.dlz.framework.db.convertor.dbtype.ITableCloumnMapper;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.util.bean.BeanConvert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库信息转换器
 *
 * @author dingkui 2017-06-26
 *
 */
public class ConvertUtil {
	/**
	 * 数据库字段名转换器
	 */
	public static AColumnNameConvertor columnMapper = new ColumnNameCamel();
	/**
	 * 数据库字段信息及内容转换
	 */
	public static ITableCloumnMapper tableCloumnMapper = null;


	/**
	 * 把传入的参数转换成数据库识别的参数
	 * 主要用于postgresql类似的强制类型
	 * @param tableName
	 * @param clumnName
	 * @param value
	 * @author dk 2018-09-28
	 * @return
	 */
	public static Object getVal4Db(String tableName,String clumnName,Object value) {
		return tableCloumnMapper==null?value:tableCloumnMapper.converObj4Db(tableName, clumnName, value);
	}
	/**
	 * 判断字段是否存在
	 * @param tableName
	 * @param clumnName
	 * @author dk 2018-09-28
	 * @return
	 */
	public static boolean isClumnExists(String tableName,String clumnName) {
		return tableCloumnMapper==null?true:tableCloumnMapper.isClumnExists(tableName, clumnName);
	}

	public static <T> List<T> getColumnList(List<ResultMap> r, Class<T> classs) {
		return r.stream().map((m) -> classs == null ? (T) m : ConvertUtil.getFistColumn(m, classs)).collect(Collectors.toList());
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


	public static <T> List<T> conver(List<ResultMap> r, Class<T> classs) {
		return BeanConvert.coverMap2Bean(r.stream().map(m -> (JSONMap)m).collect(Collectors.toList()), classs);
	}
	public static <T> T conver(ResultMap r, Class<T> classs) {
		return BeanConvert.coverMap2Bean(r, classs);
	}


	public static String clumn2Str(String dbKey) {
		return columnMapper.clumn2Str(dbKey);
	}
	public static String str2Clumn(String beanKey) {
		return columnMapper.str2Clumn(beanKey);
	}
	public static String str2DbClumn(String beanKey) {
		return columnMapper.str2Clumn(beanKey.replaceAll("\\s+", " "));
	}

}
