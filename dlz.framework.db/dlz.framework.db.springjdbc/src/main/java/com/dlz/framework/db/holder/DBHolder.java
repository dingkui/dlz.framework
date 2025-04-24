package com.dlz.framework.db.holder;

import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.redis.excutor.JedisExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 数据库配置信息
 */
@Slf4j
public class DBHolder {
    private static ICommService service;
    private static JedisExecutor jedis;

    public static ICommService getService() {
        if (service == null) {
            service = SpringHolder.getBean(ICommService.class);
        }
        return service;
    }

    public static JedisExecutor getJedis() {
        if (jedis == null) {
            jedis = SpringHolder.getBean(JedisExecutor.class);
        }
        return jedis;
    }

    public static long sequence(String tableName, long initSeq) {
        String key = "seq:" + tableName;
        Long seq = getJedis().incrBy(key, initSeq);
        if (seq == initSeq) {
            try {
                seq = getService().getDao().getFistColumn("select max(id) from " + tableName, Long.class) + initSeq;
                if (seq > initSeq) {
                    jedis.set(key, seq.toString());
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return seq;
    }

    public static long sequence(Class<?> beanClass, long initSeq) {
        return sequence(BeanInfoHolder.getTableName(beanClass), initSeq);
    }

    public static <R> R doDb(Function<ICommService, R> s) {
        return s.apply(getService());
    }

    public static <R> R doDao(Function<IDlzDao, R> s) {
        return s.apply(getService().getDao());
    }
}
