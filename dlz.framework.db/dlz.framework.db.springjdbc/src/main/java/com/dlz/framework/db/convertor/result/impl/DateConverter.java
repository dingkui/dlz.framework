package com.dlz.framework.db.convertor.result.impl;


import com.dlz.comm.util.DateUtil;
import com.dlz.framework.db.convertor.result.AClassConverter;
import com.dlz.framework.db.enums.DateFormatEnum;

import java.util.Date;

public class DateConverter extends AClassConverter<Date,String,DateFormatEnum> {

	private static String COVERCLASS_TIME="java.sql.Timestamp";
	public DateConverter(DateFormatEnum para) {
		super(para);
	}

	@Override
	public String conver2Str(Date o) {
		return DateUtil.format(o,para.getFormatStr());
	}

	@Override
	public String getCoverClass() {
		return COVERCLASS_TIME;
	}
	
	@Override
	public Date conver2Db(String o) {
		return DateUtil.getDateStr(o,para.getFormatStr());
	}
}
