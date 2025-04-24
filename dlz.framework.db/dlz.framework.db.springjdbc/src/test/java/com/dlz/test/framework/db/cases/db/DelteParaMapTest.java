package com.dlz.test.framework.db.cases.db;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.MakerDelete;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

public class DelteParaMapTest  extends SpingDbBaseTest {
    @Test
    public void conditionSqlTest1() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","123");
        paraMap.sql("[id=#{id}]",param);
        showSql(paraMap,"conditionSqlTest1","delete from t_b_dict where (id='123')");
    }
    @Test
    public void conditionSqlTest1_2() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.apply("[id={0}]","123");
        showSql(paraMap,"conditionSqlTest1_2","delete from t_b_dict where (id='123')");
    }
    @Test
    public void conditionSqlTest1_3() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.apply("[id={0}]","");
        showSql(paraMap,"conditionSqlTest1_2","delete from t_b_dict where false");
    }
    @Test
    public void conditionSqlTest2() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","123");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));

        showSql(paraMap,"conditionTest1","delete from t_b_dict where false");
    }
    @Test
    public void conditionTest1() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
//        paraMap.where(DbBuildEnum.where.build())
        showSql(paraMap,"conditionTest1","delete from t_b_dict where false");
    }
    @Test
    public void conditionTest() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(Condition.where()
                .ne(Dict::getA2, "3")
                .eq(Dict::getA4, "2")
                .le(Dict::getA6, "10")
                .or(o->o.eq(Dict::getA6, "10").eq(Dict::getA6, "10"))
                .and(a->a.eq(Dict::getA6, "10").eq(Dict::getA6, "10"))
                .sql("exists (select 1 from dual where t_b_dict where 1=#{xx}) ",new JSONMap("xx",999)))
        ;

        showSql(paraMap,"conditionTest","delete from t_b_dict where XXSS <> '3' and A4 = '2' and A6 <= '10' and (A6 = '10' or A6 = '10') and (A6 = '10' and A6 = '10') and (exists (select 1 from dual where t_b_dict where 1=999) )");
    }


    @Test
    public void conditionTest3() {
        MakerDelete paraMap = DB.delete("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.or(o->o
                .in(Dict::getA2, "3,4,5,6")
                .in(Dict::getA2, "'31',111,5,6")
                .in(Dict::getA2, "1")
                .in(Dict::getA2, "sql:select 2 from dual"))
        ;
        showSql(paraMap,"conditionTest3","delete from t_b_dict where (XXSS in (3,4,5,6) or XXSS in ('31','111','5','6') or XXSS in (1) or XXSS in (SELECT 2 FROM DUAL))");
    }

}