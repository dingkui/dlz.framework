package com.dlz.test.framework.db.cases.service;

import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.db.modal.wrapper.QueryWrapper;
import com.dlz.test.framework.db.entity.Dict;
import com.dlz.test.framework.db.entity.SysSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * 单元测试支撑类<br>
 *
 * @author dk
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CommServiceQwTest {
    @Autowired
    ICommService commService;


    @Test
    public void getBeanList1() {
        Dict dict = new Dict();
        QueryWrapper.wrapper(dict).queryBeanList();
    }
    @Test
    public void getBeanList1N() {
        SysSql dict = new SysSql();
        dict.setId(1l);

        List<SysSql> beanList = commService.getBeanList(QueryWrapper.wrapper(dict));
        log.info("beanList:"+beanList);
    }
    @Test
    public void getPage1() {
        SysSql dict = new SysSql();
        dict.setId(1l);

        Page<SysSql> beanList = QueryWrapper.wrapper(dict).queryPage();
        log.info("beanList:"+beanList);
    }

    @Test
    public void getBeanList() {
        Dict dict = new Dict();
        dict.setA2(11);
        dict.setA3(true);

        commService.getBeanList(QueryWrapper.wrapper(dict));
    }
}
