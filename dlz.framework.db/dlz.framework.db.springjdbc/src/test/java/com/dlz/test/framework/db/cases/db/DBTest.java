package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.JdbcQuery;
import com.dlz.framework.db.modal.para.MakerQuery;
import com.dlz.framework.db.modal.result.Order;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DBTest extends SpingDbBaseTest{

    @Test
    public void dbSqlTest1() {
        final MakerQuery eq = DB.select(Role::getRoleAlias)
                .in(Role::getId, "11,22")
                .eq(Role::getIsDeleted, 0);
        showSql(eq,"dbSqlTest1","select ROLE_ALIAS from sys_role t where ID in (11,22) and IS_DELETED = 0");
    }


    @Test
    public void dbSqlTest2() {
        final MakerQuery eq = DB.select(Role::getRoleAlias)
                .in(Role::getId, "a11,x22")
                .eq(Role::getIsDeleted, 0);
        showSql(eq,"dbSqlTest2","select ROLE_ALIAS from sys_role t where ID in ('a11','x22') and IS_DELETED = 0");
    }
}