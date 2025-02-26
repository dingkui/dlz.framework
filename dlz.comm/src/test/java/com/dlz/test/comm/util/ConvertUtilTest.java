package com.dlz.test.comm.util;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.system.ConvertUtil;
import com.dlz.test.beans.AA;
import com.dlz.test.beans.TestA;
import com.dlz.test.beans.TestC;
import org.junit.Test;

import java.util.List;

public class ConvertUtilTest {
	@Test
	public void test1(){
		TestC c = new TestC();
		c.setC1("c");
		c.setB1("c");
		c.setA1("[{a:1}]");
		AA a= ConvertUtil.convert(c,AA.class);
		List<TestA> a1 = a.getA1();
		TestA testA = a1.get(0);
		System.out.println(new JSONMap(a));
	}


}
