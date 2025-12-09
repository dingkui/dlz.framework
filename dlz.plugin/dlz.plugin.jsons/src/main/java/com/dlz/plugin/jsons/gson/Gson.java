package com.dlz.plugin.jsons.gson;

import com.dlz.comm.util.ValUtil;

public class Gson {
    public String toJson(Object src){
        return ValUtil.toStr(src);
    }
    public <T> T fromJson(Object json, Class<T> type){
        return ValUtil.toObj(json,type);
    }
}
