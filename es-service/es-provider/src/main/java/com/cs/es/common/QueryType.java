package com.cs.es.common;

/**
 * @ClassName: QueryType
 * @Author: CS
 * @Date: 2019/10/8 17:39
 * @Description:
 */
public interface QueryType {

    String TERM = "term";

    String RANGE = "range";

    String PREFIX = "prefix";

    String MATCH = "match";

    String NESTED = "nested";

    String QUERY_STRING = "query_string";

    String FUZZ = "fuzz_query";

    String WILDCARD = "wildcard";

}
