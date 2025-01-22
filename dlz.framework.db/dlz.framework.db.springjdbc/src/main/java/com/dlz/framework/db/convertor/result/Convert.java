package com.dlz.framework.db.convertor.result;

import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.convertor.result.impl.DateConverter;
import com.dlz.framework.db.convertor.result.impl.DateNameConverter;
import com.dlz.framework.db.convertor.result.impl.DictConverter;
import com.dlz.framework.db.enums.DateFormatEnum;
import com.dlz.framework.db.modal.result.ResultMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 查询结果转换工具
 *
 * @author dingkui
 */
public class Convert {
    Map<String, AClassConverter> classMap = new HashMap<>();
    Map<String, ANameConverter> nameMap = new HashMap<>();
    Map<String, AGroupConverter> groupMap = new HashMap<>();

    /**
     * 添加转换器(按类型添加)
     *
     * @param converter
     */
    public Convert addClassConvert(AClassConverter converter) {
        classMap.put(converter.getCoverClass(), converter);
        return this;
    }

    /**
     * 添加转换器(按类型添加)
     *
     * @param converter
     */
    public Convert addNameConvert(ANameConverter converter) {
        nameMap.put(converter.getName(), converter);
        return this;
    }

    /**
     * 添加转换器(按类型添加)
     *
     * @param converter
     */
    public Convert addGroupConvert(AGroupConverter converter) {
        groupMap.put(converter.getName(), converter);
        return this;
    }

    /**
     * 添加字典转换器
     *
     * @param name     字段名
     * @param dictCode 字典名称
     * @return
     */
    public Convert addDictConvert(String name, String dictCode) {
        name = ConvertUtil.clumn2Str(name);
        nameMap.put(name, new DictConverter(name, dictCode, "dictConverterLogicServer"));
        return this;
    }

    /**
     * 转换数据
     *
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public ResultMap convertMap(ResultMap map) {
        ResultMap nM = new ResultMap();
        for (String k : map.keySet()) {
            Object o = map.get(k);
            if (o == null) {
                continue;
            }
            if (groupMap.containsKey(k)) {
                groupMap.get(k).conver2Str(o, map);
            } else if (nameMap.containsKey(k)) {
                nM.put(k + "_text", nameMap.get(k).conver2Str(o));
            } else if (classMap.containsKey(o.getClass().getCanonicalName())) {
                map.put(k, classMap.get(o.getClass().getCanonicalName()).conver2Str(o));
            }
        }
        map.putAll(nM);
        return map;
    }

    /**
     * 转换数据
     *
     * @param list
     * @return
     */
    public void convertListMap(List<ResultMap> list) {
        for (ResultMap map : list) {
            convertMap(map);
        }
    }

    public void addDateConvert(DateFormatEnum datetimestr) {
        addClassConvert(new DateConverter(datetimestr));
    }

    public void addDateConvert(String name, DateFormatEnum datetimestr) {
        name = ConvertUtil.clumn2Str(name);
        nameMap.put(name, new DateNameConverter(name, datetimestr));
    }

}
