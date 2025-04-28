package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DBTest extends SpingDbBaseTest{

    @Test
    public void insertOrUpdateTest1() {
        SysSql dict = new SysSql();
        dict.setName("xx");
        DB.insertOrUpdate(dict);
    }

    @Test
    public void saveTest1() {
        SysSql dict = new SysSql();
        dict.setName("xx");
        DB.save(dict);
    }
    @Test
    public void updateByIdTest1() {
        SysSql dict = new SysSql();
        dict.setId(1l);
        dict.setName("xx");
        DB.updateById(dict);
    }

    @Test
    public void removeByIds1() {
        DB.removeByIds(SysSql.class, "1,2,3");
    }

    @Test
    public void getById1() {
        DB.getById(SysSql.class, "1");
    }

}