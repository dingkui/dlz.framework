package com.dlz.framework.db.modal;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.warpper.Condition;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * 构造单表的更新操作sql
 * @author dingkui
 *
 */
@Slf4j
public class UpdateParaMap extends CreateSqlParaMap{
	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.updateTable";
	private static final String STR_SETS="sets";
	public UpdateParaMap(String tableName){
		super(SQL,tableName);
	}

	public <T> void set(MFunction<T, ?> column, Object value){
		set(Reflections.getFieldName(column),value);
	}
	/**
	 * 添加要更新的值
	 * @param paraName
	 * @param value
	 * @return
	 */
	public UpdateParaMap set(String paraName,Object value){
		StringBuilder sbSets = (StringBuilder)this.getPara().get(STR_SETS);
		if(sbSets==null){
			sbSets=new StringBuilder();
			addPara(STR_SETS, sbSets);
		}
		
		paraName = ConvertUtil.str2Clumn(paraName);
		String clumnName = paraName.replaceAll("`", "");
		boolean isClumnExists = isClumnExists(clumnName);
		if(!isClumnExists){
			log.warn("clumn is not exists:"+paraName);
			return this;
		}
		
		if(sbSets.length()>0){
			sbSets.append(",");
		}
		sbSets.append(paraName);
		sbSets.append('=');
		if(value instanceof String){
			String v = ((String) value);
			if(v.startsWith("sql:")){
				sbSets.append(ConvertUtil.str2Clumn(v.substring(4)));
			}else{
				sbSets.append("#{").append(clumnName).append("}");
				addClunmnValue(clumnName, value);
			}
		}else{
			sbSets.append("#{").append(clumnName).append("}");
			addClunmnValue(clumnName, value);
		}
		return this;
	}
	/**
	 * 添加要更新的值集合
	 * @param setValues
	 * @return
	 */
	public UpdateParaMap set(Map<String,Object> setValues){
		for(String str:setValues.keySet()){
			set(str, setValues.get(str));
		}
		return this;
	}

	public UpdateParaMap where(Condition cond){
		super.where(cond.getRunsql(this));
		return this;
	}
	public int excute(ICommService service){
		return service.excuteSql(this);
	}
}
