package com.dlz.test.comm.util;

import com.dlz.comm.util.ExceptionTrace;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.comm.util.system.MFunction;
import com.dlz.comm.util.web.HttpEnum;
import com.dlz.test.beans.AA;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.regex.Pattern;

@Slf4j
public class FieldReflectionsTest {
	@Test
	public void test1(){
		t(AA::getA1);
		t(AA::getXxx);
	}

	private <T> void t(MFunction<T,?> property){
		Field field = FieldReflections.getField(property);
		String fieldName = field.getName();
		System.out.println(fieldName);
	}

}
