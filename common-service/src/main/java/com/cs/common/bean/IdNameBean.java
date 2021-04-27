package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: IdNameBean
 * @Author: CS
 * @Date: 2019/11/5 16:16
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class IdNameBean<K, V> {
    private K id;
    private V name;
}
