package com.dlz.test.framework.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dlz.framework.db.service.ICommPlusService;
import com.dlz.framework.db.util.DbUtil;
import com.dlz.test.framework.db.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WrapperTest {
    @Autowired
    ICommPlusService ps;
    @Test
    public void testDbUtil() {
        LambdaQueryWrapper<Dict> wrapper = DbUtil.mkWrapper(Dict.class).lambda();
        wrapper.eq(Dict::getDictStatus,1);
        System.out.println(wrapper.getSqlSegment());
        //System.out.println(wrapper.getSql());
    }

    @Test
    public void testEntityClass() {
        Dict bean = new Dict();
        System.out.println(DbUtil.getEntityClass(new QueryWrapper<>(bean)));
        System.out.println(DbUtil.getEntityClass(new LambdaQueryWrapper<>(Dict.class)));
        System.out.println(DbUtil.getEntityClass(DbUtil.mkWrapper(Dict.class)));
        System.out.println(DbUtil.getEntityClass(DbUtil.lbWrapper(Dict.class)));
        System.out.println(DbUtil.getEntityClass(new LambdaQueryWrapper<Dict>(){}));
        System.out.println(DbUtil.getEntityClass(new QueryWrapper<Dict>(){}));
    }
    @Test
    public void testDbUtil2() {
        System.out.println(DbUtil.getEntityClass(new QueryWrapper<Dict>()));
    }
    @Test
    public void testService() {
        Dict one = ps.getOne(DbUtil.lbWrapper(Dict.class).eq(Dict::getDictStatus,"1"));
        System.out.println(one);
    }
}