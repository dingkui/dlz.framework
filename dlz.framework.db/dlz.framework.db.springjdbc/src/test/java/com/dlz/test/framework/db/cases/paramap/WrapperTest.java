package com.dlz.test.framework.db.cases.paramap;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.map.ParaMapSearchColumn;
import com.dlz.framework.db.modal.wrapper.DeleteWrapper;
import com.dlz.framework.db.modal.wrapper.InsertWrapper;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;
import com.dlz.framework.db.modal.wrapper.UpdateWrapper;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import org.junit.Test;

public class WrapperTest extends SpingDbBaseTest {
    @Test
    public void insertWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        dict.setSqlKey("xxx");
        InsertWrapper<SysSql> insert = DB.insert(dict);
        showSql(insert,"insertWrapperTest1","insert into SYS_SQL(SQL_KEY,ID) values('xxx',123)");
    }
    @Test
    public void updateWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        UpdateWrapper<SysSql> eq = DB.update(SysSql.class).set(dict).eq(SysSql::getId, 123);
        showSql(eq,"updateWrapperTest1","update SYS_SQL t set ID=123 where ID = 123");
    }
    @Test
    public void updateWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        UpdateWrapper<SysSql> eq = DB.update(SysSql.class).set(dict);
        showSql(eq,"updateWrapperTest2","update SYS_SQL t set ID=123 where false");
    }
    @Test
    public void deleteWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        DeleteWrapper<SysSql> delete = DB.delete(SysSql.class).eq(SysSql::getId, 123);
        showSql(delete,"deleteWrapperTest1","delete from SYS_SQL t where ID = 123");
    }
    //未输入条件删除条件为false
    @Test
    public void deleteWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        DeleteWrapper<SysSql> delete = DB.delete(SysSql.class);
        showSql(delete,"deleteWrapperTest2","delete from SYS_SQL t where false");
    }
    @Test
    public void searchWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        ParaMapSearchColumn select = DB.select(SysSql::getId);
        showSql(select,"searchWrapperTest1","select ID from SYS_SQL t");
    }
    @Test
    public void searchWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        QueryWrapper<SysSql> query = DB.query(SysSql.class);
        showSql(query,"searchWrapperTest2","select * from SYS_SQL t");
    }
    @Test
    public void searchWrapperTest3() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        QueryWrapper<SysSql> query = DB.query(SysSql.class).eq(SysSql::getId, 123);
        showSql(query,"searchWrapperTest3","select * from SYS_SQL t where ID = 123");
    }


}