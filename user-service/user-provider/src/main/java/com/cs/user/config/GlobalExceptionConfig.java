package com.cs.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: CS
 * @date: 2021/1/26 下午3:57
 * @description: 全局异常捕获
 * TODO 可以在此处加上异常日志记录功能
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig extends com.cs.common.exception.GlobalExceptionConfig {

}
