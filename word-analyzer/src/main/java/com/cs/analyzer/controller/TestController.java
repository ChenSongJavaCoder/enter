package com.cs.analyzer.controller;

import com.cs.analyzer.PropertiesLoader;
import com.cs.analyzer.entity.Keyword;
import com.cs.analyzer.util.SensitiveWordUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: CS
 * @Date: 2020/2/14 8:54 下午
 * @Description:
 */
@RestController
public class TestController {

    @Autowired
    PropertiesLoader propertiesLoader;


    @GetMapping("/test/{text}")
    public String test(@PathVariable String text) {
        Map<String, Keyword> map = propertiesLoader.getKeywordMap();

        try {
            Set<String> set = SensitiveWordUtil.getSensitiveWord(text);
            List<Keyword> keywords = Lists.newArrayList();
            for (String s : set) {
                keywords.add(map.get(s));
            }
            return keywords.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }


}
