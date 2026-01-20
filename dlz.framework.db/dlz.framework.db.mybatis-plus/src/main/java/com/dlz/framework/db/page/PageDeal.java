package com.dlz.framework.db.page;

import com.dlz.framework.db.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dlz.comm.util.ValUtil;

/**
 * @author dk
 * date 2020-03-10
 * 翻页处理器
 * @param <T> 查询数据对应的bean class
 * @param <O> 输出列表的bean class
 */
@TableName
public abstract class PageDeal<T, O> {
    private Class<O> oClass;

    public PageDeal(Class<O> oClass){
        this.oClass=oClass;
    }

    /**
     * wrapper 中设置自定义查询条件
     * @param wrapper
     */
    public void setWrapper(QueryWrapper<T> wrapper){

    }

    /**
     * 对列表中的数据自定义修改
     * @param bean
     */
    public void transBean(O bean){

    }

    /**
     * 根据数据库查询结果构建目标结果数据
     * @param bean
     */
    public final O getOutBean(T bean){
        try{
            O out= null;
            if(bean.getClass() == oClass){
                out = (O) bean;
            }else{
                out = ValUtil.toObj(bean,oClass);
            }
            transBean(out);
            return out;
        }catch (Exception e){
            throw new RuntimeException("数据转换有误");
        }
    }
}