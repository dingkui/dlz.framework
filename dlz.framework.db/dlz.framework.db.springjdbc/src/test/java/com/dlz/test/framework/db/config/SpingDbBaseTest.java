package com.dlz.test.framework.db.config;

import com.dlz.comm.util.VAL;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.map.ParaMapBase;
import com.dlz.framework.db.service.ICommService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.dlz.framework", "com.dlz.test.framework.db.config"})
@Slf4j
public class SpingDbBaseTest {
    @Autowired
    @Lazy
    protected ICommService commService;
    @Autowired
    @Lazy
    protected SqlHelper sqlHelper;
    @Test
    public void test() {
        System.out.println(commService.getMapList("select * from dlz_test_user"));
    }

    public void showSql(ParaMapBase paraMap, String fn) {
        log.debug("-------------------  "+fn+"  -------------------");
        log.debug(ValUtil.toStr(paraMap));
        VAL<String, Object[]> jdbcSql = paraMap.jdbcSql();
//        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(SqlUtil.getRunSqlByJdbc(jdbcSql.v1, jdbcSql.v2));
    }
}