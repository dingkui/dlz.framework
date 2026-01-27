package com.dlz.test.framework.db.cases.docs;

import com.dlz.framework.db.modal.DB;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import com.dlz.test.framework.db.entity.User;
import org.junit.Before;
import org.junit.Test;

public class Doc00AllTest extends SpingDbBaseTest {
    @Before
    public void addSql() {
        SysSql SysSql = new SysSql();
        SysSql.setSqlKey("test");
        SysSql.setSqlValue("SELECT * FROM user WHERE and status = #{status}");
        DB.Wrapper.insert(SysSql).execute();
    }

    @Test
    public void allTest_0_1() {
        DB.Wrapper.delete(User.class).execute();
    }

}
