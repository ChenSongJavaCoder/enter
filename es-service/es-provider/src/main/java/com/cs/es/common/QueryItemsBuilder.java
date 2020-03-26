package com.cs.es.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName: QueryItemsBuilder
 * @Author: CS
 * @Date: 2019/10/8 17:39
 * @Description:
 */
@Slf4j
@Component
public class QueryItemsBuilder {

    private final static String RESERVE_PROPERTY = "type";

    /**
     * 构造BoolQueryBuilder对象
     *
     * @param queryItems 支持term、range、query_string、prefix、match、nested类型，格式如：
     *                   [
     *                   {"id":{"gt":"1","lt":"4"}, "type":"range"},
     *                   {"firstClass":{"gt":"0","lt":"5"}, "type":"range"},
     *                   {"yhPrice":"{"value":"100.93", "path":"productSpecifications"}", "type":"nested"},
     *                   {"title":"护理", "type":"term"},
     *                   {"keyWords":"正品", "type":"term"},
     *                   {"detail":"正品洗发水", "type":"match"},
     *                   {"shortTitle":"阿道夫", "type":"prefix"},
     *                   {"code":"2019032", "type":"query_string"},
     *                   {"brandId":{"gt":"1","lt":"5"}, "type":"range"}
     *                   ]
     * @return
     */
    public BoolQueryBuilder boolQueryBuilder(List<Map<String, Object>> queryItems) {
        BoolQueryBuilder mustQueryBuilder = QueryBuilders.boolQuery();
        mustQueryBuilder.must(QueryBuilders.matchAllQuery());
        queryItems.stream().forEach(item -> {
            String type = (String) item.get(RESERVE_PROPERTY);
            if (StringUtils.isEmpty(type)) {
                log.error("搜索参数中未取到type值");
                return;
            }
            Set<Map.Entry<String, Object>> entrySet = item.entrySet();
            switch (type) {
                case QueryType.TERM: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY) && Objects.nonNull(entry.getValue())) {
                            if (entry.getValue() instanceof List) {
                                mustQueryBuilder.filter(QueryBuilders.termsQuery(key, (List) entry.getValue()));
                            } else {
                                mustQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
                            }
                        }
                    });
                    break;
                }
                case QueryType.RANGE: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            Map value = (LinkedHashMap) entry.getValue();
                            mustQueryBuilder.filter(QueryBuilders.rangeQuery(key).gte(value.get("gt")).lte(value.get("lt")));
                        }
                    });
                    break;
                }
                case QueryType.QUERY_STRING: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            String value = ((String) entry.getValue());
                            if (!StringUtils.isEmpty(value)) {
                                mustQueryBuilder.filter(QueryBuilders.queryStringQuery("*" + value.toLowerCase() + "*").field(key));
                            }
                        }
                    });
                    break;
                }
                case QueryType.MATCH: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            String value = ((String) entry.getValue());
                            if (!StringUtils.isEmpty(value)) {
                                mustQueryBuilder.filter(QueryBuilders.matchQuery(key, value));
                            }
                        }
                    });
                    break;
                }
                case QueryType.NESTED: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            Map value = (LinkedHashMap) entry.getValue();
                            if (!StringUtils.isEmpty(value)) {
                                String path = (String) value.get("path");
                                String field = (String) value.get("field");
                                String model = (String) value.get("model");
                                BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
                                queryBuilder.must(QueryBuilders.matchAllQuery());
                                if (QueryType.TERM.equals(model)) {
                                    queryBuilder.filter(QueryBuilders.termQuery(path.concat(".").concat(field), (String) value.get("value")));
                                } else if (QueryType.RANGE.equals(model)) {
                                    String minValue = (String) value.get("minValue");
                                    String maxValue = (String) value.get("maxValue");
                                    queryBuilder.filter(QueryBuilders.rangeQuery(path.concat(".").concat(field)).gte(minValue).lte(maxValue));
                                }
                                mustQueryBuilder.filter(QueryBuilders.nestedQuery(path, queryBuilder, ScoreMode.None));
                            }
                        }
                    });
                    break;
                }
                case QueryType.PREFIX: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            String value = ((String) entry.getValue());
                            if (!StringUtils.isEmpty(value)) {
                                mustQueryBuilder.filter(QueryBuilders.prefixQuery(key, value.toLowerCase()));
                            }
                        }
                    });
                    break;
                }
                case QueryType.FUZZ: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            String value = (String) entry.getValue();
                            mustQueryBuilder.filter(QueryBuilders.fuzzyQuery(key, value).fuzziness(Fuzziness.AUTO));
                        }
                    });
                    break;
                }
                case QueryType.WILDCARD: {
                    entrySet.forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equals(RESERVE_PROPERTY)) {
                            String value = (String) entry.getValue();
                            mustQueryBuilder.filter(QueryBuilders.wildcardQuery(key, "*" + value + "*"));
                        }
                    });
                    break;
                }

                default:
                    log.error("传入 \"type\":\"{}\" 类型不正确", type);
            }
        });

        return mustQueryBuilder;
    }

}
