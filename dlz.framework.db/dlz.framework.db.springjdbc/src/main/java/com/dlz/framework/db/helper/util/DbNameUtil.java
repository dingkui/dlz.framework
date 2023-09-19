package com.dlz.framework.db.helper.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.convertor.ConvertUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class DbNameUtil {
    public static String getDbTableName(Class<?> clazz) {
        TableName name = clazz.getAnnotation(TableName.class);
        if (name != null) {
            return StringUtils.isEmpty(name.schema()) ? name.value() : (name.schema() + "." + name.value());
        }
        return getDbClumnName(clazz.getSimpleName());
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
//        if (beanKey == null||beanKey.indexOf("_") > -1) {
//            return beanKey;
//        }
//        return beanKey.replaceAll("([A-Z])", "_$1").toUpperCase();
        return ConvertUtil.str2Clumn(field).toUpperCase();
    }

    public static String getClumnCommont(Field field) {
        ApiModelProperty name = field.getAnnotation(ApiModelProperty.class);
        if (name != null && StringUtils.isNotEmpty(name.value())) {
            return name.value().replaceAll("[\\\"\\\n'`]", "");
        }
        return null;
    }
}
