package com.dlz.test.framework.db.cases.paramap;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.helper.support.SqlHelper;
import com.dlz.framework.db.modal.SearchParaMap;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.entity.Dict;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SearchParaMapTest {
    @Autowired
    ICommService commService;

    @Test
    public void conditionTest() {
        SearchParaMap paraMap = new SearchParaMap("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.condition()
                .ne(Dict::getA2, "3")
                .eq(Dict::getA4, "2")
                .le(Dict::getA6, "10")
                .and()
                .eq(Dict::getA6, "10")
                .eq(Dict::getA6, "10")
                .end()
                .or()
                .eq(Dict::getA6, "10")
                .eq(Dict::getA6, "10")
                .end()
                .sql("exists (select 1 from dual where t_b_dict where 1=#{xx}) ",new JSONMap("xx",999))
        ;


        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        sqlItem.setSqlRun(sqlItem.getSqlDeal());
        SqlUtil.dealParmToJdbc(paraMap);
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(paraMap.getPara().getStr("xx"));
        log.debug(SqlUtil.getRunSqlByJdbc(sqlItem.getSqlJdbc(), sqlItem.getSqlJdbcPara()));
    }


    @Test
    public void conditionTest2() {
        SearchParaMap paraMap = new SearchParaMap("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.condition()
                .in(Dict::getA2, "3,4,5,6")
                .or().in(Dict::getA2, "'31',111,5,6").end()
                .or().in(Dict::getA2, "1").end()
                .or().in(Dict::getA2, "sql:select 2 from dual")
        ;

        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        sqlItem.setSqlRun(sqlItem.getSqlDeal());
        SqlUtil.dealParmToJdbc(paraMap);
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(paraMap.getPara().getStr("xx"));
        log.debug(SqlUtil.getRunSqlByJdbc(sqlItem.getSqlJdbc(), sqlItem.getSqlJdbcPara()));
    }



}