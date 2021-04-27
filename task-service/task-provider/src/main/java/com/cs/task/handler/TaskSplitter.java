package com.cs.task.handler;

import com.cs.task.entity.SubTask;
import com.cs.task.pojo.AdaptorBaseResponse;
import com.cs.task.pojo.AdaptorResult;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * @author: CS
 * @date: 2021/4/26 下午5:26
 * @description: 子任务拆分
 */
public interface TaskSplitter {
    /**
     * 拆分子任务
     *
     * @param firstSubTask
     * @return
     */
    default List<SubTask> splitTaskByDate(SubTask firstSubTask) {
        return Lists.newArrayList(firstSubTask);
    }


    default List<SubTask> splitTaskByPage(SubTask firstSubTask, AdaptorResult<? extends AdaptorBaseResponse> adaptorResult) {
        return Lists.newArrayList(firstSubTask);
    }
}
