package com.dlz.plugin.jsons.fast;

import com.dlz.comm.json.JSONList;

import java.util.List;

/**
 * 兼容fastjson,gson等代码
 */
public class JSONArray extends JSONList {

    public JSONArray() {
        super();
    }

    public JSONArray(List<Object> list) {
        super(list);
    }

    public JSONArray(Object list) {
        super(list);
    }

    public static JSONArray parseObject(Object json) {
        return new JSONArray(json);
    }


    public String getString(int index) {
        return getStr(index);
    }

    public Integer getInteger(int index) {
        return getInt(index);
    }

    public JSONArray getJSONArray(int index) {
        return getObj(index, JSONArray.class);
    }

    public JSONObject getJSONObject(int index) {
        return getObj(index, JSONObject.class);
    }
}
