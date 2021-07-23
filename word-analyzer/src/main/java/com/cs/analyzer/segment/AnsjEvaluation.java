package com.cs.analyzer.segment;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: CS
 * @date: 2021/7/21 下午3:06
 * @description: ANSJ
 */
@Component
public class AnsjEvaluation implements WordSegment {

    @Override
    public List<String> segList(String text) {
        return BaseAnalysis.parse(text).getTerms().stream().map(Term::getName).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> segMore(String text) {
        Map<String, String> map = new HashMap<>();

        StringBuilder result = new StringBuilder();
        for (Term term : BaseAnalysis.parse(text)) {
            result.append(term.getName()).append(" ");
        }
        map.put("BaseAnalysis", result.toString());

        result.setLength(0);
        for (Term term : ToAnalysis.parse(text)) {
            result.append(term.getName()).append(" ");
        }
        map.put("ToAnalysis", result.toString());

        result.setLength(0);
        for (Term term : NlpAnalysis.parse(text)) {
            result.append(term.getName()).append(" ");
        }
        map.put("NlpAnalysis", result.toString());

        result.setLength(0);
        for (Term term : IndexAnalysis.parse(text)) {
            result.append(term.getName()).append(" ");
        }
        map.put("IndexAnalysis", result.toString());

        return map;
    }
}
