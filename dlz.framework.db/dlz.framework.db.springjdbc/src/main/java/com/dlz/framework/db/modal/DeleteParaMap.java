package com.dlz.framework.db.modal;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.warpper.Condition;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class DeleteParaMap extends CreateSqlParaMap{

	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.deleteTable";
	public DeleteParaMap(String tableName){
		super(SQL,tableName);
	}

	public DeleteParaMap where(Condition cond){
		String runsql = cond.getRunsql(this);
		if(StringUtils.isEmpty(runsql)){
			runsql="where false";
		}
		super.where(runsql);
		return this;
	}
	public int excute(ICommService service){
		return service.excuteSql(this);
	}
}
