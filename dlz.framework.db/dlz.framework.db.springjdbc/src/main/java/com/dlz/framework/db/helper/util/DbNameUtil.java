package com.dlz.framework.db.helper.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.convertor.DbConvertUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;

public class DbNameUtil {
    public static String getDbTableName(Class<?> clazz) {
        TableName name = clazz.getAnnotation(TableName.class);
        String tName = null;
        String schema = null;
        if (name != null) {
            if (name.value().length() > 0) {
                tName = name.value();
            }
        }
        if (tName == null) {
            tName = getDbClumnName(clazz.getSimpleName()).replaceAll("^_","");
        }
        return tName;
    }

    public static String getTableCommont(Class<?> clazz) {
        ApiModel name = clazz.getAnnotation(ApiModel.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"'`]", "");
        }
        return null;
    }

    public static String getDbClumnName(Field field) {
        TableField name = field.getAnnotation(TableField.class);
        if (name != null) {
            if (!name.exist()) {
                return null;
            }
            if (StringUtils.isNotEmpty(name.value())) {
                return name.value();
            }
        }
        return getDbClumnName(field.getName());
    }
    public static String getDbClumnName(DlzFn property) {
        Field field = FieldReflections.getField(property);
        return field == null ? null : getDbClumnName(field);
    }

    public static String getDbClumnName(String field) {
        return DbConvertUtil.str2Clumn(field);
    }

    public static String getClumnCommont(Field field) {
        ApiModelProperty name = field.getAnnotation(ApiModelProperty.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"\\\n'`]", "");
        }
        return null;
    }



}
