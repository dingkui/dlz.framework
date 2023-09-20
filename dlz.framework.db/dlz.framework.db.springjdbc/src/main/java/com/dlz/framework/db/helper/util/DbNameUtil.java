package com.dlz.framework.db.helper.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class DbNameUtil {
    public static String getDbTableName(Class<?> clazz) {
        TableName name = clazz.getAnnotation(TableName.class);
        String tName=null;
        String schema=null;
        if (name != null) {
            if(name.value().length()>0){
                tName = name.value();
            }
            if(name.schema().length()>0){
                schema = name.schema();
            }
        }
        if(tName == null){
            tName = getDbClumnName(clazz.getSimpleName());
        }
        if(schema != null){
            tName = schema+"."+tName;
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

    public static String getDbClumnName(String field) {
        return StringUtils.camelToUnderScore(field).toUpperCase();
    }

    public static String getClumnCommont(Field field) {
        ApiModelProperty name = field.getAnnotation(ApiModelProperty.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"\\\n'`]", "");
        }
        return null;
    }
}
