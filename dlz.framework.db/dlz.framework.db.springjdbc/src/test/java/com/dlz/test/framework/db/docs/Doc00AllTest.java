package com.dlz.test.framework.db.docs;

import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.DateUtil;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.WrapperQuery;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import com.dlz.test.framework.db.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
