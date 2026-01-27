package com.dlz.framework.db.annotation.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MybatisPlusTableName {
    private final Class<Annotation> idTypeAnnotation;
    private final Method valueMethod;
    protected MybatisPlusTableName() {
        Class<Annotation> idType1;
        Method valueMethodTmp;
        try {
            idType1 = (Class<Annotation>) Class.forName("com.baomidou.mybatisplus.annotation.TableName");
            valueMethodTmp = idType1.getMethod("value");
        } catch (Exception ex) {
            idType1 = null;
            valueMethodTmp = null;
        }
        idTypeAnnotation = idType1;
        valueMethod = valueMethodTmp;
    }

    public String value(Class<?> field){
        if (field == null||idTypeAnnotation==null){
            return null;
        }
        if (field.isAnnotationPresent(idTypeAnnotation)) {
            Annotation mpAnnotation = field.getAnnotation(idTypeAnnotation);
            try {
                String value = (String) valueMethod.invoke(mpAnnotation);
                if (!value.isEmpty()) {
                    return value;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
