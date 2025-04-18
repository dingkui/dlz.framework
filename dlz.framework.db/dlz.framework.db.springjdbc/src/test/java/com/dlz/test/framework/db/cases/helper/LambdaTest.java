package com.dlz.test.framework.db.cases.helper;

import com.dlz.comm.fn.DlzFn;
import com.dlz.comm.util.VAL;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

public class LambdaTest {


    @Test
    public void LambdaTest1() {
        System.out.println("方法名：" + doSFunction(Dict::getA2));
    }
    private <T, R> VAL<String, String> doSFunction(DlzFn<T, R> func) {
//        // 直接调用writeReplace
//        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
//        writeReplace.setAccessible(true);
//          //反射调用
//        Object sl = writeReplace.invoke(func);
//        java.lang.invoke.SerializedLambda serializedLambda = (java.lang.invoke.SerializedLambda) sl;
        return BeanInfoHolder.fnInfo(func);
    }
}
