package com.dlz.test.framework.db.config;

import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.comm.util.system.FieldReflections;
import com.dlz.framework.db.modal.items.JdbcItem;
import com.dlz.framework.db.modal.items.SqlItem;
import com.dlz.framework.db.modal.para.AWrapper;
import com.dlz.framework.db.modal.para.ParaJdbc;
import com.dlz.framework.db.modal.para.ParaMap;
import com.dlz.framework.db.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"com.dlz.framework", "com.dlz.test.framework.db.config"})
@Slf4j
public class SpingDbBaseTest {
    @Before
    public void before(){
        if(TraceUtil.getTraceid()==null){
            TraceUtil.setTraceId();
        }
    }
    private String clearSql(String sql){
        return sql.replaceAll("[\\s]+"," ").trim();
    }
    public void showSql(ParaMap paraMap, String fn, String re) {
        log.debug("-------------------  "+fn+"  -------------------");
        log.debug(ValUtil.toStr(paraMap));
        JdbcItem jdbcSql = paraMap.jdbcSql();
//        SqlUtil.dealParm(paraMap,1,true);
        SqlItem sqlItem = paraMap.getSqlItem();
        log.debug(sqlItem.toString());
        log.debug(paraMap.getPara().toString());
        String runSqlByJdbc = SqlUtil.getRunSqlByJdbc(jdbcSql.sql, jdbcSql.paras).trim();
        if(re==null){
            log.info(runSqlByJdbc);
        }else if(clearSql(re).equals(clearSql(runSqlByJdbc))){
            log.info("sucess:"+runSqlByJdbc);
        }else{
            log.error("error:"+runSqlByJdbc);
            log.error("target:"+re);
            assert false;
        }
    }
    public void showSql(ParaJdbc paraMap, String fn, String re) {
        log.debug("-------------------  "+fn+"  -------------------");
        log.debug(ValUtil.toStr(paraMap));
        JdbcItem jdbcSql = paraMap.jdbcSql();
//        SqlUtil.dealParm(paraMap,1,true);
        String runSqlByJdbc = SqlUtil.getRunSqlByJdbc(jdbcSql.sql, jdbcSql.paras).trim();
        if(re==null){
            log.info(runSqlByJdbc);
        }else if(clearSql(re).equals(clearSql(runSqlByJdbc))){
            log.info("sucess:"+runSqlByJdbc);
        }else{
            log.error("error:"+runSqlByJdbc);
            log.error("target:"+re);
            assert false;
        }
    }
    public void showSql(ParaMap paraMap, String fn) {
        showSql(paraMap, fn, null);
    }
    public void showSql(AWrapper wrapper, String fn, String re) {
        wrapper.jdbcSql();
        ParaMap paraMap = FieldReflections.getValue(wrapper, "pm",false);
        showSql(paraMap, fn, re);
    }
    public void showSql(AWrapper wrapper, String fn) {
        showSql(wrapper, fn, null);
    }
}