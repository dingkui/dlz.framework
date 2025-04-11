package com.dlz.framework.db.modal.items;

import java.io.Serializable;

public class JdbcItem implements Serializable {
	private static final long serialVersionUID = 8374167270612933157L;
	public final String sql;
	public final Object[] paras;
	public static JdbcItem of(String sqlJdbc, Object... sqlJdbcP){
		return new JdbcItem(sqlJdbc,sqlJdbcP);
	}
	public JdbcItem(String sqlJdbc, Object... sqlJdbcPara) {
		this.sql = sqlJdbc;
		this.paras = sqlJdbcPara;
	}
}
