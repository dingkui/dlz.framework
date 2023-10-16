package com.dlz.framework.util.system;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface MFunction<T, R> extends Function<T, R>, Serializable {
}