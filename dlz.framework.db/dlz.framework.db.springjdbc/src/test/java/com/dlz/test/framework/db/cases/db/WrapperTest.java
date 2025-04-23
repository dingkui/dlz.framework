package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.*;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import org.junit.Test;

public class WrapperTest extends SpingDbBaseTest {
    @Test
    public void insertWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        dict.setSqlKey("xxx");
        WrapperInsert<SysSql> insert = DB.insert(dict);
        showSql(insert,"insertWrapperTest1","insert into SYS_SQL(SQL_KEY,ID) values('xxx',123)");
    }
    @Test
    public void insertWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setSqlKey("xxx");
        WrapperInsert<SysSql> insert = DB.insert(dict);
        showSql(insert,"insertWrapperTest2","insert into SYS_SQL(SQL_KEY) values('xxx')");
        Long aLong = insert.insertWithAutoKey();
        System.out.println(aLong);
    }
    @Test
    public void updateWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        dict.setName("123L");
        WrapperUpdate<SysSql> eq = DB.update(dict).eq(SysSql::getId, 123);
        showSql(eq,"updateWrapperTest1","update SYS_SQL t set NAME='123L' where ID = 123");
    }
    @Test
    public void updateWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        dict.setName("123L");
        WrapperUpdate<SysSql> eq = DB.update(dict);
        showSql(eq,"updateWrapperTest2","update SYS_SQL t set NAME='123L' where false");
    }
    @Test
    public void deleteWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperDelete<SysSql> delete = DB.delete(SysSql.class).eq(SysSql::getId, 123);
        showSql(delete,"deleteWrapperTest1","delete from SYS_SQL t where ID = 123");
    }
    //未输入条件删除条件为false
    @Test
    public void deleteWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperDelete<SysSql> delete = DB.delete(SysSql.class);
        showSql(delete,"deleteWrapperTest2","delete from SYS_SQL t where false");
    }
    @Test
    public void searchWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        MakerQuery select = DB.select(SysSql::getId);
        showSql(select,"searchWrapperTest1","select ID from SYS_SQL t where false");
    }
    @Test
    public void searchWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperQuery<SysSql> query = DB.query(SysSql.class);
        showSql(query,"searchWrapperTest2","select * from SYS_SQL t");
    }
    @Test
    public void searchWrapperTest3() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperQuery<SysSql> query = DB.query(SysSql.class).eq(SysSql::getId, 123);
        showSql(query,"searchWrapperTest3","select * from SYS_SQL t where ID = 123");
    }
}