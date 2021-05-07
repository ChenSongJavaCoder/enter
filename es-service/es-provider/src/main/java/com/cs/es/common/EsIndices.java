package com.cs.es.common;

import com.cs.es.model.DemoTest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: EsIndices
 * @Author: CS
 * @Date: 2019/9/21 16:18
 * @Description: indices
 */
@Getter
@AllArgsConstructor
public enum EsIndices {

    DEMO("demo_test", "test", "article", "文章", DemoTest.class),
    BM_SPFL("bm_spfl", "bm_spfl", "", "商品编码分类", DemoTest.class),
    OPERATION_LOG("operation_log", "log", "", "操作日志", Object.class),
    QUESTION("question_test", "question", "", "问题", Object.class),
    DM_HJ_CHAT_RELATION("dm_hj_chat_relation", "dm_hj_chat_relation", "", "关联微信好友", Object.class),
    ;

    private String indexName;

    private String indexType;

    private String alias;

    private String description;

    private Class<?> clazz;


}
