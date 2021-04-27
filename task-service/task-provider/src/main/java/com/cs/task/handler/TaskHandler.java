package com.cs.task.handler;

import com.cs.task.entity.SubTask;
import com.cs.task.entity.Task;
import com.cs.task.enums.TaskState;
import com.cs.task.enums.TaskType;

import java.util.List;

/**
 * @author: CS
 * @date: 2021/4/26 下午6:13
 * @description: 处理任务表
 */
public interface TaskHandler {

    /**
     * 找出待执行的目标任务
     *
     * @param taskType
     * @param taskState
     * @return
     */
    List<Task> findAllTasks(TaskType taskType, TaskState taskState);

    /**
     * 更新任务状态
     *
     * @param task     任务
     * @param oldState 旧状态
     * @param newState 新状态
     * @return 影响行数
     */
    int updateByConditionState(Task task, TaskState oldState, TaskState newState);

    /**
     * 执行单个任务
     *
     * @param task
     */
    void executeSingleTask(Task task, BizHandler bizHandler);

    /**
     * 执行子任务
     *
     * @param taskId
     */
    void executeSubTasks(Long taskId, BizHandler bizHandler);

    /**
     * 更新子任务的业务数据
     *
     * @param subTask 子任务数据
     * @return 影响行数
     */
    int updateSubTaskBizData(SubTask subTask);

    /**
     * 异步化需要有此操作
     *
     * @param task
     * @param bizHandler
     * @return
     */
    void executeSingleTaskBiz(Task task, BizHandler bizHandler);

    /**
     * 执行子任务
     *
     * @param taskId
     */
    void executeSubTasksBiz(Long taskId, BizHandler bizHandler);

    /**
     * 满足条件更新
     *
     * @param subTask
     * @return
     */
    default boolean condition(SubTask subTask) {
        return true;
    }

}
