package com.dlz.framework.db.helper.bean;

import lombok.Data;

@Data
public class ColumnInfo {
    private String columnName;
    private String columnType;
    private String columnComment;
    private Class<?> javaType;
}