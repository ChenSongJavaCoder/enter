package com.cs.task.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CS
 * @date: 2021/3/16 下午3:21
 * @description:
 */
@Getter
@AllArgsConstructor
public enum Strategy {
    MB(1, "蒙柏"),
    UKEY(2, "虚拟设备（自有开票）"),
    KY(3, "凯盈"),
    ;
    private Integer code;
    private String desc;
}
