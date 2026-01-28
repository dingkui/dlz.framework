package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.wrapper.TableDelete;
import com.dlz.framework.db.modal.wrapper.TableInsert;
import com.dlz.framework.db.modal.wrapper.TableQuery;
import com.dlz.framework.db.modal.wrapper.TableUpdate;

public class DbTable {
    public TableInsert insert(String tableName) {
        return new TableInsert(tableName);
    }

    public TableDelete delete(String tableName) {
        return new TableDelete(tableName);
    }

    public TableUpdate update(String tableName) {
        return new TableUpdate(tableName);
    }

    public TableQuery select(String tableName) {
        return new TableQuery(tableName);
    }
}