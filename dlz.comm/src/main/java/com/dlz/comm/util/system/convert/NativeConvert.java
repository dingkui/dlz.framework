package com.dlz.comm.util.system.convert;

import com.dlz.comm.util.ValUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NativeConvert implements IConvert {
    @Override
    public <T> T convert(Object input, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (tClass.isAssignableFrom(input.getClass())) {
            return (T) input;
        } else if (tClass == String.class) {
            return (T) ValUtil.toStr(input);
        } else if (tClass == Integer.class) {
            return (T) ValUtil.toInt(input);
        } else if (tClass == Long.class) {
            return (T) ValUtil.toLong(input);
        } else if (tClass == Date.class) {
            return (T) ValUtil.toDate(input);
        } else if (tClass == BigDecimal.class) {
            return (T) ValUtil.toBigDecimal(input);
        } else if (tClass == Float.class) {
            return (T) ValUtil.toFloat(input);
        } else if (tClass == Double.class) {
            return (T) ValUtil.toDouble(input);
        } else if (tClass == Boolean.class) {
            return (T) ValUtil.toBoolean(input);
        } else if (tClass.isArray()) {
            return (T) ValUtil.toArray(input);
        }
        return null;
    }

    @Override
    public <S, T> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        if (input.isEmpty()) {
            return new ArrayList<>();
        }
        if (tClass.isAssignableFrom(sClass)) {
            return input.stream().map(s -> (T) s).collect(Collectors.toList());
        } else if (tClass == String.class) {
            return input.stream().map(s -> (T) ValUtil.toStr(s)).collect(Collectors.toList());
        } else if (tClass == Integer.class) {
            return input.stream().map(s -> (T) ValUtil.toStr(s)).collect(Collectors.toList());
        } else if (tClass == Long.class) {
            return input.stream().map(s -> (T) ValUtil.toLong(s)).collect(Collectors.toList());
        } else if (tClass == Date.class) {
            return input.stream().map(s -> (T) ValUtil.toDate(s)).collect(Collectors.toList());
        } else if (tClass == BigDecimal.class) {
            return input.stream().map(s -> (T) ValUtil.toBigDecimal(s)).collect(Collectors.toList());
        } else if (tClass == Float.class) {
            return input.stream().map(s -> (T) ValUtil.toFloat(s)).collect(Collectors.toList());
        } else if (tClass == Double.class) {
            return input.stream().map(s -> (T) ValUtil.toDouble(s)).collect(Collectors.toList());
        } else if (tClass == Boolean.class) {
            return input.stream().map(s -> (T) ValUtil.toBoolean(s)).collect(Collectors.toList());
        }
        return null;
    }
}
