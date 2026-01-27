package com.dlz.test.framework.db.cases.db;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.TableDelete;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;

public class TableDeleteTest extends SpingDbBaseTest {
    @Test
    public void tableDeleteTest1() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .addPara(Dict::getA2, "1")
                .sql("[id<#{id}]", new JSONMap("id", 123));
        showSql(delete, "tableDeleteTest1", "delete from t_b_dict where (id<123) and IS_DELETED = 0");
    }

    @Test
    public void tableDeleteTest1_2() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .apply("[id={0}]", "123");
        showSql(delete, "tableDeleteTest1_2", "delete from t_b_dict where (id='123') and IS_DELETED = 0");
    }

    @Test
    public void tableDeleteTest1_3() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .apply("[id={0}]", "");
        showSql(delete, "tableDeleteTest1_3", "delete from t_b_dict where IS_DELETED = 0");
    }

    @Test
    public void tableDeleteTest2() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .addPara(Dict::getA2, "1")
                .where(Condition.where().sql("[id=#{id2}]", new JSONMap("id", "123")))
                .eq("IS_DELETED", 8);

//        showSql(delete,"conditionTest1","delete from t_b_dict where IS_DELETED = 0");
        delete.execute();
    }

    @Test
    public void tableDeleteTest3() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .addPara(Dict::getA2, "1");
//        delete.where(DbBuildEnum.where.build())
        showSql(delete, "tableDeleteTest3", "delete from t_b_dict where IS_DELETED = 0");
    }

    @Test
    public void tableDeleteConditionTest1() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .addPara(Dict::getA2, "1")
                .where(Condition.where()
                        .ne(Dict::getA2, "3")
                        .eq(Dict::getA4, "2")
                        .le(Dict::getA6, "10")
                        .or(o -> o.eq(Dict::getA6, "10").eq(Dict::getA6, "10"))
                        .and(a -> a.eq(Dict::getA6, "10").eq(Dict::getA6, "10"))
                        .sql("exists (select 1 from dual where t_b_dict where 1=#{xx}) ", new JSONMap("xx", 999)));

        showSql(delete, "tableDeleteConditionTest1", "delete from t_b_dict where XXSS <> '3' and A4 = '2' and A6 <= '10' and (A6 = '10' or A6 = '10') and (A6 = '10' and A6 = '10') and (exists (select 1 from dual where t_b_dict where 1=999) ) and IS_DELETED = 0");
    }

    @Test
    public void tableDeleteConditionTest2() {
        TableDelete delete = DB.Table.delete("t_b_dict")
                .addPara(Dict::getA2, "1")
                .or(o -> o
                        .in(Dict::getA2, "3,4,5,6")
                        .in(Dict::getA2, "'31',111,5,6")
                        .in(Dict::getA2, "1")
                        .in(Dict::getA2, "sql:select 2 from dual"));
        showSql(delete, "tableDeleteConditionTest2", "delete from t_b_dict where (XXSS in (3,4,5,6) or XXSS in ('31','111','5','6') or XXSS in (1) or XXSS in (select 2 from dual)) and IS_DELETED = 0");
    }

    @Test
    public void tableDeleteConditionTest3() {
        TableDelete delete = DB.Table.delete("dh_room")
                .where(Condition.where()
                        .eq("equipment_id", 1)
                        .eq("equipment_id2", 2)
                        .and(w -> w.eq("xxId2", 3).eq("xxId1", 4))
                        .or(w -> w.eq("xxId2", 3).eq("xxId1", 4))
                        .eq("xxId3", 5));
        showSql(delete, "tableDeleteConditionTest3", "delete from dh_room where equipment_id = 1 and equipment_id2 = 2 and (XX_ID2 = 3 and XX_ID1 = 4) and (XX_ID2 = 3 or XX_ID1 = 4) and XX_ID3 = 5 and IS_DELETED = 0");
    }
}