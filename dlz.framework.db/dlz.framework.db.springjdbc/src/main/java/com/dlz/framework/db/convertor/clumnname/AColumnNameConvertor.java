package com.dlz.framework.db.convertor.clumnname;

import com.dlz.framework.db.convertor.DbConvertUtil;

public abstract class AColumnNameConvertor {
	public AColumnNameConvertor(){
		DbConvertUtil.columnMapper=this;
	}
	public abstract String clumn2Str(String dbKey);
	public abstract String str2Clumn(String beanKey);
}
