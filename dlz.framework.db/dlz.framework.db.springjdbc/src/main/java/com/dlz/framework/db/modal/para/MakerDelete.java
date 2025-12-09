package com.dlz.framework.db.modal.para;

import com.dlz.framework.db.inf.IOperatorDelete;

/**
 * 构造单表的删除操作sql
 * @author dingkui
 *
 */
public class MakerDelete extends AMakerSearch<MakerDelete> implements IOperatorDelete<MakerDelete> {
	private static final long serialVersionUID = 8374167270612933157L;
	public MakerDelete(String tableName){
		super(tableName);
	}
	@Override
	public MakerDelete me() {
		return this;
	}

	@Override
	public String getSql() {
		return MakerUtil.MAKER_SQL_DELETE;
	}
}
