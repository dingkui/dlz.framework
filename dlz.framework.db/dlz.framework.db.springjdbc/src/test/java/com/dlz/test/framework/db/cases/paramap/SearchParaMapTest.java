package com.dlz.test.framework.db.cases.paramap;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.enums.DbBuildEnum;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.modal.para.MakerQuery;
import com.dlz.framework.db.modal.para.WrapperQuery;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Dict;
import com.dlz.test.framework.db.entity.Menu;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class SearchParaMapTest  extends SpingDbBaseTest{
    @Test
    public void conditionSqlTest1() {
        MakerQuery paraMap = new MakerQuery("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id}]",param));
        showSql(paraMap,"conditionSqlTest1","select * from t_b_dict t where (id='sql:id')");
    }
    @Test
    public void conditionSqlTest2_1() {
        MakerQuery paraMap = new MakerQuery("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));
        showSql(paraMap,"conditionSqlTest2_1","select * from t_b_dict t where false");
    }
    @Test
    public void conditionSqlTest2_2() {
        MakerQuery paraMap = new MakerQuery("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.setAllowFullQuery(true);
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));
        showSql(paraMap,"conditionSqlTest2_2","select * from t_b_dict t");
    }
    @Test
    public void conditionSqlTest3() {
        MakerQuery paraMap = new MakerQuery("xx","t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        JSONMap param = new JSONMap("id","sql:id");
        paraMap.where(Condition.where().sql("[id=#{id2}]",param));
        showSql(paraMap,"conditionSqlTest3","select xx from t_b_dict t where false");
    }
    @Test
    public void conditionTest1() {
        final MakerQuery paraMap = DB.select("t_b_dict")
                .addPara(Dict::getA2, "1")
                .setAllowFullQuery(true);
        showSql(paraMap,"conditionTest1","select * from t_b_dict t");
    }
    final String reult_1 = "select * from t_b_dict t where XXSS <> '3' and A4 = '2' and A6 <= '10' and (A6 = '10' or A6 = '10') or (A6 = '10' and A6 = '10') and (exists (select 1 from dual where t_b_dict where 1=999) )";
    @Test
    public void conditionWhereTest1_1() {
        MakerQuery paraMap = new MakerQuery("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(DbBuildEnum.where.build()
                .ne(Dict::getA2, "3")
                .eq(Dict::getA4, "2")
                .le(Dict::getA6, "10")
//                .and(Condition.OR())
                .and(Condition.OR().eq(Dict::getA6, "10")
                        .eq(Dict::getA6, "10"))
                .or(Condition.AND().eq(Dict::getA6, "10")
                        .eq(Dict::getA6, "10"))
                .sql("exists (select 1 from dual where t_b_dict where 1=#{xx}) ",new JSONMap("xx",999)))
        ;
        showSql(paraMap,"conditionWhereTest1_1",reult_1);
    }
    @Test
    public void conditionWhereTest1_2() {
        MakerQuery paraMap = new MakerQuery("t_b_dict");
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
        showSql(paraMap,"conditionWhereTest1_2", reult_1);

    }
    String reult_2="select * from t_b_dict t where XXSS in (3,4,5,6) or XXSS in ('31','111','5','6') or XXSS in (1) or XXSS in (SELECT 2 FROM DUAL)";
    @Test
    public void conditionWhereTest2_1() {
        MakerQuery paraMap = DB.select("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.where(DbBuildEnum.where.build()
                .in(Dict::getA2, "3,4,5,6")
                .or(Condition.AND().in(Dict::getA2, "'31',111,5,6"))
                .or(Condition.AND().in(Dict::getA2, "1"))
                .or(Condition.AND().in(Dict::getA2, "sql:select 2 from dual")))
        ;
        showSql(paraMap,"conditionWhereTest2_1",reult_2);
    }
    @Test
    public void conditionWhereTest2_2() {
        MakerQuery paraMap = DB.select("t_b_dict");
        paraMap.addPara(Dict::getA2, "1");
        paraMap.in(Dict::getA2, "3,4,5,6")
                .or(Condition.AND().in(Dict::getA2, "'31',111,5,6"))
                .or(Condition.AND().in(Dict::getA2, "1"))
                .or(Condition.AND().in(Dict::getA2, "sql:select 2 from dual"));
        showSql(paraMap,"conditionWhereTest2_1",reult_2);
    }
    @Test
    public void conditionWhereTest3_1() {
        Menu menu = new Menu();
        menu.setId(100L);
        menu.setCode("qsm");
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.query(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.and(Condition.OR()
                .eq(Menu::getCode, menu.getCode())
                .or(Condition.AND().eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_1","select * from sys_menu t where ID <> 100 and (CODE = 'qsm' or (NAME = '全生命周期项目' and CATEGORY = '1')) and IS_DELETED = 0");
    }

    @Test
    public void conditionWhereTest3_2() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.query(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.and(Condition.OR()
                .eq(Menu::getCode, menu.getCode())
                .or(Condition.AND().eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_2","select * from sys_menu t where and (CODE = 'qsm' or (NAME = '全生命周期项目' and CATEGORY = '1')) and IS_DELETED = 0");
    }

    @Test
    public void conditionWhereTest3_3() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.query(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.eq(Menu::getCategory, "1");
        menuQueryWrapper.and(xx-> xx.eq(Menu::getCode, menu.getCode()).eq(Menu::getName, "1"));
        showSql(menuQueryWrapper,"conditionWhereTest3_3","select * from sys_menu t where CATEGORY = '1' and (CODE = 'qsm' or NAME = '1') and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest3_4() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.query(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.eq(Menu::getCategory, "1");
        menuQueryWrapper.or(xx-> xx.eq(Menu::getCode, menu.getCode()).eq(Menu::getName, "1"));
        showSql(menuQueryWrapper,"conditionWhereTest3_4","select * from sys_menu t where CATEGORY = '1' or (CODE = 'qsm' and NAME = '1') and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest3_5() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.query(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.and(xx->
                xx.eq(Menu::getCode, menu.getCode())
                .and(xx1->xx1.eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_5");
    }
}