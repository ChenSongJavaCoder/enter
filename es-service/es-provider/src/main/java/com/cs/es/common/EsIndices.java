package com.cs.es.common;

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

    DEMO("demo_test", "test", "article", "文章"),
    OPERATION_LOG("operation_log", "log", "", "操作日志"),
    QUESTION("question_test", "question", "", "问题"),
    DM_HJ_CHAT_RELATION("dm_hj_chat_relation", "dm_hj_chat_relation", "", "关联微信好友"),
    ;

    private String indexName;

    private String indexType;

    private String alias;

    private String description;


}
