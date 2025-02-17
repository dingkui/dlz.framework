package com.dlz.framework.db.holder;

import com.dlz.comm.util.encry.TraceUtil;
import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.executor.Executor;
import com.dlz.framework.holder.SpringHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库配置信息
 */
@Slf4j
public class DBHolder {
    private static ICommService service;
    public static ICommService getService(){
        if(service==null){
            service = SpringHolder.getBean(ICommService.class);
        }
        return service;
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
