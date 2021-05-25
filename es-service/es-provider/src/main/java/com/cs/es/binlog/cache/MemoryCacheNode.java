package com.cs.es.binlog.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author keosn
 * @date 2019/4/13 11:49
 */
@Data
@AllArgsConstructor
public class MemoryCacheNode {

    private String key;

    private Map<String, Serializable> value;

    private long expireTime;

}
