package com.dlz.framework.spring.iproxy;

import com.dlz.comm.util.StringUtils;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.spring.iproxy.anno.AnnoApi;
import com.dlz.framework.spring.scaner.IScaner;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * 自定义扫描 只扫描接口和抽象类，并且注解了AnnoApi的类 命名为： I*Api.java
 *
 * @author dk
 */
public class ApiScaner implements IScaner {
    private String resoucePath = "classpath*:**/I*Api.class";

    @Override
    public String getResoucePath() {
        return resoucePath;
    }
    public ApiScaner(String resoucePath) {
		this.resoucePath = "classpath*:"+resoucePath;
		addScanerProcessors(new IScanerProcessor() {
			BeanDefinitionRegistry registry = SpringHolder.getBeanDefinitionRegistry();

			@Override
			public boolean isCandidate(AnnotatedBeanDefinition beanDefinition) {
				Class<?> forName = getBeanClass(beanDefinition, false);
				AnnoApi annotation = forName.getAnnotation(AnnoApi.class);
				return beanDefinition.getMetadata().isAbstract() && annotation != null;
			}

			@Override
			public void proces(AnnotatedBeanDefinition definition) {
				Class<?> forName = getBeanClass(definition, false);
				if (forName == null) {
					return;
				}
				AnnoApi annotation = forName.getAnnotation(AnnoApi.class);
				String beanName = annotation.value();
				if ("".equals(beanName)) {
					beanName = StringUtils.getBeanId(definition.getBeanClassName());
				}
				definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
				definition.setBeanClassName(ApiProxyFactory.class.getName());
				registry.registerBeanDefinition(beanName, definition);
			}
		});

    }
}