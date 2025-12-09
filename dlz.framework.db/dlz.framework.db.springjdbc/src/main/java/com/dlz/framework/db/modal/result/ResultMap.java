package com.dlz.framework.db.modal.result;

import com.dlz.comm.json.JSONMap;

import java.util.Date;

public class ResultMap extends JSONMap{

	private static final long serialVersionUID = -7368198549742264784L;
	public void coverDate2Str(String dateFormat){
		for(String k:super.keySet()){
			if(get(k) instanceof Date){
				put(k, getDateStr(k,dateFormat));
			}
		}
	}
}
