package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.inf.ISqlPara;

import java.util.function.Function;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbBaseService {
    IDlzDao getDao();

    default <T> T doCnt(ISqlPara paraMap, Function<JdbcItem, T> executor) {
        try {
            return executor.apply(paraMap.jdbcCnt());
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage() + " sqkKey:" + paraMap.getSqlItem().getSqlKey(), 1005, e);
        }
    }
    default <T> T doDb(ISqlPara paraMap, Function<JdbcItem, T> executor) {
        try {
            return executor.apply(paraMap.jdbcSql());
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage() + " sqkKey:" + paraMap.getSqlItem().getSqlKey(), 1005, e);
        }
    }
}
