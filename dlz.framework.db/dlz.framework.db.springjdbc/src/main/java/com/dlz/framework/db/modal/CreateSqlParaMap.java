package com.dlz.framework.db.modal;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.enums.DbOprateEnum;
import com.dlz.comm.exception.DbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 构造单表的增删改查操作sql
 * @author dingkui
 *
 */
public class CreateSqlParaMap extends BaseParaMap{

	private static Logger logger=LoggerFactory.getLogger(CreateSqlParaMap.class);
	private static final long serialVersionUID = 8374167270612933157L;
	protected static final String STR_TABLENAME="tableName";
	protected static final String STR_WHERE="where";
	protected static final String STR_COLUMS="colums";
	private String tableName;
	@SuppressWarnings("rawtypes")
	protected CreateSqlParaMap(String Sql, String tableName, Page page){
		super(Sql,page);
		this.tableName=tableName;
		addPara(STR_TABLENAME, tableName);
		addPara(STR_WHERE, new StringBuilder(" where 2=3"));
	}
	public void setWhere(String where){
		addPara(STR_WHERE, where);
	}
	/**
	 * 添加参数
	 * @param key
	 * @param value
	 * @return
	 */
	public CreateSqlParaMap addClunmnValue(String key,Object value){
		return addClunmnValue(key, key, value);
	}
	/**
	 * 添加参数
	 * @param key
	 * @param value
	 * @return
	 */
	public CreateSqlParaMap addClunmnValue(String key,String clumnName,Object value){
		addPara(key, ConvertUtil.getVal4Db(tableName,clumnName, value));
		return this;
	}

	/**
	 * 添加参数
	 * @param paraName
	 * @param option
	 * @param value
	 * @return
	 */
	public void addCondition(String paraName, DbOprateEnum option, Object value){
		if(value==null||"".equals(value)){
			throw new DbException("addCondition 参数不能为空:table="+this.getPara().get(STR_TABLENAME)+",paraName="+paraName+",option="+option,1002);
		}

		paraName = ConvertUtil.str2Clumn(paraName);
		String clumnName = paraName.replaceAll("`", "");
		boolean isClumnExists = isClumnExists(clumnName);
		if(!isClumnExists){
			logger.warn("clumn is not exists:"+paraName);
			return;
		}
		StringBuilder sbWhere = (StringBuilder)this.getPara().get(STR_WHERE);
//		if(sbWhere==null){
//			sbWhere=new StringBuilder(" where 2=3");
//			addPara(STR_WHERE, sbWhere);
//		}
		if(sbWhere.toString().endsWith("2=3")){
			sbWhere.replace(7, 10, "1=1");
		}
		sbWhere.append(" and ");
		sbWhere.append(option.getCondition(paraName,value,this));
	}

	/**
	 * 判断字段是否存在
	 * @param clumnName
	 * @return
	 */
	public boolean isClumnExists(String clumnName){
		return ConvertUtil.isClumnExists(tableName, clumnName);
	}


	/**
	 * 添加要更新的值和更新条件集合
	 * @param paraName
	 * @param value
	 * @return
	 */
	public CreateSqlParaMap addEqCondition(String paraName,Object value){
		addCondition(paraName, DbOprateEnum.eq, value);
		return this;
	}
//
//	public void addRequestPara(Map<String,?> requestParaMap){
//		for(String key:requestParaMap.keySet()){
//			if(key.startsWith("search_")){
//				String[] keys=key.split("_");
//				if(keys.length<3){
//					continue;
//				}
//				String k=key.substring(keys[0].length()+keys[1].length()+2);
//				if(k.length()==0){
//					continue;
//				}
//				Object o=requestParaMap.get(key);
//				if(o instanceof String[]){
//					String[] paras=(String[])o;
//					if(paras.length==1){
//						addCondition(k,keys[1], paras[0]);
//					}else{
//						addCondition(k,keys[1], paras);
//					}
//				}else{
//					addCondition(k,keys[1], o);
//				}
//			}
//		}
//	}
}
