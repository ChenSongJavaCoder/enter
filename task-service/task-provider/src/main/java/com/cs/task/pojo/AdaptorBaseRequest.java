package com.cs.task.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: CS
 * @date: 2021/3/6 下午1:47
 * @description: 适配层接口必填参数
 */
@Getter
@Setter
public abstract class AdaptorBaseRequest {

    /**
     * 客户id
     */
    @NotBlank
    private String customId;

    /**
     * 税盘号
     */
    private String spbh;

    /**
     * 流水号 重试请求时传值
     */
    private String bizId;

    /**
     * 解密密钥 凯盈使用
     */
    private String jmmy;

}
