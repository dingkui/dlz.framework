package com.dlz.framework.db.modal.wrapper;

import com.dlz.framework.db.modal.para.AQuery;
import com.dlz.framework.db.inf.IExecuteDelete;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class TableDelete extends AQuery<TableDelete> implements IExecuteDelete<TableDelete> {
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
		return WrapperBuildUtil.MAKER_SQL_DELETE;
	}
}
