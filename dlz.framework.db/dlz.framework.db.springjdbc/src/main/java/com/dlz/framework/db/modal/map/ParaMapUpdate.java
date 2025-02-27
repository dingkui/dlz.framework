package com.dlz.framework.db.modal.map;

import com.dlz.framework.db.convertor.DbConvertUtil;
import com.dlz.framework.db.holder.DBHolder;
import com.dlz.framework.db.modal.DbInfoCache;
import com.dlz.comm.fn.DlzFn;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * 构造单表的更新操作sql
 * @author dingkui
 *
 */
@Slf4j
public class ParaMapUpdate extends AParaMapSearch<ParaMapUpdate>{
	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.updateTable";
	private static final String STR_SETS="sets";
	private final Map<String,Object> updateSets=new HashMap<>();
	public ParaMapUpdate(String tableName){
		super(SQL,tableName);
	}
	public void buildSql() {
		super.buildSql();
		addPara(STR_SETS, buildSets());
	}
	/**
	 * 生成更新信息
	 * @return
	 */
	private String buildSets(){
		StringBuilder sbSets = new StringBuilder();
		updateSets.entrySet().forEach(e->{
			String paraName = e.getKey();
			Object value = e.getValue();
			String clumnName = paraName.replaceAll("`", "");

			if(sbSets.length()>0){
				sbSets.append(",");
			}
			sbSets.append(paraName);
			sbSets.append('=');
			if(value instanceof String){
				String v = ((String) value);
				if(v.startsWith("sql:")){
					sbSets.append(DbConvertUtil.str2Clumn(v.substring(4)));
				}else{
					sbSets.append("#{").append(clumnName).append("}");
					addClunmnValue(clumnName, value);
				}
			}else{
				sbSets.append("#{").append(clumnName).append("}");
				addClunmnValue(clumnName, value);
			}
		});
		return sbSets.toString();
	}

	public ParaMapUpdate set(String paraName, Object value){
		paraName = DbConvertUtil.str2DbClumn(paraName);
		if(!isClumnExists(paraName)){
			log.warn("column is not exists:"+tableName+"."+paraName);
			return this;
		}
		updateSets.put(paraName, value);
		return this;
	}
	public <T> void set(DlzFn<T, ?> column, Object value){
		set(DbInfoCache.fnName(column),value);
	}

	/**
	 * 添加要更新的值集合
	 * @param setValues
	 * @return
	 */
	public ParaMapUpdate set(Map<String,Object> setValues){
		for(String str:setValues.keySet()){
			set(str, setValues.get(str));
		}
		return this;
	}

	public int excute(){
		return DBHolder.doDb(s->s.excuteSql(this));
	}

	@Override
	public ParaMapUpdate me() {
		return this;
	}
}
