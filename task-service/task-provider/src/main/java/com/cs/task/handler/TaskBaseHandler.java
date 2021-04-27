package com.cs.task.handler;


import cn.hutool.json.JSONUtil;
import com.cs.task.entity.SubTask;
import com.cs.task.entity.Task;
import com.cs.task.enums.TaskState;
import com.cs.task.enums.TaskType;
import com.cs.task.mapper.SubTaskMapper;
import com.cs.task.mapper.TaskMapper;
import com.cs.task.pojo.AdaptorBaseRequest;
import com.cs.task.pojo.AdaptorBaseResponse;
import com.cs.task.pojo.AdaptorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: CS
 * @date: 2021/4/26 下午9:33
 * @description: todo 异常情况未捕获处理
 */
@Slf4j
public abstract class TaskBaseHandler implements TaskHandler {

    @Autowired
    ObjectMapper objectMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private SubTaskMapper subTaskMapper;

    @Override
    public List<Task> findAllTasks(TaskType taskType, TaskState taskState) {
        Example example = new Example(Task.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskType", taskType);
        criteria.andEqualTo("taskState", taskState);
        List<Task> list = taskMapper.selectByExample(example);
        return list;
    }

    @Override
    public int updateByConditionState(Task task, TaskState oldState, TaskState newState) {
        Task updater = new Task();
        updater.setTaskState(newState);
        Example example = new Example(Task.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", task.getId());
        criteria.andEqualTo("taskState", oldState);
        return taskMapper.updateByExample(updater, example);

    }

    @Override
    public int updateSubTaskBizData(SubTask subTask) {
        return subTaskMapper.updateByPrimaryKey(subTask);
    }

    @Override
    public void executeSingleTask(Task task, BizHandler bizHandler) {
        // 更新主任务状态
        updateByConditionState(task, TaskState.TASK_INIT, TaskState.TASK_PROCESSING);
        // 执行子任务业务
        executeSubTasks(task.getId(), bizHandler);
        // 更新任务表状态
        updateByConditionState(task, TaskState.TASK_PROCESSING, TaskState.TASK_DONE);
    }

    @Override
    public void executeSubTasks(Long taskId, BizHandler bizHandler) {
        List<SubTask> subTasks = getSubTasksByTaskId(taskId);
        subTasks.stream().forEach(e -> {
            // 组装任务请求参数
            AdaptorBaseRequest request = bizHandler.buildRequest(e.getKeyword());
            // 调用业务接口
            AdaptorResult<? extends AdaptorBaseResponse> adaptorResult = bizHandler.bizCall(request);
//            todo 此处可以直接处理业务
//            bizHandler.bizExecuteAfterCall(adaptorResult);
            // 更新子任务表请求响应数据
            e.setRequest(JSONUtil.toJsonStr(request));
            e.setResponse(JSONUtil.toJsonStr(adaptorResult));
            updateSubTaskBizData(e);
        });
    }

    @Override
    public void executeSingleTaskBiz(Task task, BizHandler bizHandler) {
        // 更新主任务状态
        updateByConditionState(task, TaskState.BUSINESS_PROCESSING, TaskState.TASK_DONE);
        // 执行子任务业务
        executeSubTasksBiz(task.getId(), bizHandler);
        // 更新任务表状态
        updateByConditionState(task, TaskState.BUSINESS_SUC, TaskState.BUSINESS_PROCESSING);

    }

    @Override
    public void executeSubTasksBiz(Long taskId, BizHandler bizHandler) {
        List<SubTask> subTasks = getSubTasksByTaskId(taskId);
        subTasks.stream().forEach(e -> {
            // 组装任务请求参数
            AdaptorResult adaptorResult = JSONUtil.toBean(e.getResponse(), AdaptorResult.class);
            bizHandler.bizExecuteAfterCall(adaptorResult);
        });
    }

    private List<SubTask> getSubTasksByTaskId(Long taskId) {
        Example example = new Example(SubTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        return subTaskMapper.selectByExample(example);
    }


}
