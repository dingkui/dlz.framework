package com.dlz.framework.db.annotation.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MybatisPlusTableField {
    private final Class<Annotation> idTypeAnnotation;
    private final Method valueMethod;
    private final Method existMethod;
    protected MybatisPlusTableField() {
        Class<Annotation> idType1;
        Method valueMethodTmp;
        Method existMethodTmp;
        try {
            idType1 = (Class<Annotation>) Class.forName("com.baomidou.mybatisplus.annotation.TableField");
            valueMethodTmp = idType1.getMethod("value");
            existMethodTmp = idType1.getMethod("exist");
        } catch (Exception ex) {
            idType1 = null;
            valueMethodTmp = null;
            existMethodTmp = null;
        }
        idTypeAnnotation = idType1;
        valueMethod = valueMethodTmp;
        existMethod = existMethodTmp;
    }

    public String value(Field field){
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


    public Boolean exist(Field field){
        if (field == null||idTypeAnnotation==null){
            return null;
        }
        if (field.isAnnotationPresent(idTypeAnnotation)) {
            Annotation mpAnnotation = field.getAnnotation(idTypeAnnotation);
            try {
                return (boolean) existMethod.invoke(mpAnnotation);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
