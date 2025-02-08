package com.dlz.framework.db.enums;

import com.dlz.framework.db.holder.SqlHolder;

public enum DbTypeEnum {
    MYSQL(".mysql"),
    POSTGRESQL(".postgresql"),
    ORACLE(".oracle"),
    SQLITE(".sqlite"),
    MSSQL(".sqlserver");
    private String end;

    DbTypeEnum(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    public static String dropSqlKeySufix(String key){
        DbTypeEnum[] values = DbTypeEnum.values();
        for (int i = 0; i < values.length; i++) {
            DbTypeEnum item = values[i];
            if(key.endsWith(item.getEnd())){
                if(SqlHolder.properties.getDbtype()== item){
                    return key.substring(0, key.length()- item.getEnd().length());
                }
                return null;
            }
        }
        return key;

    }
}