package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.modal.UpdateParaMap;
import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.cons.TestConst;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = TestConst.SCAN_BASE_PACKAGES)
public class UpdateParaTest {
	@Autowired
	ICommService commService;

	@Test
	public void UpdateParaMapTest(){
		UpdateParaMap ump=new UpdateParaMap("dh_room");
		ump.addEqCondition("equipment_id", 1);
		ump.addSetValue("room_id", 1);
		commService.excuteSql(ump);
	}
}
