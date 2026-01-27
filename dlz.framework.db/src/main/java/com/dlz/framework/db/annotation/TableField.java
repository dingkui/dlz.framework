package com.dlz.framework.db.annotation;

import java.lang.annotation.*;
/**
 * 表字段标识
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableField {
    /**
     * 数据库字段值
     */
    String value() default "";

    /**
     * 是否为数据库表字段
     * <p>
     * 默认 true 存在，false 不存在
     */
    boolean exist() default true;

    /**
     * 是否进行 select 查询
     * <p>
     * 大字段可设置为 false 不加入 select 查询范围
     */
    boolean select() default true;
}