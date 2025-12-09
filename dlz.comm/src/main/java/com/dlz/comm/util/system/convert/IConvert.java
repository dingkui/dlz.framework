package com.dlz.comm.util.system.convert;

import java.util.List;
import java.util.function.Consumer;

public interface IConvert {
    <T> T convert(Object input, Class<T> tClass, Consumer<T> fn);
    <S,T> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn);
}
