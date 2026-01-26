package com.dlz.test.framework.db.cases.db;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.WrapperQuery;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Menu;
import com.dlz.test.framework.db.entity.Role;
import com.dlz.test.framework.db.entity.SysSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class QueryWrapperTest extends SpingDbBaseTest{
    @Test
    public void conditionWhereTest3_1() {
        Menu menu = new Menu();
        menu.setId(100L);
        menu.setCode("qsm");
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.or(w->w
                .eq(Menu::getCode, menu.getCode())
                .and(s->s.eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_1","select * from sys_menu t where ID <> 100 and (CODE = 'qsm' or (NAME = '全生命周期项目' and CATEGORY = '1')) and IS_DELETED = 0");
    }

    @Test
    public void conditionWhereTest3_2() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.or(w->w
                .eq(Menu::getCode, menu.getCode())
                .and(s->s.eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_2","select * from sys_menu t where (CODE = 'qsm' or (NAME = '全生命周期项目' and CATEGORY = '1')) and IS_DELETED = 0");
    }

    @Test
    public void conditionWhereTest3_3() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.eq(Menu::getCategory, "1");
        menuQueryWrapper.or(xx-> xx.eq(Menu::getCode, menu.getCode()).eq(Menu::getName, "1"));
        showSql(menuQueryWrapper,"conditionWhereTest3_3","select * from sys_menu t where CATEGORY = '1' and (CODE = 'qsm' or NAME = '1') and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest3_4() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.eq(Menu::getCategory, "1");
        menuQueryWrapper.or(xx-> xx.eq(Menu::getCode, menu.getCode()).eq(Menu::getName, "1"));
        showSql(menuQueryWrapper,"conditionWhereTest3_4","select * from sys_menu t where CATEGORY = '1' and (CODE = 'qsm' or NAME = '1') and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest3_5() {
        Menu menu = new Menu();
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.or(w->w
                .eq(Menu::getCode, menu.getCode())
                .and(xx1->xx1.eq(Menu::getName, menu.getName()).eq(Menu::getCategory, "1")));
        showSql(menuQueryWrapper,"conditionWhereTest3_5","select * from sys_menu t where (CODE = 'qsm' or (NAME = '全生命周期项目' and CATEGORY = '1')) and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest4_1() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        if (menu.getId() != null) {
            menuQueryWrapper.ne(Menu::getId, menu.getId());
        }
        menuQueryWrapper.apply("xx in (select x from dual where 1={0} and 2={1})", 1, 2);
        showSql(menuQueryWrapper, "conditionWhereTest4_1","select * from sys_menu t where ID <> 1 and (xx in (select x from dual where 1=1 and 2=2)) and IS_DELETED = 0");
    }
    @Test
    public void conditionWhereTest4_2() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setCode("qsm");
        menu.setName("全生命周期项目");
        final WrapperQuery<Menu> menuQueryWrapper = DB.Wrapper.select(Menu.class);
        menuQueryWrapper.ne(Menu::getId, menu.getId());
        menuQueryWrapper.sql("xx in (select x from dual where 1=#{a} and 2=#{b})",new JSONMap("a",1,"b",2));
        showSql(menuQueryWrapper, "conditionWhereTest4_2","select * from sys_menu t where ID <> 1 and (xx in (select x from dual where 1=1 and 2=2)) and IS_DELETED = 0");
    }


    @Test
    public void searchWrapperTest1() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperQuery select = DB.Wrapper.select(SysSql.class).columns(SysSql::getId);
        showSql(select,"searchWrapperTest1","select ID from SYS_SQL t where IS_DELETED = 0");
    }
    @Test
    public void searchWrapperTest2() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperQuery<SysSql> query = DB.Wrapper.select(SysSql.class);
        showSql(query,"searchWrapperTest2","select * from SYS_SQL t where IS_DELETED = 0");
    }
    @Test
    public void searchWrapperTest3() {
        SysSql dict = new SysSql();
        dict.setId(123L);
        WrapperQuery<SysSql> query = DB.Wrapper.select(SysSql.class).eq(SysSql::getId, 123);
        showSql(query,"searchWrapperTest3","select * from SYS_SQL t where ID = 123 and IS_DELETED = 0");
    }


    @Test
    public void dbSqlTest1() {
        final WrapperQuery eq = DB.Wrapper.select(Role.class).columns(Role::getRoleAlias)
                .in(Role::getId, "11,22")
                .eq(Role::getIsDeleted, 0);
        showSql(eq,"dbSqlTest1","select ROLE_ALIAS from sys_role t where ID in (11,22) and IS_DELETED = 0");
    }


    @Test
    public void dbSqlTest2() {
        final WrapperQuery eq = DB.Wrapper.select(Role.class).columns(Role::getRoleAlias)
                .in(Role::getId, "a11,x22")
                .eq(Role::getIsDeleted, 0);
        showSql(eq,"dbSqlTest2","select ROLE_ALIAS from sys_role t where ID in ('a11','x22') and IS_DELETED = 0");
    }
    @Test
    public void dbSqlTest21() {
        final WrapperQuery eq = DB.Wrapper.select(Role.class).columns(Role::getId)
                .in(Role::getId, "a11,x22")
                .eq(Role::getIsDeleted, 0);
        showSql(eq,"dbSqlTest2","select ID from sys_role t where ID in ('a11','x22') and IS_DELETED = 0");
    }
}