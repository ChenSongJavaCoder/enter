package com.cs.task.mapper;

import com.cs.common.mybatis.BaseMapper;
import com.cs.task.entity.Task;
import com.cs.task.enums.TaskState;
import org.apache.ibatis.annotations.Update;

/**
 * @author: CS
 * @date: 2021/4/26 下午5:26
 * @description:
 */
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 更新任务状态
     *
     * @param oldState 旧状态
     * @param newState 新状态
     */
    @Update("update task set task_state = #{newState}")
    int updateByConditionState(TaskState oldState, TaskState newState);
}
