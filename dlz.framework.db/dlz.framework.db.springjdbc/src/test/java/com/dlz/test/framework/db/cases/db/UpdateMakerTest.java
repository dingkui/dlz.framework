package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.MakerDelete;
import com.dlz.framework.db.modal.para.MakerUpdate;
import com.dlz.framework.db.modal.para.WrapperUpdate;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Role;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
public class UpdateMakerTest extends SpingDbBaseTest {
	@Test
	public void UpdateParaMapTest(){
		MakerUpdate where = DB.update("Sys_Sql")
				.set("sql_key", "1")
				.where(Condition.where()
						.eq("equipment_id", 1)
						.eq("equipment_id2", 1)
				);
		where.excute();
		showSql(where,"UpdateParaMapTest","update Sys_Sql t set sql_key='1' where equipment_id = 1 and equipment_id2 = 1 and IS_DELETED = 0");

	}
}
