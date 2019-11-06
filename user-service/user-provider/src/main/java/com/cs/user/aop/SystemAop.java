package com.cs.user.aop;

import com.cs.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @ClassName: SystemInfoAop
 * @Author: CS
 * @Date: 2019/11/5 14:55
 * @Description: aop
 */
@Aspect
@Component
@Slf4j
public class SystemAop {

    /**
     * 客户端参数校验
     *
     * @param pjp
     * @param bindingResult
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.cs.*.controller..*(..)) && args(..,bindingResult)")
    public Object validateParam(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        Object retVal;
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuffer sb = new StringBuffer("参数不合法，请对照以下信息：");
            for (FieldError error : errors) {
                sb.append(error.getField()).append(error.getDefaultMessage()).append("; ");
            }
//            throw new IllegalArgumentException(sb.toString());
            return Result.illegal().message(sb.toString()).build();
        } else {
            retVal = pjp.proceed();
        }
        return retVal;
    }

//    @Pointcut("execution(* cn.geeme.hairdressing.service.controller..*(..))")
////    public void controllerMethodPointcut() {
////    }
////
////
////    @AfterReturning(value = "controllerMethodPointcut()", returning = "response")
////    public void after(JoinPoint point, Object response) {
////        SystemInfo systemInfo =  new SystemInfo();
////        if (response instanceof BaseResponse) {
////            systemInfo.setSystem_code(SystemInfo.ResponseCodeEnum.SUCCESS.getCode()).setSystem_message(SUCCESS_MESSAGE);
////            ((BaseResponse) response).setSystemInfo(systemInfo);
////        } else {
////            throw new IllegalStateException(SUCCESS_WITHOUT_BASERESPONSE);
////        }
////    }
////
////    @AfterThrowing(value = "controllerMethodPointcut()", throwing = "throwing")
////    public void error(Throwable throwing) {
////        log.info("AfterThrowing");
////    }


}
