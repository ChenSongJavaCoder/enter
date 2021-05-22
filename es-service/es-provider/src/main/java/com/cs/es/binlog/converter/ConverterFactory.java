package com.cs.es.binlog.converter;

import cn.hutool.core.util.ClassUtil;
import com.cs.es.binlog.bean.SourceTargetPair;
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

    /**
     * converter容器
     */
    private static final Map<SourceTargetPair, Converter> converters = new ConcurrentHashMap<>(16);

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
        // 获取当前类的包
        Package pkg = this.getClass().getPackage();
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(pkg.getName(), Converter.class);
        classes.forEach(aClass -> {
            try {
                Object instance = aClass.newInstance();
                register((T) instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
