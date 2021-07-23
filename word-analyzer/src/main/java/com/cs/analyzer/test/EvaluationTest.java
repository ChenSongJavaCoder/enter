package com.cs.analyzer.test;

import cn.hutool.json.JSONUtil;
import com.cs.analyzer.segment.WordSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:36
 * @description:
 */
@Component
public class EvaluationTest {

    @Autowired
    List<WordSegment> wordSegments;

    List<String> demoKeyword = Arrays.asList(
            "ABB变频器"
            , "207豆豉凌鱼。"
            , "技术服务费"
            , "95号车用汽油(ⅥA)"
            , "12.00R20全钢轮胎"
            , "机动车交通事故责任强制保险服务"
    );


    @PostConstruct
    public void test() {
        demoKeyword.stream().forEach(text -> {
            System.out.println(text);
            wordSegments.stream().forEach(e -> {
                System.out.println(e.getClass().getSimpleName() + "\n" + JSONUtil.toJsonPrettyStr(e.segMore(text)));
            });
        });

    }
}
