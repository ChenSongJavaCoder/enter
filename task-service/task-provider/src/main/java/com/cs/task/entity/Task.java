package com.cs.task.entity;

import com.cs.common.mybatis.BaseEntity;
import com.cs.task.enums.TaskState;
import com.cs.task.enums.TaskType;
import lombok.Data;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:06
 * @description: 主任务
 */
@Data
public class Task extends BaseEntity {

    private TaskType taskType;

    private TaskState taskState;

    private String exceptionMsg;

}
