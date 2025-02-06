package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.db.helper.wrapper.ConditionOrWrapper;
import com.dlz.framework.util.system.Reflections;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

public class SqlHelperTest extends SpingDbBaseTest {
    @Test
    public void landaTest1() {
        long t=System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Reflections.getFields(Dict.class);
        }
        System.out.println(System.currentTimeMillis()-t);
    }
    @Test
    public void landaTest2() {
        long t=System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            Reflections.getFieldName(Dict::getDictStatus);
            Reflections.getFieldName(Dict::getA2);
            Reflections.getFieldName(Dict::getA6);
            Reflections.getFieldName(Dict::getA4);
            Reflections.getFieldName(Dict::getA5);
        }
        System.out.println(System.currentTimeMillis()-t);
    }
    @Test
    public void landaTest() {
        ConditionOrWrapper conditionOrWrapper = new ConditionOrWrapper();
        conditionOrWrapper.eq(Dict::getA2, "1");
        conditionOrWrapper.eq(Dict::getA2, "2");
    }

    @Test
    public void doTest() {
        sqlHelper.findById("1", Dict.class);
    }
}