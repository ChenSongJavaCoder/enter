package com.cs.task.pojo;


/**
 * @author: CS
 * @date: 2021/4/24 下午3:39
 * @description: 第三方模拟数据返回，用作模拟调试使用
 */
public interface Mock<T extends AdaptorBaseResponse> {
    /**
     * mock数据
     *
     * @return
     */
    T mock();
}
