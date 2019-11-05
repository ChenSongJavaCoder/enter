package com.cs.user.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: UserInfo
 * @Author: CS
 * @Date: 2019/11/5 16:48
 * @Description:
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserInfo {

    private Long id;

    private String name;

    private Integer age;

    /**
     * 使用该方式选择部分属性不参与序列化
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean deleted;
}
