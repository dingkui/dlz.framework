package com.dlz.plugin.jsons.fast;

import com.dlz.comm.json.JSONMap;

/**
 * 兼容fastjson,gson等代码
 */
public class JSONObject extends JSONMap {
    public JSONObject(CharSequence json) {
        super(json);
    }

    public JSONObject(Object json) {
        super(json);
    }

    public static JSONObject parseObject(CharSequence json) {
        return new JSONObject(json);
    }

    public static JSONObject parseObject(Object json) {
        return new JSONObject(json);
    }

    public String getString(String key) {
        return getStr(key);
    }

    public Integer getInteger(String key) {
        return getInt(key);
    }

    public JSONArray getJSONArray(String key) {
        return getObj(key, JSONArray.class);
    }
}
