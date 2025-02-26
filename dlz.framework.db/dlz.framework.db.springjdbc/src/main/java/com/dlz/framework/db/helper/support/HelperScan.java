package com.dlz.framework.db.helper.support;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.helper.util.DbNameUtil;
import com.dlz.framework.db.holder.SqlHolder;
import com.dlz.comm.util.system.FieldReflections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class HelperScan {
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

    public static void scan(String packageName,SqlHelper helper) {
        if (StringUtils.isEmpty(packageName)) {
            return;
        }
        Set<Class<?>> set = scanPackage(packageName, TableName.class);
        boolean initSync = SqlHolder.properties.getDbtype() == DbTypeEnum.SQLITE;
        if (initSync) {
            set.stream().forEach(clazz -> initTable(clazz,helper));
        } else {
            set.parallelStream().forEach(clazz -> initTable(clazz,helper));
        }
    }

    public static void initTable(Class<?> clazz,SqlHelper helper) {
        TableName table = clazz.getAnnotation(TableName.class);
        if (table != null) {
            String tableName = DbNameUtil.getDbTableName(clazz);
            Set<String> columns = helper.getTableColumnNames(tableName);
            if (columns.size()==0) {
                // 创建表
                helper.createTable(tableName,clazz);
                return;
            }
            // 建立字段
            List<Field> fields = FieldReflections.getFields(clazz);
            for (Field field : fields) {
                String columnName=DbNameUtil.getDbClumnName(field);
                if(columnName==null
                        || columns.contains(columnName)
                        || columnName.equalsIgnoreCase("id")
                ){
                    continue;
                }
                // 创建字段
                helper.createColumn(tableName,columnName,field);
            }
        }
    }
}
