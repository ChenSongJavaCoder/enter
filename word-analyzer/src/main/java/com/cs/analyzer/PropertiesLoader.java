package com.cs.analyzer;

import com.cs.analyzer.entity.Keyword;
import com.cs.analyzer.mapper.KeywordMapper;
import com.cs.analyzer.util.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: CS
 * @Date: 2020/2/14 8:26 下午
 * @Description:
 */
@Slf4j
@Component
public class PropertiesLoader {

    private Map<String, Keyword> sensitiveWordMap;

    @Autowired
    KeywordMapper keywordMapper;

    @PostConstruct
    private void init() {
        // 初始化读取数据库中的关键词
        List<Keyword> list = keywordMapper.selectAll();
        sensitiveWordMap = list.stream().collect(Collectors.toMap(p -> p.getKeyword(), p -> p));

        // 加载到词库
        Dictionary.initial(DefaultConfig.getInstance());
        Dictionary dictionary = Dictionary.getSingleton();
        dictionary.addWords(sensitiveWordMap.keySet());

        // 加载到工具类
        SensitiveWordUtil.init(sensitiveWordMap.keySet());
    }


    public Map<String, Keyword> getKeywordMap() {
        return sensitiveWordMap;

    }
}
