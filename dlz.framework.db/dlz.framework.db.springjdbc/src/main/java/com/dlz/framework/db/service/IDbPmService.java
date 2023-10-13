package com.dlz.framework.db.service;

import com.dlz.comm.exception.DbException;
import com.dlz.comm.util.JacksonUtil;
import com.dlz.framework.db.convertor.ConvertUtil;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.db.modal.Page;
import com.dlz.framework.db.modal.ResultMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbPmService extends IBaseDbService {
    /**
     * 更新或插入数据库
     * sql语句，可以带参数如：update JOB_AD set AD_text=#{adText} where ad_id in (${ad_id})
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    int excuteSql(BaseParaMap paraMap);

    /**
     * 从数据库中取得map类型列表如：[{AD_ENDDATE=2015-04-08 13:47:12.0}]
     * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    List<ResultMap> getMapList(BaseParaMap paraMap);

    int getCnt(BaseParaMap paraMap);

    /**
     * 从数据库中取得单个字段数据
     */
    default Object getColum(BaseParaMap paraMap) {
        List<ResultMap> list = getMapList(paraMap);
        if (list.size() == 0) {
            return null;
        } else if (list.size() > 1) {
            throw new DbException("查询结果为多条", 1004);
        } else {
            return ConvertUtil.getFistClumn(list.get(0));
        }
    }

    default String getStr(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), String.class);
    }

    default BigDecimal getBigDecimal(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), BigDecimal.class);
    }

    default Float getFloat(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), Float.class);
    }

    default Integer getInt(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), Integer.class);
    }

    default Long getLong(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), Long.class);
    }

    default Double getDouble(BaseParaMap paraMap) {
        return ConvertUtil.getColum(getMapList(paraMap), Double.class);
    }

    default List<String> getStrList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), String.class);
    }

    default List<BigDecimal> getBigDecimalList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), BigDecimal.class);
    }

    default List<Float> getFloatList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), Float.class);
    }

    default List<Integer> getIntList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), Integer.class);
    }

    default List<Long> getLongList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), Long.class);
    }

    default List<Double> getDoubleList(BaseParaMap paraMap) {
        return ConvertUtil.getColumList(getMapList(paraMap), Double.class);
    }

    /**
     * 从数据库中取得集合
     */
    default ResultMap getMap(BaseParaMap paraMap) {
        return getMap(paraMap, true);
    }

    default ResultMap getMap(BaseParaMap paraMap, boolean throwEx) {
        List<ResultMap> list = getMapList(paraMap);
        if (list.size() == 0) {
            return null;
        } else if (list.size() > 1 && throwEx) {
            throw new DbException("查询结果为多条", 1004);
        } else {
            return list.get(0);
        }
    }

    default <T> T getBean(BaseParaMap paraMap, Class<T> t, boolean throwEx) {
        try {
            return JacksonUtil.coverObj(getMap(paraMap, throwEx), t);
        } catch (Exception e) {
            if (e instanceof DbException) {
                throw e;
            }
            throw new DbException(e.getMessage(), 1005, e);
        }
    }

    default <T> T getBean(BaseParaMap paraMap, Class<T> t) {
        return getBean(paraMap, t, true);
    }

    default <T> List<T> getBeanList(BaseParaMap paraMap, Class<T> t) {
        List<ResultMap> list = getMapList(paraMap);
        List<T> l = new ArrayList<T>();
        for (ResultMap r : list) {
            try {
                l.add(JacksonUtil.coverObj(r, t));
            } catch (Exception e) {
                throw new DbException(e.getMessage(), 1005, e);
            }
        }
        return l;
    }

    /**
     * 取得分页数据
     *
     * @return
     * @throws Exception
     */
    default Page<ResultMap> getPage(BaseParaMap paraMap) {
        return getPage(paraMap, ResultMap.class);
    }

    default <T> Page<T> getPage(BaseParaMap paraMap, Class<T> t) {
        Page cache = paraMap.getCacheItem().getCache("page", paraMap);
        if (cache != null) {
            return cache;
        }

        Page<T> page = paraMap.getPage();
        //是否需要查询列表（需要统计条数并且条数是0的情况不查询，直接返回空列表）
        boolean needList = true;

        page.setCount(getCnt(paraMap));
        if (page.getCount() == 0) {
            needList = false;
        }

        if (needList) {
            if (t == ResultMap.class) {
                page.setData((List<T>) getMapList(paraMap));
            } else {
                page.setData(getBeanList(paraMap, t));
            }
        } else {
            page.setData(new ArrayList<T>());
        }
        paraMap.getCacheItem().saveCache(page);

        return page;
    }
}
