package com.dlz.framework.holder;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.encry.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.util.Map;

/**
 * 以静态变量保存Spring ApplicationContext,可在任意代码中取出ApplicaitonContext.
 *
 * @author dk
 */
@Slf4j
public class SpringHolder {
    private static Logger logger = LoggerFactory.getLogger(SpringHolder.class);
    private static ConfigurableListableBeanFactory beanFactory;

    public static void init() {
        init("*");
    }

    public static void init(String xml) {
        if (beanFactory == null) {
            beanFactory = new ClassPathXmlApplicationContext("classpath*:spring_cfg/" + xml + ".xml").getBeanFactory();
        }
    }

    public static void init(ConfigurableListableBeanFactory applicationContext) {
        if (beanFactory == null) {
            beanFactory = applicationContext;
        }
    }

    public static BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return (BeanDefinitionRegistry) beanFactory;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
//	/**
//	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
//	 */
//	public void setApplicationContext(ApplicationContext applicationContext) {
//		if(beanFactory==null){
//			beanFactory =((ConfigurableApplicationContext)applicationContext).getBeanFactory();
//		}
//	}org.apache.ibatis.binding.MapperProxy@6c8a68c1

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        if (beanFactory.containsBean(name))
            return (T) beanFactory.getBean(name);
        return null;
    }


    /**
     * 是否包含某个bean
     *
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        try {
            return beanFactory.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            logger.warn("NoSuchBeanDefinition" + clazz.getName());
            return null;
        }
    }


    /**
     * 从静态变量ApplicationContext中取得Bean, 如果取不到则注册一个单例bean.
     */
    public static <T> T getBeanWithRegister(Class<T> clazz) {
        checkApplicationContext();
        try {
            if (!beanFactory.getBeansOfType(clazz).isEmpty()) {
                return beanFactory.getBean(clazz);
            }
            return registerBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            logger.warn("NoSuchBeanDefinition" + clazz.getName());
            return null;
        }
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        checkApplicationContext();
        return beanFactory.getBeansOfType(clazz);
    }

    private static void checkApplicationContext() {
        if (beanFactory == null)
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextUtil");
    }

    /**
     * 注册bean
     *
     * @param beanId    所注册bean的id
     * @param singleton 是否单例
     * @param clazz     clazz
     */
    @SuppressWarnings("unchecked")
    public static <T> T registerBean(String beanId, boolean singleton, Class<T> clazz) {
        String toRegeistBeanId = beanId;
        if (singleton) {
            if (!beanFactory.getBeansOfType(clazz).isEmpty()) {
                return beanFactory.getBean(clazz);
            }
        } else {
            while (beanFactory.containsBean(toRegeistBeanId)) {
                toRegeistBeanId = beanId + "_" + TraceUtil.generateShortUuid();
            }
        }
//    	ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) application;
//		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();

        // get the BeanDefinitionBuilder
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        AbstractBeanDefinition definition = beanDefinitionBuilder.getRawBeanDefinition();
//        // get the BeanDefinition
//        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        // register the bean
        getBeanDefinitionRegistry().registerBeanDefinition(toRegeistBeanId, definition);
        return (T) beanFactory.getBean(toRegeistBeanId);
    }

    /**
     * 创建一个bean并执行autowired
     *
     * @param clazz clazz
     */
    public static <T> T createBean(Class<T> clazz) {
        try {
            T bean = clazz.newInstance();
            AutowiredAnnotationBeanPostProcessor autowiredProcessor = new AutowiredAnnotationBeanPostProcessor();
            autowiredProcessor.setBeanFactory(beanFactory);
            autowiredProcessor.processInjection(bean);
            return bean;
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }

    /**
     * 注册bean
     *
     * @param className bean的className，
     *                  三种获取方式：1、直接书写，如：com.mvc.entity.User
     *                  2、User.class.getName
     *                  3.user.getClass().getName()
     * @throws ClassNotFoundException
     */
    public static void registerBean(String className) throws ClassNotFoundException {
        registerBean(Class.forName(className));
    }

    /**
     * 注册bean
     *
     * @param clazz class
     */
    public static <T> T registerBean(Class<T> clazz) {
        return registerBean(StringUtils.getBeanId(clazz.getName()), true, clazz);
    }

    /**
     * 注册bean
     *
     * @param clazz     class
     * @param singleton 是否多例
     */
    public static <T> T registerBean(Class<T> clazz, boolean singleton) {
        return registerBean(StringUtils.getBeanId(clazz.getName()), singleton, clazz);
    }

    /**
     * 移除bean
     *
     * @param beanId bean的id
     */
    public static void unregisterBean(String beanId) {
//    	ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) application;
//		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();
        getBeanDefinitionRegistry().removeBeanDefinition(beanId);
    }



    public static ApplicationContext getApplicationContext() {
        if (beanFactory != null && beanFactory instanceof ApplicationContext) {
            return (ApplicationContext) beanFactory;
        }
        return null;
    }
    /**
     * 发布事件
     *
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event) {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext == null) {
            return;
        }
        try {
            applicationContext.publishEvent(event);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    public static Resource getResource(String resourceLocation) {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getResource(resourceLocation);
    }
}
