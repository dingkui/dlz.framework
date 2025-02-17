package com.dlz.framework.db.modal.map;

import com.dlz.comm.util.VAL;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.util.system.FieldReflections;
import com.dlz.framework.util.system.MFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 构造单表的添加操作sql
 * @author dingkui
 *
 */
@Slf4j
public class ParaMapInsert extends ParaMapMaker {
	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.insertTable";
	private static final String STR_COLUMS="colums";
	private static final String STR_VALUES="values";
	private final Map<String,Object> insertValues=new HashMap<>();
	public ParaMapInsert(String tableName){
		super(SQL,tableName);
	}
	public void buildSql() {
		super.buildSql();
		VAL<String, String> sql = buildColumSql();
		addPara(STR_COLUMS, sql.v1);
		addPara(STR_VALUES, sql.v2);
	}

	/**
	 * 生成更新信息
	 * @return
	 */
	private VAL<String,String> buildColumSql(){
		StringBuilder sbColums = new StringBuilder();
		StringBuilder sbValues = new StringBuilder();
		insertValues.entrySet().forEach(e->{
			String paraName = e.getKey();
			Object value = e.getValue();
			String clumnName = paraName.replaceAll("`", "");

			if(sbColums.length()>0){
				sbColums.append(',');
				sbValues.append(',');
			}
			sbColums.append(paraName);
			if(value instanceof String){
				String v = ((String) value);
				if(v.startsWith("sql:")){
					sbValues.append(ConvertUtil.str2Clumn(v.substring(4)));
				}else{
					sbValues.append("#{").append(clumnName).append("}");
					addPara(clumnName, ConvertUtil.getVal4Db(tableName, clumnName, value));
				}
			}else{
				sbValues.append("#{").append(clumnName).append("}");
				if(value==null)
					value="";
				addPara(clumnName, ConvertUtil.getVal4Db(tableName, clumnName, value));
			}
		});
		return VAL.of(sbColums.toString(),sbValues.toString());
	}

	public <T> void value(MFunction<T, ?>  column, Object value){
		value(FieldReflections.getFieldName(column),value);
	}
	public ParaMapInsert value(String key, Object value){
		String paraName = ConvertUtil.str2DbClumn(key);
		boolean isClumnExists = isClumnExists(paraName);
		if(!isClumnExists){
			log.warn("column is not exists:"+tableName+"."+paraName);
			return this;
		}
		insertValues.put(paraName, value);
		return this;
	}
	
	public ParaMapInsert value(Map<String,Object> values){
		for(String str:values.keySet()){
			value(str, values.get(str));
		}
		return this;
	}
	public int excute(){
		return  DBHolder.doDb(s->s.excuteSql(this));
	}
}
