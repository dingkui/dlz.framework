package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.MakerDelete;
import com.dlz.framework.db.modal.para.MakerUpdate;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
public class UpdateParaTest  extends SpingDbBaseTest {
	@Test
	public void UpdateParaMapTest(){
		MakerUpdate where = DB.update("Sys_Sql")
				.set("sql_key", 1)
				.where(Condition.where()
						.eq("equipment_id", 1)
						.eq("equipment_id2", 1)
				);
		showSql(where,"UpdateParaMapTest","update Sys_Sql t set sql_key='1' where equipment_id = 1 and equipment_id2 = 1");
	}
	@Test
	public void DeleteParaMapTest(){
		Condition where = Condition.where()
				.eq("equipment_id", 1)
				.eq("equipment_id2", 2)
				.and(w->w.eq("xxId2", 3).eq("xxId1", 4))
				.or(w->w.eq("xxId2", 3).eq("xxId1", 4))
				.eq("xxId3", 5);

		MakerDelete dh_room = DB
				.delete("dh_room")
				.where(where);
		showSql(dh_room,"DeleteParaMapTest","delete from dh_room where equipment_id = 1 and equipment_id2 = 2 and (XX_ID2 = 3 and XX_ID1 = 4) and (XX_ID2 = 3 or XX_ID1 = 4) and XX_ID3 = 5");
	}
}
