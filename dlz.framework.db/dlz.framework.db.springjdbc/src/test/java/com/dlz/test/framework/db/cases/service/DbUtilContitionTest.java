package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.SqlKeyQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
public class DbUtilContitionTest  extends SpingDbBaseTest {
	@Test
	public void ConditionTest(){
//		ParaMap ump=new ParaMap("select 1 from dual");
//		ump.setPage(Page.build(1, 1));
//		cs.getMap(ump);
		DB.sqlSelect("select t.* from PTN t where t.id=${key.comm.pageSql} and t.cc=${a} and c=${b} and ccc")
				.addPara("a", "a${b}")
				.addPara("b", "b${c}")
				.addPara("_sql", "_sql${a}").queryOne();
//		ump2.setPage(Page.build(1, 2,"id","asc"));
	}
	@Test
	public void ConditionTest2(){
		String sql = "key.sqlTest.sqlUtil";
		SqlKeyQuery ump2=DB.sqlSelect(sql);
		ump2.addPara("a", "a1");
		ump2.addPara("b", "b1");
		ump2.addPara("d", "d1");
		ump2.addPara("c", "c1");
		ump2.addPara("_sql", "_sql${a}");
		ump2.setPage(Page.build(1, 2, Order.asc("id")));
		commService.getMap(ump2);
		System.out.println(ump2.getSqlItem().getSqlRun());
	}
}
