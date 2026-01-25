package com.dlz.framework.db.modal;

import com.dlz.framework.db.modal.para.TableDelete;
import com.dlz.framework.db.modal.para.TableInsert;
import com.dlz.framework.db.modal.para.TableQuery;
import com.dlz.framework.db.modal.para.TableUpdate;

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
