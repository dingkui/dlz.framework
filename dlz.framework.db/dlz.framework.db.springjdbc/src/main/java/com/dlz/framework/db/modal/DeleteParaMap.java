package com.dlz.framework.db.modal;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class DeleteParaMap extends SearchParaMap{

	private static final long serialVersionUID = 8374167270612933157L;
	private static final String SQL="key.comm.deleteTable";
	public DeleteParaMap(String tableName){
		super(SQL,tableName,null);
	}
}
