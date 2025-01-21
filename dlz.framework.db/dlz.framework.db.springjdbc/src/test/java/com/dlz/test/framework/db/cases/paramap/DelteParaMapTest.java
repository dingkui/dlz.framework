package com.dlz.test.framework.db.cases.paramap;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.modal.DeleteParaMap;
import com.dlz.framework.db.modal.ParaMapFactory;
import com.dlz.framework.db.modal.DeleteParaMap;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.warpper.Condition;
import com.dlz.test.framework.db.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
@Slf4j
public class DelteParaMapTest {
    @Autowired
    ICommService commService;
    @Test
    public void conditionSqlTest1() {
        DeleteParaMap paraMap = ParaMapFactory.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","123");
        paraMap.where(Condition.where().sql("[id=#{id}]",param));
//        paraMap.where(DbBuildEnum.where.build().eq(Dict::getA2, "3"));

        ParaMapTestUtil.showSql(paraMap);
        //delete from t_b_dict where (id='123')
    }
    @Test
    public void conditionSqlTest2() {
        DeleteParaMap paraMap = ParaMapFactory.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","123");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));

        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        sqlItem.setSqlRun(sqlItem.getSqlDeal());
        SqlUtil.dealParmToJdbc(paraMap);
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(SqlUtil.getRunSqlByJdbc(sqlItem.getSqlJdbc(), sqlItem.getSqlJdbcPara()));
        //delete from t_b_dict where false
    }
    @Test
    public void conditionTest1() {
        DeleteParaMap paraMap = ParaMapFactory.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
//        paraMap.where(DbBuildEnum.where.build())

        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        sqlItem.setSqlRun(sqlItem.getSqlDeal());
        SqlUtil.dealParmToJdbc(paraMap);
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(SqlUtil.getRunSqlByJdbc(sqlItem.getSqlJdbc(), sqlItem.getSqlJdbcPara()));
    }
    @Test
    public void conditionTest() {
        DeleteParaMap paraMap = ParaMapFactory.delete("t_b_dict");
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


        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        sqlItem.setSqlRun(sqlItem.getSqlDeal());
        SqlUtil.dealParmToJdbc(paraMap);
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(SqlUtil.getRunSqlByJdbc(sqlItem.getSqlJdbc(), sqlItem.getSqlJdbcPara()));
    }


    @Test
    public void conditionTest2() {
        DeleteParaMap paraMap = ParaMapFactory.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(DbBuildEnum.where.build()
                .in(Dict::getA2, "3,4,5,6")
                .or(Condition.AND().in(Dict::getA2, "'31',111,5,6"))
                .or(Condition.AND().in(Dict::getA2, "1"))
                .or(Condition.AND().in(Dict::getA2, "sql:select 2 from dual")))
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