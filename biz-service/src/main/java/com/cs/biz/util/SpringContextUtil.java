package com.cs.biz.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/4/16 4:43 下午
 * @Description:
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext context;

    /**
     * not thread safe
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * this method not thread safe
     *
     * @param className className pattern classFullName[:beanId]
     * @param <T>       target Class Type
     * @return spring bean
     */
    public static <T> T getBean(final String className) throws ClassNotFoundException {
        String clazzName = className;
        String beanId = null;
        int beanIdIndex = className.indexOf(":");
        if (beanIdIndex > 0) {
            clazzName = className.substring(0, beanIdIndex);
            beanId = className.substring(beanIdIndex + 1);
        }
        Class<?> clazz = Class.forName(clazzName);

        if (StringUtils.isBlank(beanId)) {
            return (T) context.getBean(clazz);
        } else {
            return (T) context.getBean(beanId, clazz);
        }
    }

    /**
     * this method not thread safe
     *
     * @param className   className pattern classFullName[:beanId]
     * @param classLoader target classLoader
     * @param <T>         target Class Type
     * @return spring bean
     */
    public static <T> T getBean(final String className, ClassLoader classLoader) throws ClassNotFoundException {
        String clazzName = className;
        String beanId = null;
        int beanIdIndex = className.indexOf(":");
        if (beanIdIndex > 0) {
            clazzName = className.substring(0, beanIdIndex);
            beanId = className.substring(beanIdIndex + 1);
        }
        Class<?> clazz = classLoader.loadClass(clazzName);

        if (StringUtils.isBlank(beanId)) {
            return (T) context.getBean(clazz);
        } else {
            return (T) context.getBean(beanId, clazz);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(context)) {
            context = applicationContext;
        }
    }
}
