package com.dlz.test.framework.db.cases.helper;

import com.dlz.framework.db.config.DlzDbProperties;
import com.dlz.framework.db.dao.DaoOperator;
import com.dlz.framework.db.helper.bean.TableInfo;
import com.dlz.framework.db.helper.support.dbs.DbOpMysql;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class SqlHelper2Test {
    @Test
    public void landaTest1() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.1.126:3306/elec?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true");
        dataSource.setUsername("root");
        dataSource.setPassword("1234qwer");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DlzDbProperties dbProperties = new DlzDbProperties();
        DaoOperator dao = new DaoOperator(jdbcTemplate, dbProperties);
        DbOpMysql dbOpMysql = new DbOpMysql(dao);

        TableInfo sys_test = dbOpMysql.getTableInfo("sys_code");
        System.out.println(sys_test);
    }
    @Test
    public void lamdaTest2() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.1.126:3306/elec");
        dataSource.setUsername("root");
        dataSource.setPassword("1234qwer");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DlzDbProperties dbProperties = new DlzDbProperties();
        DaoOperator dao = new DaoOperator(jdbcTemplate, dbProperties);
        DbOpMysql dbOpMysql = new DbOpMysql(dao);

        TableInfo sys_test = dbOpMysql.getTableInfo("elec_graph");
        System.out.println(sys_test);
    }
}