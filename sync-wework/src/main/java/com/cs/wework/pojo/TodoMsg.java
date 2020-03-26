package com.cs.wework.pojo;

import com.cs.wework.entity.BaseMsg;
import lombok.Data;

/**
 * @Author: CS
 * @Date: 2020/3/23 2:10 下午
 * @Description:
 */
@Data
public class TodoMsg extends BaseMsg<TodoMsg.Todo> {

    private Todo todo;

    @Data
    public static class Todo {

        private String title;

        private String content;
    }
}
