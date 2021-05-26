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

    private static final String SEPARATOR = ":";

    @Autowired
    ElasticEnvironment elasticEnvironment;

    public String key(String database, String table, String column, String value) {
        return new StringBuffer(elasticEnvironment.getProfile())
                .append(SEPARATOR).append(database)
                .append(SEPARATOR).append(table)
                .append(SEPARATOR).append(column)
                .append(SEPARATOR).append(value)
                .toString();
    }

}
