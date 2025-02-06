package com.dlz.test.framework.db.cases.paramap;

import com.dlz.framework.db.modal.DbFactory;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateParaTest  extends SpingDbBaseTest {
	@Autowired
	ICommService commService;

	@Test
	public void UpdateParaMapTest(){
		DbFactory
				.update("dh_room")
				.set("room_id", 1)
				.where(Condition.where()
					.eq("equipment_id", 1)
					.eq("equipment_id2", 1)
				).excute();
	}
	@Test
	public void DeleteParaMapTest(){
		Condition where = Condition.where()
				.eq("equipment_id", 1)
				.eq("equipment_id2", 2)
				.or(Condition.AND().eq("xxId2", 3).eq("xxId1", 4))
				.and(Condition.OR().eq("xxId2", 3).eq("xxId1", 4))
				.eq("xxId3", 5);
		DbFactory
				.delete("dh_room")
				.where(where)
				.excute();
	}
}
