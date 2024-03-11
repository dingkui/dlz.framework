package com.dlz.framework.cache.service;

import com.dlz.comm.cache.CacheHolder;
import com.dlz.comm.cache.ICache;
import com.dlz.comm.util.ExceptionUtils;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.holder.SpringHolder;
import com.dlz.framework.util.system.Reflections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;

import java.io.Serializable;

/**
 * 缓存实现
 *
 * @author dk
 */
@Slf4j
@DependsOn("dlzCache")
public abstract class AbstractCache<KEY extends Serializable, T extends Serializable> {

    public class DbOperator {
        protected T getFromDb(KEY key) {
            return null;
        }

        protected void saveToDb(KEY key, T t) {
        }
    }

    /**
     * 取值操作器
     */
    protected DbOperator dbOperator = new DbOperator() {
        public Object getFromDb(Object o) {
            return null;
        }

        public void saveToDb(Object o, Object o2) {
        }
    };
    /**
     * 缓存时间
     */
    protected int timeToLiveSeconds = -1;
    /**
     * 缓存名称
     */
    private String cacheName;
    /**
     * 缓存对象
     */
    private Class<T> resultClass = (Class<T>) Reflections.getActualType(getClass(), 1);
//            (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    /**
     * 构造函数
     *
     * @param cache             缓存实现
     * @param cacheName         缓存名称
     * @param timeToLiveSeconds 缓存时间：秒
     */
    public AbstractCache(ICache cache, String cacheName, int timeToLiveSeconds) {
        if (StringUtils.isEmpty(cacheName)) {
            cacheName = this.getClass().getSimpleName();
        }
        this.cacheName = cacheName.toLowerCase();

        if (cache == null) {
            cache = SpringHolder.getBean("dlzCache");
        }

        if (timeToLiveSeconds > 0) {
            this.timeToLiveSeconds = timeToLiveSeconds;
        }
        if (dbOperator == null) {
            this.dbOperator = new DbOperator() {
                public Object getFromDb(Object o) {
                    return null;
                }

                public void saveToDb(Object o, Object o2) {
                }
            };
        }

        CacheHolder.add(this.cacheName, cache);
    }

    public AbstractCache(String cacheName, ICache cache) {
        this(cache, cacheName, 0);
    }

    public AbstractCache(String cacheName) {
        this(null, cacheName, 0);
    }

    public AbstractCache(ICache cache) {
        this(cache, null, 0);
    }

    public AbstractCache() {
        this(null, null, 0);
    }

    protected ICache getCache() {
        return CacheHolder.get(this.cacheName);
    }

    public String toString() {
        ICache cache = getCache();
        return this.cacheName + "：" + (cache == null ? "未初始化" : cache.toString());
    }

    private T readDb(KEY key) {
        T t = null;
        try {
            t = dbOperator.getFromDb(key);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace("从数据库加载缓存失败：" + getCacheName() + "." + key,e));
        }
        return t;
    }

    /**
     * 保存缓存到数据库
     */
    public void saveCache2Db(KEY key, T value) {
        if (value == null) {
            if (key == null) {
                log.error("缓存保存失败：key或value必须设置一个！" + getCacheName());
                return;
            }
            value = getFromCache(key);
        }else{
            //将数据更新到缓存
            put(key,value);
        }
        if (value != null) {
            try {
                dbOperator.saveToDb(key, value);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace("保存缓存到数据库失败：" + getCacheName(),e));
            }
        }
    }

    /**
     * 缓存中读取对象，取不到则从数据库中取得
     */
    public T get(KEY key) {
        T t = null;
        try {
            if (key != null) {
                t = getFromCache(key);
                if (t == null) {
                    t = readDb(key);
                    if (t != null) {
                        put(key, t, timeToLiveSeconds);
                    }
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return t;
    }

    /**
     * 缓存中读取对象，取不到则返回空
     */
    public T getFromCache(KEY key) {
        return getCache().get(cacheName, key, resultClass);
    }

    /**
     * 设置缓存
     */
    public void put(KEY key, T value) {
        put(key, value, timeToLiveSeconds);
    }

    /**
     * 设置缓存
     *
     * @param exp 有效期：秒
     */
    public void put(KEY key, T value, int exp) {
        try {
            getCache().put(cacheName, key, value, exp);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 删除缓存
     */
    public void remove(KEY key) {
        try {
            getCache().remove(cacheName, key);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 清空缓存
     */
    public void clear() {
        getCache().removeAll(cacheName);
    }

    /**
     * 取得当前缓存的名称
     */
    public String getCacheName() {
        return this.cacheName;
    }

    /**
     * 从数据库中重新加载
     */
    public T reload(KEY key) {
        T t = readDb(key);
        if (t == null) {
            log.error("缓存刷新失败：cacheName=" + getCacheName() + " key=" + key);
        } else {
            put(key, t, timeToLiveSeconds);
        }
        return t;
    }
}