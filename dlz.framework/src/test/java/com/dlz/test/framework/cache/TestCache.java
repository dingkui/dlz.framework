package com.dlz.test.framework.cache;

import com.dlz.comm.cache.CacheHolder;
import com.dlz.comm.cache.CacheUtil;
import com.dlz.comm.cache.ICache;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.service.impl.CacheRedisJsonHash;
import com.dlz.framework.redis.service.impl.CacheRedisJsonKey;
import com.dlz.framework.redis.service.impl.CacheRedisSerialHash;
import com.dlz.framework.redis.service.impl.CacheRedisSerialKey;
import com.dlz.test.framework.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestCache extends BaseTest {
	@Autowired
	ICache bean;
	@Test
	public void t1(){
		List<String> s=new ArrayList<>();
		s.add("xx");
		bean.put("aaa","xxx1111222","xxx",360000);
		bean.put("aaa","xxx22333","xddxx3333",360000);
		bean.put("aaa","xxx33444","xxddxxxxx",360000);
	}
	@Test
	public void t12(){
		ArrayList<String> s=new ArrayList<>();
		s.add("xx");
		ICache iCache = CacheHolder.get("t1", bean);
		iCache.put("t1","x1","1",-1);
		iCache.put("t1","x2",s,3600);
	}

	@Test
	public void t13(){
		ICache iCache = CacheHolder.get("t1", bean);
		ArrayList<String> data = iCache.get("t1", "x2", null);
		System.out.println(data);
	}
	@Test
	public void t14(){
		JSONMap a = new JSONMap("a", "1222222222222222222");
		bean.put("xx","xx", a.toString(),-1);
		String b=bean.get("xx","xx",String.class);
		System.out.println(b);
		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisSerialKey.class));
		CacheUtil.put("xx","xx2", a.toString(),-1);
		Serializable serializable = CacheUtil.get("xx", "xx2");
		System.out.println(CacheUtil.get("xx","xx3"));
		System.out.println(CacheUtil.get("xx","xx3",()->new BeanTest("1","4")));
		System.out.println(CacheUtil.get("xx","xx3",JSONMap.class));
		System.out.println(serializable);
		System.out.println(SpringHolder.getBean(ICache.class));
	}

	@Test
	public void t15(){
		TraceUtil.setTraceId();

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisJsonKey.class));
		System.out.println(CacheUtil.get("jsonkey","xx1",()->new BeanTest("jsonkey","xx1")));
		System.out.println(CacheUtil.get("jsonkey","xx2",()->new BeanTest("jsonkey","xx2")));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisJsonHash.class));
		System.out.println(CacheUtil.get("jsonhash","xx1",()->new BeanTest("jsonhash","xx1")));
		System.out.println(CacheUtil.get("jsonhash","xx2",()->new BeanTest("jsonhash","xx2")));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisSerialKey.class));
		System.out.println(CacheUtil.get("serialkey","xx1",()->new BeanTest("serialkey","xx1")));
		System.out.println(CacheUtil.get("serialkey","xx2",()->new BeanTest("serialkey","xx2")));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisSerialHash.class));
		System.out.println(CacheUtil.get("serialhash","xx1",()->new BeanTest("serialhash","xx1")));
		System.out.println(CacheUtil.get("serialhash","xx2",()->new BeanTest("serialhash","xx2")));

	}
	@Test
	public void t16(){
		TraceUtil.setTraceId();
		for (int i = 0; i < 100; i++) {
			int finalI = i;
			new Thread(()->{
				for (int j = 0; j < 200; j++) {
					bean.put("name"+j,"key"+ finalI,"xxxxx", 10+ValUtil.getInt(Math.random()*10000));
//					bean.put("name"+j,"key"+ finalI,"xxxxx");
				}
			}).start();
		}
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void t17(){
		TraceUtil.setTraceId();
		System.out.println("111111111111111111");
		System.out.println(bean);
		System.out.println("222222222222222222");
		System.out.println(SpringHolder.getBean(ICache.class));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisJsonKey.class));
		System.out.println(CacheUtil.get("jsonkey","xx1",()->new BeanTest("jsonkey","xx1")));
		System.out.println(CacheUtil.get("jsonkey","xx2",()->new BeanTest("jsonkey","xx2")));
		System.out.println(SpringHolder.getBeans(ICache.class));
		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisJsonHash.class));
		System.out.println(CacheUtil.get("jsonhash","xx1",()->new BeanTest("jsonhash","xx1")));
		System.out.println(CacheUtil.get("jsonhash","xx2",()->new BeanTest("jsonhash","xx2")));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisSerialKey.class));
		System.out.println(CacheUtil.get("serialkey","xx1",()->new BeanTest("serialkey","xx1")));
		System.out.println(CacheUtil.get("serialkey","xx2",()->new BeanTest("serialkey","xx2")));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisSerialHash.class));
		System.out.println(CacheUtil.get("serialhash","xx1",()->new BeanTest("serialhash","xx1")));
		System.out.println(CacheUtil.get("serialhash","xx2",()->new BeanTest("serialhash","xx2")));
		System.out.println(SpringHolder.getBeans(ICache.class));

		CacheUtil.init(SpringHolder.getBeanWithRegister(CacheRedisJsonKey.class));
		System.out.println(CacheUtil.get("jsonkey2","xx1",()->new BeanTest("jsonkey","xx1")));
		System.out.println(CacheUtil.get("jsonkey2","xx2",()->new BeanTest("jsonkey","xx2")));

		System.out.println(SpringHolder.getBeans(ICache.class));
	}

	@Test
	public void t18(){
		TraceUtil.setTraceId();
		ICache cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		cache.remove("jsonkey","xx1");
		cache.remove("jsonkey","xx2");
		cache=SpringHolder.createBean(CacheRedisJsonHash.class);
		cache.remove("jsonhash","xx1");
		cache.remove("jsonhash","xx2");
		cache=SpringHolder.createBean(CacheRedisSerialKey.class);
		cache.remove("serialkey","xx1");
		cache.remove("serialkey","xx2");
		cache=SpringHolder.createBean(CacheRedisSerialHash.class);
		cache.remove("serialhash","xx1");
		cache.remove("serialhash","xx2");
		cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		cache.remove("jsonkey2","xx1");
		cache.remove("jsonkey2","xx2");
		System.out.println(SpringHolder.getBeans(ICache.class));
	}

	@Test
	public void t19(){
		TraceUtil.setTraceId();
		ICache cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		cache.removeAll("jsonkey");
		cache=SpringHolder.createBean(CacheRedisJsonHash.class);
		cache.removeAll("jsonhash");
		cache=SpringHolder.createBean(CacheRedisSerialKey.class);
		cache.removeAll("serialkey");
		cache=SpringHolder.createBean(CacheRedisSerialHash.class);
		cache.removeAll("serialhash");
		cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		cache.removeAll("jsonkey2");
	}
	@Test
	public void t20(){
		TraceUtil.setTraceId();
		ICache cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		System.out.println("CacheRedisJsonKey.......");
		System.out.println(cache.keys("jsonkey"));
		System.out.println(cache.all("jsonkey"));
		cache=SpringHolder.createBean(CacheRedisJsonHash.class);
		System.out.println("CacheRedisJsonHash.......");
		System.out.println(cache.keys("jsonhash"));
		System.out.println(cache.all("jsonhash"));
		cache=SpringHolder.createBean(CacheRedisSerialKey.class);
		System.out.println("CacheRedisSerialKey.......");
		System.out.println(cache.keys("serialkey"));
		System.out.println(cache.all("serialkey"));
		cache=SpringHolder.createBean(CacheRedisSerialHash.class);
		System.out.println("CacheRedisSerialHash.......");
		System.out.println(cache.keys("serialhash"));
		System.out.println(cache.all("serialhash"));
		cache=SpringHolder.createBean(CacheRedisJsonKey.class);
		System.out.println("CacheRedisJsonKey.......");
		System.out.println(cache.keys("jsonkey2"));
		System.out.println(cache.all("jsonkey2"));
	}
}
