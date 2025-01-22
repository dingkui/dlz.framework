package com.dlz.test.framework.db.cases.paramap;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.modal.DbFactory;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.map.ParaMapSearch;
import com.dlz.framework.db.modal.map.ParaMapSearchColumn;
import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.dlz.framework", "com.dlz.test.framework.db.config"})
@EnableAsync
@Slf4j
public class SearchParaMapTest {
    @Autowired
    ICommService commService;
    @Test
    public void conditionSqlTest1() {
        ParaMapSearch paraMap = new ParaMapSearch("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id}]",param));
        ParaMapTestUtil.showSql(paraMap,"conditionSqlTest1");
        //输出sql：select * from t_b_dict where (id='sql:id')
    }
    @Test
    public void conditionSqlTest2() {
        ParaMapSearch paraMap = new ParaMapSearch("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));
        ParaMapTestUtil.showSql(paraMap,"conditionSqlTest2");
        //输出sql：select A2 from t_b_dict
    }
    @Test
    public void conditionTest1() {
        ParaMapSearch paraMap = new ParaMapSearch("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        ParaMapTestUtil.showSql(paraMap,"conditionTest1");
        //输出sql：select * from t_b_dict
    }
    @Test
    public void conditionTest() {
        ParaMapSearch paraMap = new ParaMapSearch("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(DbBuildEnum.where.build()
                .ne(Dict::getA2, "3")
                .eq(Dict::getA4, "2")
                .le(Dict::getA6, "10")
                .and(Condition.OR())
                .and(Condition.OR().eq(Dict::getA6, "10")
                        .eq(Dict::getA6, "10"))
                .or(Condition.AND().eq(Dict::getA6, "10")
                        .eq(Dict::getA6, "10"))
                .sql("exists (select 1 from dual where t_b_dict where 1=#{xx}) ",new JSONMap("xx",999)))
        ;
        ParaMapTestUtil.showSql(paraMap,"conditionTest");
    }


    @Test
    public void conditionTest2() {
        ParaMapSearch paraMap = DbFactory.select("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(DbBuildEnum.where.build()
                .in(Dict::getA2, "3,4,5,6")
                .or(Condition.AND().in(Dict::getA2, "'31',111,5,6"))
                .or(Condition.AND().in(Dict::getA2, "1"))
                .or(Condition.AND().in(Dict::getA2, "sql:select 2 from dual")))
        ;
        ParaMapTestUtil.showSql(paraMap,"conditionTest2");
    }
    @Test
    public void conditionSelectTest2() {
        ParaMapSearchColumn paraMap = DbFactory.select(Dict::getA2).where(Condition.where().in(Dict::getA2, "3,4,5,6"));
        ParaMapTestUtil.showSql(paraMap,"conditionSelectTest2");
        List<Long> longList1 = paraMap.getLongList();
        log.info("longList1:"+longList1);
    }
}