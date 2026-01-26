package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.modal.DB;
import com.dlz.framework.db.modal.para.WrapperQuery;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.test.framework.db.config.SpingDbBaseTest;
import com.dlz.test.framework.db.entity.Dict;
import com.dlz.test.framework.db.entity.SysSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;


/**
 * 单元测试支撑类<br>
 *
 * @author dk
 */
@Slf4j
public class CommServiceQwTest  extends SpingDbBaseTest {
    @Test
    public void getBeanList1() {
        Dict dict = new Dict();
        DB.Wrapper.select(dict).queryBeanList();
    }
    @Test
    public void getBeanList1N() {
        SysSql dict = new SysSql();
        dict.setId(1l);

        List<SysSql> beanList = commService.getBeanList(WrapperQuery.wrapper(dict));
        log.info("beanList:"+beanList);
    }
    @Test
    public void getPage1() {
        SysSql dict = new SysSql();
//        dict.setId(1l);
        Page<SysSql> dictPage = WrapperQuery.wrapper(dict)
//                .eq(SysSql::getId, 0l)
                .orderByAsc(SysSql::getId)
                .page(1, 2)
                .queryBeanPage();
//        Page<SysSql> beanList = QueryWrapper.wrapper(dict).queryPage();
        log.info("beanList:"+dictPage);
    }

    @Test
    public void getBeanList() {
        Dict dict = new Dict();
        dict.setA2(11);
        dict.setA3(true);

        commService.getBeanList(WrapperQuery.wrapper(dict));
    }



    @Test
    public void getBeanList2() {
        DB.Wrapper.select(Dict.class).columns(Dict::getA2).eq("xx",1).queryStr();
        DB.Wrapper.select(Dict.class).columns(Dict::getA2).eq(Dict::getA7,1).queryStrList();
        DB.Wrapper.select(Dict.class).eq(Dict::getA2,1).queryBean();

        DB.insert(new Dict()).execute();


    }
}
