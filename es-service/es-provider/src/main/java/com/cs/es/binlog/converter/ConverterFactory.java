package com.cs.es.binlog.converter;

import com.cs.es.binlog.bean.SourceTargetPair;
import com.cs.es.binlog.scan.ClassScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: CS
 * @date: 2021/5/10 上午10:48
 * @description: 转换器工厂
 */
@Component
public class ConverterFactory {

    private static final Map<SourceTargetPair, Converter> converters = new ConcurrentHashMap<>(16);

    @Autowired
    ClassScanner classScanner;

    /**
     * 注册converter
     *
     * @param converter
     */
    public void register(Converter converter) {
        converters.put(converter.getKey(), converter);
    }

    public Converter getConverter(SourceTargetPair sourceTargetPair) {
        return converters.get(sourceTargetPair);
    }


    /**
     * 可以实现自动注入转化器^_^
     *
     * @param <T>
     */
    @PostConstruct
    public <T extends Converter> void init() {
        Set<Class<?>> classes = cn.hutool.core.lang.ClassScanner.scanPackageBySuper("com.cs.es.binlog.converter.impl", Converter.class);
//        Set<Class> classes = classScanner.scan("com.cs.es.binlog.converter.impl");
        classes.forEach(aClass -> {
            try {
                Object instance = aClass.newInstance();
                if (instance instanceof Converter) {
                    register((T) instance);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
