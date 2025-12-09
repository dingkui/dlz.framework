package com.dlz.framework.spring.iproxy;

import com.dlz.comm.exception.SystemException;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.spring.iproxy.anno.AnnoApi;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口代理创建工厂
 *
 * @param <T>
 * @author dingkui
 */
public class ApiProxyFactory<T> implements FactoryBean<T> {
    private Class<T> clas;

    public Class<T> getInterfaceClass() {
        return clas;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.clas = interfaceClass;
    }

    @Override
    public T getObject() throws Exception {
        return new InterfaceProxy().bind(clas);
    }

    @Override
    public Class<?> getObjectType() {
        return clas;
    }

    @Override
    public boolean isSingleton() {
        // 单例模式
        return true;
    }

    private static Map<String, ApiProxyHandler> cachedHandlers = new HashMap<>();

    /**
     * 接口代理实现
     *
     * @author dingkui
     */
    public class InterfaceProxy implements InvocationHandler {
        private Class<?> cls;
        private String handlerName;

        private ApiProxyHandler getHandler() {
            ApiProxyHandler proxyHandler = cachedHandlers.get(handlerName);
            if (proxyHandler == null) {
                proxyHandler = SpringHolder.getBean(handlerName + "ApiHandler");
                SystemException.notNull(proxyHandler, () -> handlerName + "ApiHandler未定义");
                cachedHandlers.put(handlerName, proxyHandler);
            }
            return proxyHandler;
        }

        @SuppressWarnings("unchecked")
        public T bind(Class<T> cls) {
            this.cls = cls;
            handlerName = cls.getAnnotation(AnnoApi.class).handler();
            return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return getHandler().done(cls, method, args);
        }
    }
}