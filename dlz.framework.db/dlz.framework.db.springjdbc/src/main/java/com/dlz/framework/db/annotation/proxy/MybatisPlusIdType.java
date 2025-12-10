package com.dlz.framework.db.annotation.proxy;

import com.dlz.framework.db.annotation.IdType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MybatisPlusIdType {
    private final Class<Annotation> idTypeAnnotation;
    private final Method valueMethod;
    private final Method typeMethod;
    protected MybatisPlusIdType() {
        Class<Annotation> idType1;
        Method valueMethodTmp;
        Method typeMethodTmp;
        try {
            idType1 = (Class<java.lang.annotation.Annotation>) Class.forName("com.baomidou.mybatisplus.annotation.TableId");
            valueMethodTmp = idType1.getMethod("value");
            typeMethodTmp = idType1.getMethod("type");
        } catch (Exception ex) {
            idType1 = null;
            valueMethodTmp = null;
            typeMethodTmp = null;
        }
        idTypeAnnotation = idType1;
        valueMethod = valueMethodTmp;
        typeMethod = typeMethodTmp;
    }

    public String value(Field field){
        if (field == null||idTypeAnnotation==null){
            return null;
        }
        if (field.isAnnotationPresent(idTypeAnnotation)) {
            java.lang.annotation.Annotation mpAnnotation = field.getAnnotation(idTypeAnnotation);
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

    public IdType type(Field field){
        if (field == null||idTypeAnnotation==null){
            return null;
        }
        if (field.isAnnotationPresent(idTypeAnnotation)) {
            java.lang.annotation.Annotation mpAnnotation = field.getAnnotation(idTypeAnnotation);
            try {
                String value = typeMethod.invoke(mpAnnotation).toString();
                switch ( value){
                    case "AUTO":
                        return IdType.AUTO;
                    case "INPUT":
                        return IdType.INPUT;
                    case "NONE":
                        return IdType.SEQ;
                    case "ASSIGN_ID":
                        return IdType.ASSIGN_ID;
                    case "ASSIGN_UUID":
                        return IdType.ASSIGN_UUID;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
