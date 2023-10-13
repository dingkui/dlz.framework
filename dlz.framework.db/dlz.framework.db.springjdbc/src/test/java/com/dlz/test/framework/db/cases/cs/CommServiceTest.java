package com.dlz.test.framework.db.cases.cs;

import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.cons.TestConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.dlz.framework","com.dlz.test.framework.db.config"})
@Slf4j
public class CommServiceTest {
    @Autowired
    ICommService commService;

    @Test
    public void doTest() {

        String str = commService.getStr("select 1 from dual");
        log.trace(str);
    }
}