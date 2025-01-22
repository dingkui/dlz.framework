package com.dlz.framework.db.holder;

import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.holder.SpringHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库配置信息
 */
@Slf4j
public class ServiceHolder {
    private static ICommService service;
    public static ICommService getService(){
        if(service==null){
            service = SpringHolder.getBean(ICommService.class);
        }
        return service;
    }
}
