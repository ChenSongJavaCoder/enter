package com.cs.analyzer.segment;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:06
 * @description: Ik中文分词器
 */
@Component
public class IKEvaluation implements WordSegment {

    /**
     * 对语句进行分词
     *
     * @param text 语句
     * @return 分词后的集合
     * @throws IOException
     */
    private static List<String> segment(String text, boolean useSmart) throws IOException {
        List<String> list = new ArrayList<>();
        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, useSmart);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }

    /**
     * 对语句进行分词
     *
     * @param text 语句
     * @return 分词后的集合
     * @throws IOException
     */
    private static String segmentToListString(String text, boolean useSmart) throws IOException {
        return segment(text, useSmart).toString();
    }

    @Override
    public List<String> segList(String text) {
        try {
            return segment(text, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList(text);
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("智能切分", segmentToListString(text, true));
            map.put("细粒度切分", segmentToListString(text, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
