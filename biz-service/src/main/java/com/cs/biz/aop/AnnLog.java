package com.cs.biz.aop;

/**
 * @author: CS
 * @date: 2021/4/12 上午11:06
 * @description:
 */
public interface AnnLog {

    <R> R log(Object... param);
}
