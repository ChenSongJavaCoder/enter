package com.cs.es.binlog.builder;

import com.cs.es.binlog.config.ColumnRelatedMapping;
import com.cs.es.binlog.config.EntityRelatedMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * @author keosn
 * @date 2019/4/3 17:07
 */
@Component
public class RelatedValueGetter {

    @Autowired
    RowValuesCacheGetter rowValuesCacheGetter;

    public Serializable getValue(ColumnRelatedMapping relatedColumnMapping, Object o) {
        return rowValuesCacheGetter.getValue(relatedColumnMapping.getRelatedDatabase(),
                relatedColumnMapping.getRelatedTable(),
                relatedColumnMapping.getRelatedTargetColumn(),
                o,
                relatedColumnMapping.getTargetColumn());

    }

    public Map<String, Serializable> getRowValue(EntityRelatedMapping entityRelatedMapping, Object relatedValue) {
        return rowValuesCacheGetter.getRowValue(entityRelatedMapping.getDatabase(),
                entityRelatedMapping.getTableName(),
                entityRelatedMapping.getRelatedValueColumn(),
                relatedValue);
    }
}
