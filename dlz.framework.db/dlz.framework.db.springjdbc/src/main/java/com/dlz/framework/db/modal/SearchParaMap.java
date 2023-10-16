package com.dlz.framework.db.modal;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.helper.bean.Sort;
import com.dlz.framework.db.warpper.Condition;

import java.util.Map;

import static com.dlz.framework.db.enums.DbOprateEnum.where;


/**
 * 构造单表的查询操作sql
 * @author dingkui
 *
 */
@SuppressWarnings("rawtypes")
public class SearchParaMap extends CreateSqlParaMap{

	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.searchTable";
	public SearchParaMap(String tableName, Page page){
		this(tableName,"*",page);
	}
	public SearchParaMap(String tableName){
		this(tableName,"*");
	}
	public SearchParaMap(String tableName, String colums, Page page){
		super(SQL,tableName,page);
		addPara(STR_COLUMS, ConvertUtil.str2Clumn(colums));
	}
	public SearchParaMap(String tableName,String colums){
		super(SQL,tableName,null);
		addPara(STR_COLUMS, ConvertUtil.str2Clumn(colums));
	}

	private Condition condition=where.mk();
	//排序条件
	private Sort sort = new Sort();

	public void setWhere(String where){
		addPara(STR_WHERE, where);
	}

	public Condition condition(){
		return condition;
	}
	public Sort sort(){
		return sort;
	}
	public void setWhere(){
		String whereSql = this.condition.getRunsql(this);
		if (whereSql!=where.condition) {
			setWhere(whereSql);
		}
	}
	/**
	 * 添加要更新的值和更新条件集合
	 * @param conditionValues
	 * @return
	 */
	public void eqs(Map<String,Object> conditionValues){
		for(String str:conditionValues.keySet()){
			condition().eq(str, conditionValues.get(str));
		}
	}
}
