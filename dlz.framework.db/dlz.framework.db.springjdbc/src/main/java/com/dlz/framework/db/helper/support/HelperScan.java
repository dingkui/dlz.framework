package com.dlz.framework.db.helper.support;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.holder.SqlHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class HelperScan {
    final String packageName;
    final AsyncUtils asyncUtils;

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
        if (StringUtils.isEmpty(packageName)) {
            return;
        }
        Set<Class<?>> set = scanPackage(packageName, TableName.class);
        boolean initSync = SqlHolder.properties.getDbtype() == DbTypeEnum.SQLITE;
        for (Class<?> clazz : set) {
            if (initSync) {
                asyncUtils.initTableSync(clazz);
            } else {
                asyncUtils.initTable(clazz);
            }
        }
    }
}
