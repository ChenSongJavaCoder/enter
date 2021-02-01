package com.cs.user.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "用户名 可用作登陆，具有唯一性（eg：工号，手机号）", example = "0001")
    private String username;

    @ApiModelProperty(value = "姓名 昵称", example = "霸王龙")
    private String nickname;

    @Min(0)
    @ApiModelProperty(value = "年龄", example = "26")
    private Integer age;

    /**
     * 使用该方式选择部分属性不参与序列化
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    /**
     * 该属性字段不应通用放开，可拿掉，需要业务处理时单独处理。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean deleted;
}
