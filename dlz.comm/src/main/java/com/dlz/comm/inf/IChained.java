package com.dlz.comm.inf;

public interface IChained<T extends IChained> {
    T me();
}
