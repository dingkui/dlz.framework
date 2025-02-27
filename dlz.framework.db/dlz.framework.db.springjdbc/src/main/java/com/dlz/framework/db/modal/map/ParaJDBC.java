package com.dlz.framework.db.modal.map;

import java.io.Serializable;

public class ParaJDBC implements Serializable {
	private static final long serialVersionUID = 8374167270612933157L;
	public final String sql;
	public final Object[] paras;
	public static ParaJDBC of(String sqlJdbc, Object... sqlJdbcP){
		return new ParaJDBC(sqlJdbc,sqlJdbcP);
	}
	public ParaJDBC(String sqlJdbc, Object... sqlJdbcPara) {
		this.sql = sqlJdbc;
		this.paras = sqlJdbcPara;
	}
}
