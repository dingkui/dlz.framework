package com.dlz.test.framework.db.cases.paramap;

import com.dlz.comm.util.VAL;
import com.dlz.comm.util.ValUtil;
import com.dlz.framework.db.SqlUtil;
import com.dlz.framework.db.modal.map.ParaMapBase;
import com.dlz.framework.db.modal.items.SqlItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParaMapTestUtil {
    public static void showSql(ParaMapBase paraMap,String fn) {
        log.debug("-------------------  "+fn+"  -------------------");
        log.debug(ValUtil.getStr(paraMap));
        VAL<String, Object[]> jdbcSql = paraMap.jdbcSql();
//        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        log.debug(SqlUtil.getRunSqlByJdbc(jdbcSql.v1, jdbcSql.v2));
    }
}