package com.dlz.framework.db.modal.map;

import com.dlz.framework.db.holder.ServiceHolder;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class ParaMapDelete  extends AParaMapSearch<ParaMapDelete>{
	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.deleteTable";
	public ParaMapDelete(String tableName){
		super(SQL,tableName);
	}

	public int excute(){
		return ServiceHolder.getService().excuteSql(this);
	}

	@Override
	public ParaMapDelete mine() {
		return this;
	}
}
