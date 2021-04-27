package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: NameValBean
 * @Author: CS
 * @Date: 2019/11/5 16:16
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
@AllArgsConstructor
public class NameValBean<K, V> {
    private K name;
    private V value;
}
