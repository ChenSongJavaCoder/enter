package com.cs.biz.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/4/12 上午10:13
 * @description:
 */
@Aspect
@Component
public class TestAnnAspect {


    @Pointcut(value = "@annotation(com.cs.biz.aop.TestAnn)")
    public void point() {

    }


    @Around("point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("切面方法执行前");
        Object obj = joinPoint.proceed();
        System.out.println("切面方法执行后");
        return obj;

    }
}
