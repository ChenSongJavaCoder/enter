package com.cs.es.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: CS
 * @date: 2021/6/5 下午9:50
 * @description:
 */
public class ThreadPoolUtil {

    /**
     * 异步更新关联实体document
     */
    public static ThreadPoolExecutor ASYNC_UPDATE_RELATED_ENTITY = new ThreadPoolExecutor(
            10,
            30,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            (r) -> new Thread(r, "ASYNC_UPDATE_RELATED_ENTITY"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 异步更新关联字段document
     */
    public static ThreadPoolExecutor ASYNC_UPDATE_RELATED_FIELD = new ThreadPoolExecutor(
            10,
            30,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            (r) -> new Thread(r, "ASYNC_UPDATE_RELATED_FIELD"),
            new ThreadPoolExecutor.CallerRunsPolicy());

}
