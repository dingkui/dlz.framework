package com.dlz.framework.db.helper.bean;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
    private String tableName;
    private String tableComment;
    private List<String> primaryKeys;
    private List<ColumnInfo> columnInfos;
}