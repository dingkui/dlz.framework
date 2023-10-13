package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.cons.TestConst;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 单元测试支撑类<br>
 *
 * @author dk
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = TestConst.SCAN_BASE_PACKAGES)
public class CommServiceJdbcTest{
    @Autowired
    ICommService commService;

    @Test
    public void getInt() {
        commService.getIntList("select 1 from xx where x=?", "666");
    }
}
