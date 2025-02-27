package com.dlz.comm.fn;

import java.io.Serializable;

/**
 * 函数式接口
 * 无参无返回 Runnable -> void run();
 * 无参有返回受检 Callable<V> -> V call() throws Exception;
 * 无参有返回 Supplier<T> -> T get();
 *
 * 有2参无返回 MConsumer<T1,T2> -> void accept(T1 t,T2 t2);
 * 有2参有返回 MFunction2<T1,T2,R> -> void accept(T1 t,T2 t2);
 * 有参无返回 Consumer<T> -> void accept(T t);
 * 有参有返回 Function<T, R> -> R apply(T t);
 *
 * 比较 Comparator<T> -> int compare(T o1, T o2);
 *
 * @author dlz
 */
@FunctionalInterface
public interface DlzFn<T, R> extends Serializable {
    R apply(T t);
}