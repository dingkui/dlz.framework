package com.dlz.framework.db.helper.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.support.AsyncUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class HelperScan {
    @Autowired
    HelperProperties properties;
    @Autowired
    @Lazy
    AsyncUtils asyncUtils;

    private static Set<Class<?>> scanPackage(String basePackage, Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    @PostConstruct
    private void scan() {
        if (!properties.autoUpdate || StringUtils.isEmpty(properties.packageName)) {
            return;
        }
        Set<Class<?>> set = scanPackage(properties.packageName, TableName.class);
        for (Class<?> clazz : set) {
            if (properties.type == DbTypeEnum.SQLITE) {
                asyncUtils.initTableSync(clazz);
            } else {
                asyncUtils.initTable(clazz);
            }
        }
    }
}
