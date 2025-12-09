package com.dlz.comm.util.system;

import com.dlz.comm.util.ValUtil;
import com.dlz.comm.util.system.convert.BeanConvert;
import com.dlz.comm.util.system.convert.IConvert;
import com.dlz.comm.util.system.convert.MapConvert;
import com.dlz.comm.util.system.convert.NativeConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConvertUtil {
    private static final IConvert[] CONVERTS =new IConvert[]{new NativeConvert(),new MapConvert(),new BeanConvert()};

    public static <T> T convert(Object input, Class<T> clazz) {
        return convert(input, clazz, null);
    }

    public static <T> T convert(Object input, Class<T> tClass, Consumer<T> fn) {
        if (input == null) {
            return null;
        }
        for (int i = 0; i < CONVERTS.length; i++) {
            T convert = CONVERTS[i].convert(input, tClass, fn);
            if(convert!=null){
                return convert;
            }
        }
        return null;
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param input
     * @param tClass
     * @return
     */
    public static <T, S> List<T> convertList(List<S> input, Class<S> sClass, Class<T> tClass, Consumer<T> fn) {
        if (ValUtil.isEmpty(input)) {
            return new ArrayList<>();
        }
        for (int i = 0; i < CONVERTS.length; i++) {
            List<T> convert = CONVERTS[i].convertList(input,sClass, tClass, fn);
            if(convert!=null){
                return convert;
            }
        }
        return null;
    }

    /**
     * Map转Bean
     *
     * @param <T>
     * @param input
     * @param tClass
     * @return
     */
    public static <T> List<T> convertList(List<?> input, Class<T> tClass, Consumer<T> fn) {
        if (ValUtil.isEmpty(input)) {
            return new ArrayList<>();
        }
        return input.stream().map(o -> convert(o, tClass, fn)).collect(Collectors.toList());
    }

    public static <T, T1> List<T> convertList(List<T1> input, Class<T> clazz) {
        return convertList(input, clazz, null);
    }
}
