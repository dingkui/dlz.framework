package com.dlz.comm.util.system;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类.
 * <p>
 * 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 *
 * @author calvin
 */
@Slf4j
public class Reflections {
    private static final String CGLIB_CLASS_SEPARATOR = "$$";


    /**
     * 取得泛型参数类型
     * Map<String, HashMap<Integer,Double>> test = new HashMap<String, HashMap<Integer,Double>>(){};
     * getActualType(test.getClass(),0);  ->class java.lang.String
     * getActualType(test.getClass(),1);	->java.util.HashMap<java.lang.Integer, java.lang.Double>
     * getActualType(test.getClass(),1,0);	->class java.lang.Integer
     * getActualType(test.getClass(),1,1);	->class java.lang.Double
     *
     * @param type   需要判断的class
     * @param indexs 泛型序号，有多级时，传递多个参数
     */
    public static Type getActualType(Type type, int... indexs) {
        int length = indexs.length;

        //无序号，默认取得第一个泛型参数或者本身
        if (length == 0) {
            if (type instanceof Class) {
                return type;
            } else if (type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
        }

        //取得泛型参数
        ParameterizedType genericSuperclass;
        if (type instanceof Class) {
            Type genericSuper = ((Class) type).getGenericSuperclass();
            if (genericSuper instanceof ParameterizedType) {
                genericSuperclass = (ParameterizedType) genericSuper;
            } else {
                throw new SystemException(type + "无泛型参数");
            }
        } else if (type instanceof ParameterizedType) {
            genericSuperclass = (ParameterizedType) type;
        } else {
            throw new SystemException(type + "未识别泛型参数");
        }

        Type actualType = genericSuperclass.getActualTypeArguments()[indexs[0]];
        if (length == 1) {
            return actualType;
        }
        int[] subIndex = new int[length - 1];
        System.arraycopy(indexs, 1, subIndex, 0, length - 1);
        return getActualType(actualType, subIndex);
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    public static Class<?> getTargetClass(Class clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }
    public static Class<?> getTargetClass(Object instance) {
        return getTargetClass(instance.getClass());
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, final Class<?>[] parameterTypes, final Object[] args) {
        try {
            return clazz.getConstructor(parameterTypes).newInstance(args);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    public static Class<?>[] mkParameterTypes(Object ... para) {
        Class<?>[] aClass = new Class[para.length];
        for (int i = 0; i < para.length; i++) {
            aClass[i] = para[i]==null?null:getTargetClass(para[i].getClass());
        }
        return aClass;
    }

    public static <T> T newInstance(Class<T> classz,Object ... para) {
        return newInstance(classz,mkParameterTypes(para),para);
    }
    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }



}
