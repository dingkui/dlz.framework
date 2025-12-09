package com.dlz.test.framework.db.cases.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dlz.framework.db.service.ICommPlusService;
import com.dlz.framework.db.service.ICommService;
import com.dlz.test.framework.db.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


/**
 * 单元测试基类,资源初始化<br>
 * @author dk
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommPlusServiceTest {
	@Autowired
	public  ICommService cs;
	@Autowired
	public  ICommPlusService commPlusService;
	
//	@BeforeClass
//	public static void before() throws Exception {
//		SpringHolder.init();
//		cs=SpringHolder.getBean("commServiceImpl");
//		commPlusService=SpringHolder.getBean(ICommPlusService.class);
//	}
	@Test
	public void test01(){
		Map<String, Object> map = cs.getMap("select * from t_b_dict",false);
		List<Dict> map2 = commPlusService.list(new Dict());
		System.out.println(map);
		System.out.println(map2);
	}
	@Test
	public void test1(){
		List<Dict> list = commPlusService.list(new Dict());
		log.debug("re： {}",list);
	}

	@Test
	public void test2(){
		LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Dict::getDictStatus,"1");
		List<Dict> list = commPlusService.list(queryWrapper, Dict.class);
		log.debug("re： {}",list);
	}
}
