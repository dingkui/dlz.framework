package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorDelete;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class TableDelete extends AQuery<TableDelete> implements IOperatorDelete<TableDelete> {
	private static final long serialVersionUID = 8374167270612933157L;
	public TableDelete(String tableName){
		super(tableName);
	}
	@Override
	public TableDelete me() {
		return this;
	}

	@Override
	public String getSql() {
		return TableMakerUtil.MAKER_SQL_DELETE;
	}
}
