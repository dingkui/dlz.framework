package com.dlz.framework.db.convertor.result.impl;

import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.convertor.result.ANameConverter;
import com.dlz.framework.db.convertor.result.ILogicServer;
import com.dlz.framework.holder.SpringHolder;

public class DictConverter extends ANameConverter<Object,String,String> {

	ILogicServer<Object,String> logicServer=null;
	public DictConverter(String name, String para,String logicServer) {
		super(name, para);
		this.logicServer=SpringHolder.getBean(logicServer);
	}
	@Override
	public String conver2Str(Object o) {
		if(logicServer!=null){
			return ValUtil.toStr(logicServer.conver2Str(o, getPara()));
		}
		return null;
	}

	@Override
	public Object conver2Db(String o) {
		if(logicServer!=null){
			return logicServer.conver2Str(o, getPara());
		}
		return null;
	}
}
