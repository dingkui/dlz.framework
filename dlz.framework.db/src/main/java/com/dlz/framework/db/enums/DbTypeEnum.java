package com.dlz.framework.db.enums;

public enum DbTypeEnum {
    MYSQL("_mysql"),
    POSTGRESQL("_postgresql"),
    ORACLE("_oracle"),
    DM8("_dm8"),
    SQLITE("_sqlite"),
    MSSQL("_sqlserver");
    private String end;

    DbTypeEnum(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }
}