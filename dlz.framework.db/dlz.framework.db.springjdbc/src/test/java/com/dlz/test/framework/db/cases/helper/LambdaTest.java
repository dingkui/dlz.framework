package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.util.system.MFunction;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

public class LambdaTest {


    @Test
    public void LambdaTest1() {
        System.out.println("方法名：" + doSFunction(Dict::getA2));
    }
    private <T, R> String doSFunction(MFunction<T, R> func) {
//        // 直接调用writeReplace
//        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
//        writeReplace.setAccessible(true);
//          //反射调用
//        Object sl = writeReplace.invoke(func);
//        java.lang.invoke.SerializedLambda serializedLambda = (java.lang.invoke.SerializedLambda) sl;
        return DbNameUtil.getDbClumnName(func);
    }
}
