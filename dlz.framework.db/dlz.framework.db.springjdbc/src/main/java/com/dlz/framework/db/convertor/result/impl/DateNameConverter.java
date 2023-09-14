package com.dlz.framework.db.convertor.result.impl;


import com.dlz.comm.util.DateUtil;
import com.dlz.framework.db.convertor.result.ANameConverter;
import com.dlz.framework.db.enums.DateFormatEnum;

import java.util.Date;

public class DateNameConverter extends ANameConverter<Date,String,DateFormatEnum> {

	public DateNameConverter(String name,DateFormatEnum para) {
		super(name, para);
	}
	@Override
	public String conver2Str(Date o) {
		return DateUtil.format(o,getPara().getFormatStr());
	}
	@Override
	public Date conver2Db(String o) {
		return DateUtil.getDateStr(o,getPara().getFormatStr());
	}
}
