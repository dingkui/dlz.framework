package com.dlz.framework.spring.scaner;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 扫描定义
 * 
 * @author dingkui
 *
 */
public interface IScaner {
	List<IScanerProcessor> scaners = new ArrayList<>();

	String getResoucePath();

	default void addScanerProcessors(IScanerProcessor scanerProcessor) {
		scaners.add(scanerProcessor);
	}

	default Collection<IScanerProcessor> getScanerProcessors() {
		return scaners;
	}

	/**
	 * 扫描处理器
	 */
	interface IScanerProcessor {
		Logger logger = org.slf4j.LoggerFactory.getLogger(IScanerProcessor.class);

		default Class<?> getBeanClass(AnnotatedBeanDefinition beanDefinition, boolean init) {
			try {
				if (init) {
					return Class.forName(beanDefinition.getBeanClassName());
				}
				return Class.forName(beanDefinition.getBeanClassName(), false, this.getClass().getClassLoader());
			} catch (ClassNotFoundException e) {
				logger.error(beanDefinition.getBeanClassName() + " " + e.getMessage(), e);
			}
			return null;
		}

		/**
		 * 是否符合处理器条件
		 * 
		 * @param beanDefinition
		 */
		boolean isCandidate(AnnotatedBeanDefinition beanDefinition);

		/**
		 * 处理相关的定义类
		 * 
		 * @param definition
		 */
		void proces(AnnotatedBeanDefinition definition);
	}
}