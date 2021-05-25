package com.cs.es.binlog.cache;

import com.cs.es.binlog.env.ElasticEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author keosn
 * @date 2019/4/3 17:41
 */
@Component
public class RowValuesKeyProvider {

    private static final String SPARATOR = ":";
    @Autowired
    ElasticEnvironment elasticEnvironment;

    public String key(String database, String table, String column, String value) {
        return new StringBuffer(elasticEnvironment.getProfile())
                .append(SPARATOR).append(database)
                .append(SPARATOR).append(table)
                .append(SPARATOR).append(column)
                .append(SPARATOR).append(value)
                .toString();
    }

}
