package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.dlz.framework", "com.dlz.test.framework.db.config"})
@EnableAsync
public class SqlHelperTest {
    @Autowired
    @Lazy
    SqlHelper sqlHelper;

    @Test
    public void doTest() {
        sqlHelper.findById("1", Dict.class);
    }

}