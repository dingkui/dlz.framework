package com.dlz.test.framework.springframework.iproxy;

import com.dlz.framework.spring.iproxy.ApiProxyHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class TestApiHandler extends ApiProxyHandler {
	@Override
	public Object done(Class<?> clazz,Method method, Object[] args) throws Exception {
		return method.getName()+getParaAsMap(method, args);
	}
}
