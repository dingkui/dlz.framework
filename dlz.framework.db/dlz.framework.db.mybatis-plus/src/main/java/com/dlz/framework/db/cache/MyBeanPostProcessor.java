package com.dlz.framework.db.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dlz.framework.holder.SpringHolder;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;

public class MyBeanPostProcessor implements BeanPostProcessor {
    private Map<Class<?>, Object[]> hashtable = new Hashtable<>();
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass() == MapperFactoryBean.class) {
            Class mapperInterface = ((MapperFactoryBean) bean).getMapperInterface();
            if (!BaseMapper.class.isAssignableFrom(mapperInterface)) {
                return bean;
            }
            ParameterizedType pt = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
            Type actualTypeArgument = pt.getActualTypeArguments()[0];
            if(actualTypeArgument instanceof Class){
                Class<?> aClass = (Class<?>) actualTypeArgument;
                hashtable.put(aClass, new Object[]{mapperInterface, null});
            }
        }
        return bean;
    }


    public <T> BaseMapper<T> getMapper(Class<T> clazz) {
        Object[] obj = hashtable.get(clazz);
        if (obj == null) {
            throw new RuntimeException("未找到[" + clazz.getName() + "]对应的Dao");
        }
        BaseMapper<T> BaseMapper = (BaseMapper) obj[1];
        if (BaseMapper == null) {
            BaseMapper = (BaseMapper) SpringHolder.getBean(((Class) obj[0]));
            obj[1] = BaseMapper;
        }
        return BaseMapper;
    }
}
