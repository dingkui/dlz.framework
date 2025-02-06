package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import org.junit.Test;


public class SqlHelper2Test extends SpingDbBaseTest {
    @Test
    public void landaTest1() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://192.168.1.126:3306/elec?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true");
//        dataSource.setUsername("root");
//        dataSource.setPassword("1234qwer");
//
//        SqlHolder.init(new DlzDbProperties());
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        DlzDao dao = new DlzDao(jdbcTemplate);
//        DbOpMysql dbOpMysql = new DbOpMysql(dao);

        TableInfo sys_test = sqlHelper.getTableInfo("sys_code");
        System.out.println(sys_test);
    }
    @Test
    public void lamdaTest2() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://192.168.1.126:3306/elec");
//        dataSource.setUsername("root");
//        dataSource.setPassword("1234qwer");
//
//        SqlHolder.init(new DlzDbProperties());
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        DlzDao dao = new DlzDao(jdbcTemplate);
//        DbOpMysql dbOpMysql = new DbOpMysql(dao);

        TableInfo sys_test = sqlHelper.getTableInfo("elec_graph");
        System.out.println(sys_test);
    }
}