package com.dlz.framework.db.cache;

import com.dlz.comm.cache.MemoryCahe;
import com.dlz.comm.json.JSONMap;
import com.dlz.framework.cache.service.AbstractCache;
import com.dlz.framework.db.dao.IDlzDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据库表字段缓存
 */
public class TableInfoCache extends AbstractCache<String, JSONMap> {

    @Autowired
    IDlzDao dao;

    public TableInfoCache() {
        super(new MemoryCahe());
        dbOperator = new DbOperator() {
            protected JSONMap getFromDb(String tableName) {
                return new JSONMap(dao.getTableColumsInfo(tableName));
            }
        };
    }
}
