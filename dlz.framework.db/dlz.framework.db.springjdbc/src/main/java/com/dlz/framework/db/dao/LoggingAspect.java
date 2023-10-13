package com.dlz.framework.db.dao;

import com.dlz.comm.util.encry.TraceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(value = "dlz.db.showCaller", havingValue = "true")
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