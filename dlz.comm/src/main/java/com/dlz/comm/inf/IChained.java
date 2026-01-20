package com.dlz.comm.inf;

/**
 * 链式调用接口
 * 
 * 用于实现链式调用模式，允许方法链式调用返回当前对象
 * 
 * @param <T> 实现此接口的类型
 * 
 * @author dingkui
 * @since 2023
 */
public interface IChained<T extends IChained> {
    /**
     * 返回当前对象，用于链式调用
     * 
     * @return 当前对象
     */
    T me();
}