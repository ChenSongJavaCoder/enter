package com.cs.common.log;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/8/11 上午10:26
 * @description: 日志切面，日志链路追踪
 * 包括：
 * 1、web接口请求，响应
 * 2、MQ消息消费
 * 3、自定义接口切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private static void log() {

    }

    /**
     * 所有controller包下面的controller做切面
     */
    @Pointcut("execution(public * com.cs.*.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 所有BizLog注解标注的数据
     */
    @Pointcut("@annotation(BizLog)")
    public void bizLog() {
    }

    /**
     * 可指定到对应消费消息的接口
     */
    @Pointcut("this(com.cs.common.log.LogAspect)")
    public void mqLog() {
    }

    @Before(value = "webLog()")
    public void requestLog(JoinPoint joinPoint) {
        // logback-spring.xml中配置traceId. 可根据traceId追踪此次线程打印的所有日志
        // 疑问？ 异步打印日志这时候traceId还能生效吗
        MDC.put("traceId", IdUtil.fastSimpleUUID());

    }

    @Around("webLog()")
    public Object around(JoinPoint joinPoint) {
        log();
        return null;
    }

    @AfterReturning(value = "webLog()", returning = "response")
    public void after(JoinPoint joinPoint, Object response) {
        log();
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void exceptionLog(JoinPoint joinPoint, Throwable e) {
        String message = ExceptionUtil.stacktraceToString(e);
        log();
    }

    /**
     * 日志对象
     */
    @Data
    public static class LogInfo {
        private String traceId;
        private String logType;
        private String requestUrl;
        private String httpMethod;
        private String httpParams;
        private String requestIp;
        private String classMethod;
        private String type;
        private String userId;
    }


}
