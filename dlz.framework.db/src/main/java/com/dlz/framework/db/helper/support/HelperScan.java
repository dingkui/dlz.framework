package com.dlz.framework.db.helper.support;

import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.annotation.TableName;
import com.dlz.framework.db.enums.DbTypeEnum;
import com.dlz.framework.db.holder.BeanInfoHolder;
import com.dlz.framework.db.modal.DB;
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

    public static void scan(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            return;
        }
        final SqlHelper helper = DB.Dynamic.getSqlHelper();
        Set<Class<?>> set = scanPackage(packageName, TableName.class);
        set.stream().forEach(clazz -> initTable(clazz,helper));
    }

    public static void initTable(Class<?> clazz,SqlHelper helper) {
        TableName table = clazz.getAnnotation(TableName.class);
        if (table != null) {
            String tableName = BeanInfoHolder.getTableName(clazz);
            Set<String> columns = helper.getTableColumnNames(tableName);
            if (columns.size()==0) {
                // 创建表
                helper.createTable(tableName,clazz);
                return;
            }
            // 建立字段
            List<Field> fields = FieldReflections.getFields(clazz);
            for (Field field : fields) {
                String columnName= BeanInfoHolder.getColumnName(field);
                if(columnName.equals("")
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
