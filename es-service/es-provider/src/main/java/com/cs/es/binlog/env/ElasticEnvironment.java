package com.cs.es.binlog.env;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author keosn
 * @date 2019/4/3 17:49
 */
@Data
@Component
public class ElasticEnvironment {

    @Value("${spring.profiles.active:local}")
    private String profile;


}
