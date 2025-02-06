package com.dlz.framework.db.config;

import com.dlz.comm.util.encry.TraceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {
    @Around("execution(* com.dlz.framework.db.helper.support.SqlHelper.*(..)) || execution(* com.dlz.framework.db.service.*.*(..))")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        TraceUtil.setCaller();
        try {
            return point.proceed();
        }finally {
            TraceUtil.clearCaller();
        }
    }
}