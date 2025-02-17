package com.dlz.framework.db.inf;

public interface IChained<T extends IChained> {
    T me();
}
