package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.wrapper.PojoDelete;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import org.junit.Test;

public class WrapperDeleteTest extends SpingDbBaseTest {

    @Test
    public void deleteWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        PojoDelete<SysSql> delete = DB.Pojo.delete(SysSql.class).eq(SysSql::getId, 123);
        showSql(delete,"deleteWrapperTest1","delete from SYS_SQL where ID = 123 and IS_DELETED = 0");
    }
    //未输入条件删除条件为false
    @Test
    public void deleteWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        PojoDelete<SysSql> delete = DB.Pojo.delete(SysSql.class);
        showSql(delete,"deleteWrapperTest2","delete from SYS_SQL where IS_DELETED = 0");
    }

}