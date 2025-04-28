package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.MakerUpdate;
import com.dlz.framework.db.modal.para.WrapperUpdate;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Role;
import com.dlz.test.framework.db.entity.SysSql;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 * @author dk
 */
public class UpdateWrapperTest extends SpingDbBaseTest {


	@Test
	public void dbSqlTest3() {
		Role role = new Role();
		role.setId(11L);
		role.setRoleName("xx");
		role.setRoleAlias("xx2");
		final WrapperUpdate<Role> id = DB.update(role).eq("ID", role.getId());
		showSql(id,"dbSqlTest2","update sys_role t set ROLE_ALIAS='xx2',ROLE_NAME='xx' where ID = 11 and IS_DELETED = 0");
	}

	@Test
	public void updateWrapperTest1() {
		SysSql dict = new SysSql();
		dict.setId(123L);
		dict.setName("123L");
		WrapperUpdate<SysSql> eq = DB.update(dict).eq(SysSql::getId, 123);
		showSql(eq,"updateWrapperTest1","update SYS_SQL t set NAME='123L' where ID = 123 and IS_DELETED = 0");
	}
	@Test
	public void updateWrapperTest2() {
		SysSql dict = new SysSql();
		dict.setId(123L);
		dict.setName("123L");
		WrapperUpdate<SysSql> eq = DB.update(dict);
		showSql(eq,"updateWrapperTest2","update SYS_SQL t set NAME='123L' where IS_DELETED = 0");
	}
}
