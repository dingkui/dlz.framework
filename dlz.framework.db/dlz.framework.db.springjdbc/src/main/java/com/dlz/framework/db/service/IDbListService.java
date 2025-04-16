package com.dlz.framework.db.service;

import com.dlz.comm.util.system.ConvertUtil;
import com.dlz.framework.db.inf.IOperatorQuery;
import com.dlz.framework.db.modal.result.Page;
import com.dlz.framework.db.modal.result.ResultMap;
import com.dlz.framework.db.modal.para.WrapperQuery;

import java.util.List;

/**
 * 从数据库中取得单条map类型数据：{adEnddate=2015-04-08 13:47:12.0}
 * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
 * paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
 *
 * @return
 * @throws Exception
 */
public interface IDbListService extends IDbBaseService{
    /**
     * 从数据库中取得map类型列表如：[{AD_ENDDATE=2015-04-08 13:47:12.0}]
     * sql语句，可以带参数如：select AD_ENDDATE from JOB_AD t where ad_id=#{ad_id}
     *
     * @param paraMap ：Map<String,Object> m=new HashMap<String,Object>();m.put("ad_id", "47");
     * @return
     * @throws Exception
     */
    default List<ResultMap> getMapList(IOperatorQuery paraMap) {
        return doDb(paraMap, jdbcSql -> getDao().getList(jdbcSql.sql, jdbcSql.paras));
    }
    default <T> List<T> getBeanList(WrapperQuery<T> wrapper) {
        return doDb(wrapper, jdbcSql -> ConvertUtil.convertList(getDao().getList(jdbcSql.sql, jdbcSql.paras),wrapper.getBeanClass()));
    }


    default <T> List<T> getBeanList(IOperatorQuery paraMap, Class<T> t) {
        return ConvertUtil.convertList(getMapList(paraMap), t);
    }
    default <T> List<T> getBeanList(T bean){
        final WrapperQuery<T> wrapper = WrapperQuery.wrapper(bean);
        return getBeanList(wrapper,wrapper.getBeanClass());
    }
    default int getCnt(IOperatorQuery paraMap) {
        return doCnt(paraMap, jdbcSql -> getDao().getFistColumn(jdbcSql.sql, Integer.class, jdbcSql.paras));
    }
    /**
     * 取得分页数据
     *
     * @return
     * @throws Exception
     */
    default Page<ResultMap> getPage(IOperatorQuery paraMap) {
        Page<ResultMap> page = (Page<ResultMap>)paraMap.getPage();
        if (page == null) {
            page = Page.build();
            paraMap.setPage(page);
        }
        if(page.getCurrent()<=0){
            page.setCurrent(1);
        }
        return page.doPage(() -> getCnt(paraMap), () -> getMapList(paraMap));
    }

    default <T> Page<T> getPage(IOperatorQuery paraMap, Class<T> t) {
        Page<T> page = (Page<T>)paraMap.getPage();
        if (page == null) {
            page = Page.build();
            paraMap.setPage(page);
        }
        if(page.getCurrent()<=0){
            page.setCurrent(1);
        }
        return page.doPage(() -> getCnt(paraMap), () -> getBeanList(paraMap, t));
    }

}
