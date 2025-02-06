package com.dlz.test.framework.db.cases.service;

import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;


/**
 * 单元测试支撑类<br>
 *
 * @author dk
 */

public class CommServiceJdbcTest extends SpingDbBaseTest {
    @Test
    public void getInt() {
        commService.getIntList("select 1 from xx where x=?", "666");
    }
}
