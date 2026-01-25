package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.WrapperInsert;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import org.junit.Test;

public class InsertWrapperTest extends SpingDbBaseTest {

    @Test
    public void insertWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        dict.setSqlKey("xxx");
        WrapperInsert<SysSql> insert = DB.Wrapper.insert(dict);
        showSql(insert,"insertWrapperTest1","insert into SYS_SQL(SQL_KEY,ID) values('xxx',123)");
    }

    @Test
    public void insertWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setSqlKey("xxx");
        WrapperInsert<SysSql> insert = DB.Wrapper.insert(dict);
        showSql(insert,"insertWrapperTest2","insert into SYS_SQL(SQL_KEY) values('xxx')");
        Long aLong = insert.insertWithAutoKey();
        System.out.println(aLong);
    }

}