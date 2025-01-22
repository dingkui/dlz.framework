package com.dlz.framework.db.modal.map;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.modal.result.Page;

import java.util.Map;

public class ParaMap extends ParaMapBase {

	private static final long serialVersionUID = 8374167270612933157L;
	
	@SuppressWarnings("rawtypes")
	public ParaMap(String sql){
		super(sql);
	}
	public ParaMap(String sql, Page page){
		super(sql);
		this.setPage(page);
	}


	public ParaMap setLikePara(String paraName) {
		if(!StringUtils.isEmpty(getPara().get(paraName))){
			super.addPara(paraName, "%"+getPara().get(paraName)+"%");
		}
		return this;
	}
	public ParaMap addDefaultPara(String paraName,String defaultValue) {
		if(StringUtils.isEmpty(getPara().get(paraName))){
			super.addPara(paraName, defaultValue);
		}
		return this;
	}
	public ParaMap addRequestPara(Map<String,?> requestParaMap){
		for(String key:requestParaMap.keySet()){
			if(key.startsWith("search_")){
				String[] paras=(String[])requestParaMap.get(key);
				if(paras.length==1){
					addPara(key.substring("search_".length()), paras[0]);
				}else{
					addPara(key.substring("search_".length()), paras);
				}
			}
		}
		return this;
	}
}
