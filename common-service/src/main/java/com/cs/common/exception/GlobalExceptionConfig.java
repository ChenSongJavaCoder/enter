package com.cs.common.exception;

import com.cs.common.bean.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author: CS
 * @date: 2021/1/26 下午3:57
 * @description: 全局异常捕获
 * TODO 可以在此处加上异常日志记录功能
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 对于参数异常情况的处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> illegalArgsExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        StringBuffer sb = new StringBuffer("参数不合法，请对照以下信息：");
        for (FieldError error : errors) {
            sb.append(error.getField()).append(error.getDefaultMessage()).append("; ");
        }
        log.error("参数异常捕获 {}", sb.toString());
        return Result.illegal().message(sb.toString()).build();
    }

    /**
     * 作为异常解决响应前端提示信息
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseCodeException.class)
    public Result<Object> baseCodeExceptionHandler(BaseCodeException e) {
        log.info("通用平台码异常 {}", e.getDesc());
        return Result.failure(e).build();
    }

    /**
     * 规范后照理说不应该出现，兜底解决
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result<Object> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常异常处理", e.getMessage());
        return Result.failure().message(e.getMessage()).build();
    }

    /**
     * 照理说不应该出现，兜底解决
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Object> exceptionHandler(Exception e) {
        log.error("全局异常处理点，value 定向捕获特定异常 {}", e.getCause().getMessage());
        return Result.failure().message(e.getCause().getMessage()).build();
    }


}
