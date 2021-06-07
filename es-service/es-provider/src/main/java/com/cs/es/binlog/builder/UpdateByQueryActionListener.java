package com.cs.es.binlog.builder;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.stereotype.Component;

/**
 * @author: CS
 * @date: 2021/6/5 下午10:09
 * @description:
 */
@Slf4j
@Component
public class UpdateByQueryActionListener implements ActionListener<BulkByScrollResponse> {

    @Override
    public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
        log.info("UpdateByQueryAction updated : {}", bulkByScrollResponse.getUpdated());
    }

    @Override
    public void onFailure(Exception e) {
        log.error("UpdateByQueryAction failed ", e);
    }
}
