package com.cs.analyzer.segment;

import com.google.common.collect.Lists;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:06
 * @description: Jcseg中文分词器
 */
@Component
public class JcsegEvaluation implements WordSegment {

    private static final SegmenterConfig CONFIG = new SegmenterConfig();
    private static final ADictionary DIC = DictionaryFactory.createDefaultDictionary(CONFIG);

    static {
        CONFIG.setLoadCJKSyn(false);
        CONFIG.setLoadCJKPinyin(false);
    }

    @Override
    public List<String> segList(String text) {
        List<String> list = Lists.newArrayList();
        try {
            ISegment seg = ISegment.Type.SIMPLE.factory.create(CONFIG, DIC);
            seg.reset(new StringReader(text));
            IWord word = null;
            while ((word = seg.next()) != null) {
                list.add(word.getValue());
            }
        } catch (Exception ex) {
            list.add(text);
            return list;
        }
        return list;
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();
        Stream.of(ISegment.Type.values()).forEach(e -> {
            map.put(e.name, segText(text, e.index));
        });
        return map;
    }

    private String segText(String text, int type) {
        StringBuilder result = new StringBuilder();
        try {
            ISegment seg = ISegment.Type.fromIndex(type).factory.create(CONFIG, DIC);
            seg.reset(new StringReader(text));
            IWord word = null;
            while ((word = seg.next()) != null) {
                result.append(word.getValue()).append(" ");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result.toString();
    }
}
