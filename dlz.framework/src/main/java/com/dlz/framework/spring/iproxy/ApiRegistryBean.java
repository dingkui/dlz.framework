package com.dlz.framework.spring.iproxy;

import com.dlz.framework.spring.iproxy.anno.AnnoApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 自定义扫描注册bean
 * @author dingkui
 *
 */
public class ApiRegistryBean implements BeanDefinitionRegistryPostProcessor{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    	IntefaceDefinitionScanner beanScanner = new IntefaceDefinitionScanner(beanDefinitionRegistry);
    	Set<BeanDefinition> beanDefinitions = beanScanner.findCandidateComponents();
    	for (BeanDefinition definition : beanDefinitions) {
    		String beanName = definition.getBeanClassName();
    		int lastIndexOf = beanName.lastIndexOf('.');
    		beanName=beanName.substring(lastIndexOf+1,lastIndexOf+2).toLowerCase()+beanName.substring(lastIndexOf+2);
	        definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
	        definition.setBeanClassName(ApiProxyFactory.class.getName());
	        beanDefinitionRegistry.registerBeanDefinition(beanName, definition);
    	}
    }
    /**
     * 自定义扫描
     * 只扫描接口和抽象类，并且注解了AnnoInterfaceProxy的类  命名为： I*Api.java
     * @author dk
     */
    private static String CLASSPATH="classpath*:com/dlz/**/I*Api.class";
    private class IntefaceDefinitionScanner extends ClassPathBeanDefinitionScanner{
        public IntefaceDefinitionScanner(BeanDefinitionRegistry registry) {
            super(registry,false);
            this.addIncludeFilter(new AnnotationTypeFilter(AnnoApi.class, true, true));
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return beanDefinition.getMetadata().isAbstract() && beanDefinition.getMetadata().hasAnnotation(AnnoApi.class.getName());
        }
        
    	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    	private MetadataReaderFactory metadataReaderFactory =
    			new CachingMetadataReaderFactory(this.resourcePatternResolver);
        
        public Set<BeanDefinition> findCandidateComponents() {
    		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
    		try {
    			Resource[] resources = resourcePatternResolver.getResources(CLASSPATH);
    			for (Resource resource : resources) {
    				System.out.println(resource.getFile().getAbsolutePath());
    				if (resource.isReadable()) {
    					try {
    						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
    						if (isCandidateComponent(metadataReader)) {
    							ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
    							sbd.setResource(resource);
    							sbd.setSource(resource);
    							candidates.add(sbd);
    						}
    					}
    					catch (Throwable ex) {
    						throw new BeanDefinitionStoreException(
    								"Failed to read candidate component class: " + resource, ex);
    					}
    				}
    			}
    		}
    		catch (IOException ex) {
    			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
    		}
    		return candidates;
    	}
    }
}