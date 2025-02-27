package com.dlz.test.framework.db.cases.paramap;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.map.ParaMapDelete;
import com.dlz.framework.db.modal.map.ParaMapUpdate;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
public class UpdateParaTest  extends SpingDbBaseTest {
	@Test
	public void UpdateParaMapTest(){
		ParaMapUpdate where = DB.update("Sys_Sql")
				.set("room_id", 1)
				.where(Condition.where()
						.eq("equipment_id", 1)
						.eq("equipment_id2", 1)
				);

		showSql(where,"UpdateParaMapTest","insert into SYS_SQL(SQL_KEY,ID) values('xxx',123)");
	}
	@Test
	public void DeleteParaMapTest(){
		Condition where = Condition.where()
				.eq("equipment_id", 1)
				.eq("equipment_id2", 2)
				.or(Condition.AND().eq("xxId2", 3).eq("xxId1", 4))
				.and(Condition.OR().eq("xxId2", 3).eq("xxId1", 4))
				.eq("xxId3", 5);

		ParaMapDelete dh_room = DB
				.delete("dh_room")
				.where(where);
		showSql(dh_room,"insertWrapperTest1","insert into SYS_SQL(SQL_KEY,ID) values('xxx',123)");
	}
}
