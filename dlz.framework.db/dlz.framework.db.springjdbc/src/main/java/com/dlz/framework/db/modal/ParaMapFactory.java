package com.dlz.framework.db.modal;

public class ParaMapFactory {
    public static ParaMap para(String sql) {
        return new ParaMap(sql);
    }
    public static ParaMap para(String sql,Page page) {
        return new ParaMap(sql,page);
    }

    public static InsertParaMap insert(String tableName) {
        return new InsertParaMap(tableName);
    }
    public static DeleteParaMap delete(String tableName) {
        return new DeleteParaMap(tableName);
    }

    public static UpdateParaMap update(String tableName) {
        return new UpdateParaMap(tableName);
    }
    public static SearchParaMap select(String colums,String tableName) {
        return new SearchParaMap(colums,tableName);
    }
    public static SearchParaMap select(String tableName) {
        return new SearchParaMap(tableName);
    }
}
