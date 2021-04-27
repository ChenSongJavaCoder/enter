package com.cs.task.entity;

import com.cs.common.mybatis.BaseEntity;
import lombok.Data;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:06
 * @description: 子任务
 */
@Data
public class SubTask extends BaseEntity {

    private Long taskId;

    private String keyword;

    private String request;

    private String response;

}
