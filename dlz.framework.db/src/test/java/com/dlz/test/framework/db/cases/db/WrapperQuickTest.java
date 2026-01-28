package com.dlz.test.framework.db.cases.db;

import com.dlz.framework.db.ds.DataSourceProperty;
import com.dlz.framework.db.modal.DB;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.SysSql;
import com.dlz.test.framework.db.entity.Yc1Record;
import com.dlz.test.framework.db.entity.YcRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class WrapperQuickTest extends SpingDbBaseTest{

    @Test
    public void insertOrUpdateTest1() {
        SysSql dict = new SysSql();
        dict.setName("xx");
        DB.Pojo.insertOrUpdate(dict);
    }

    @Test
    public void saveTest1() {
        SysSql dict = new SysSql();
        dict.setName("xx");
        DB.Pojo.save(dict);
    }
    @Test
    public void saveTest2() {
        YcRecord dict = new Yc1Record();
        dict.setRe("xx");
        dict.setPcid("xx");
        dict.setSta(1);
        DB.Pojo.save(dict);

        YcRecord yc1Record = new Yc1Record();
        DB.Pojo.insert(yc1Record).execute();
    }
    @Test
    public void updateByIdTest1() {
        SysSql dict = new SysSql();
        dict.setId(1l);
        dict.setName("xx");
        DB.Pojo.updateById(dict);
    }

    @Test
    public void removeByIds1() {
        DB.Pojo.removeByIds(SysSql.class, "1,2,3");
    }

    @Test
    public void getById1() {
        DB.Pojo.getById(SysSql.class, "1");
    }
    @Test
    public void getUseDbById1() {

        final DataSourceProperty properties = new DataSourceProperty();
        properties.setName("test");
        properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        properties.setUsername("root");
        properties.setPassword("1234qwer");
        properties.setUrl("jdbc:mysql://192.168.1.126:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
        DB.Dynamic.setDataSource(properties);


        DB.Dynamic.use("test",()-> {
            DB.Pojo.getById(SysSql.class, "1");
            DB.Pojo.getById(SysSql.class, "2");
            return null;
        });

        DB.Pojo.getById(SysSql.class, "1");
        DB.Dynamic.use("default",()-> {
            DB.Pojo.getById(SysSql.class, "1");
            DB.Pojo.getById(SysSql.class, "2");
            return null;
        });

    }

}