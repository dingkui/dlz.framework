package com.dlz.framework.cache.aspect;

import com.dlz.comm.cache.ICache;
import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.StringUtils;
import com.dlz.comm.util.ValUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * AOP 缓存操作
 * 系统方法添加注解 @CacheAnno则支持缓存
 * @author dk 2020-06-05
 */
@Slf4j
@Aspect
public class CacheAspect {
    ICache cache;

    public CacheAspect(ICache cache){
        this.cache = cache;
    }

    @Pointcut("@annotation(com.dlz.framework.cache.aspect.CacheAnno)")
    public void pointcutCache() {
    }

    @Pointcut("@annotation(com.dlz.framework.cache.aspect.CacheEvictAnno)")
    public void pointcutCacheEvict() {
    }

    private <T extends Annotation> T getApiAnnotation(JoinPoint joinPoint, Class<T> anno) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        return method.getAnnotation(anno);
    }
    private Serializable getKey(JoinPoint point,String keySet){
        if(StringUtils.isEmpty(keySet)){
            keySet="$method";
        }
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = point.getArgs();
        JSONMap paraMap = new JSONMap();
        for (int i = 0; i < parameters.length; i++) {
            paraMap.put(parameters[i].getName(),args[i]);
        }
        return Arrays.stream(keySet.split("\\+")).map(o -> getSimpleKey(o.trim(), method, paraMap)).collect(Collectors.joining(""));
    }

    private String getSimpleKey(String key,Method method,JSONMap paraMap){
        if(key.equals("$method")){
            return method.getDeclaringClass().getName()+"."+method.getName();
        }else if(key.startsWith("$if:")){
            Class<ICacheKeyIf> aClass = null;
            try {
                aClass = (Class<ICacheKeyIf>) Class.forName(key.substring(4));
                ICacheKeyIf iCacheKeyIf = aClass.newInstance();
                return iCacheKeyIf.getKey(method, paraMap);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new SystemException("key取得失败：" + method.getName(),e);
            }
        }else{
            return paraMap.getStr(key,"null");
        }
    }


    @Around("pointcutCache()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        CacheAnno cacheAnno = getApiAnnotation(point, CacheAnno.class);
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        String cacheName = cacheAnno.value();
        if(StringUtils.isEmpty(cacheName)){
            cacheName= method.getDeclaringClass().getName();
        }
        String keySet = cacheAnno.key();
        Serializable key = getKey(point,keySet);

//        System.out.println("------------------");
//        System.out.println(cacheName);
//        System.out.println(key);
//        System.out.println("------------------");

        Serializable t = cache.get(cacheName, key, method.getGenericReturnType());
        if (t != null) {
            return t;
        }

        // 执行方法取得数据
        Object result = point.proceed();
        if(result == null){
            return result;
        }
        SystemException.isTrue(!Serializable.class.isAssignableFrom(result.getClass()) , () -> "类型：" + result.getClass() + "无法缓存！");
        //数据保存进缓存
        cache.put(cacheName, key, (Serializable) result, ValUtil.getInt(cacheAnno.cacheTime()));
        return result;
    }

    /**
     * 缓存失效处理
     * @param point
     */
    @After("pointcutCacheEvict()")
    public void aroundEvict(JoinPoint point){
        CacheEvictAnno cacheAnno = getApiAnnotation(point, CacheEvictAnno.class);
        String keySet = cacheAnno.key();
        if(keySet == null){
            cache.removeAll(cacheAnno.value());
        }else{
            cache.remove(cacheAnno.value(), getKey(point, keySet));
        }
    }
}
