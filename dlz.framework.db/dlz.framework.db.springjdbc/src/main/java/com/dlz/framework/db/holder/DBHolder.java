package com.dlz.framework.db.holder;

import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.executor.Executor;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.excutor.JedisExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库配置信息
 */
@Slf4j
public class DBHolder {
    private static ICommService service;
    private static JedisExecutor jedis;
    public static ICommService getService(){
        if(service==null){
            service = SpringHolder.getBean(ICommService.class);
        }
        return service;
    }
    public static JedisExecutor getJedis(){
        if(jedis==null){
            jedis = SpringHolder.getBean(JedisExecutor.class);
        }
        return jedis;
    }

    public static long sequence(String tableName) {
        String key = "seq:" + tableName;
        Long seq = getJedis().incr(key);
        if(seq==1){
            try {
                seq = getService().getDao().getFistColumn("select max(id) from " + tableName, Long.class)+1;
                if(seq>1){
                    jedis.set(key, seq.toString());
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return seq;
    }
    public static <R> R doDb(Executor<ICommService, R> s) {
        ICommService service = getService();
        if (SqlHolder.properties.getLog().isShowCaller()) {
            TraceUtil.setCaller(3);
            try {
                return s.excute(service);
            } finally {
                TraceUtil.clearCaller();
            }
        } else {
            return s.excute(service);
        }
    }
    public static <R> R doDao(Executor<IDlzDao, R> s) {
        IDlzDao service = getService().getDao();
        if (SqlHolder.properties.getLog().isShowCaller()) {
            TraceUtil.setCaller(3);
            try {
                return s.excute(service);
            } finally {
                TraceUtil.clearCaller();
            }
        } else {
            return s.excute(service);
        }
    }
}
