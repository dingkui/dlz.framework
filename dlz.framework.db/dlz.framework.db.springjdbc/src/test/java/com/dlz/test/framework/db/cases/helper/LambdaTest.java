package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.util.system.Reflections;
import com.dlz.test.framework.db.entity.Dict;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;
public class LambdaTest {
    public static void main(String[] args) throws Exception {
        System.out.println("方法名：" + doSFunction(Dict::getA2));
    }
    private static <T, R> String doSFunction(MFunction<T, R> func) throws Exception {
//        // 直接调用writeReplace
//        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
//        writeReplace.setAccessible(true);
//          //反射调用
//        Object sl = writeReplace.invoke(func);
//        java.lang.invoke.SerializedLambda serializedLambda = (java.lang.invoke.SerializedLambda) sl;
        return Reflections.getFieldName(func);
    }
}
@FunctionalInterface
interface MFunction<T, R> extends Function<T, R>, Serializable {
}
