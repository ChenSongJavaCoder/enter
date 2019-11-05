package com.cs.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @ClassName: PagedRequest
 * @Author: CS
 * @Date: 2019/11/5 14:55
 * @Description: 基础分页返回参数
 */
@Getter
@ApiModel
@AllArgsConstructor
public class Result<T> {
    @ApiModelProperty(value = "返回码", required = true)
    private int code;

    @ApiModelProperty(value = "提示消息", required = true)
    private String message;

    @ApiModelProperty(value = "返回值", required = true)
    private T data;

    public static Builder success() {
        return new Builder(ResultCode.SUCCESS.getCode(), MessageBuilder.successMessage());
    }

    public static Builder failure() {
        return new Builder(ResultCode.FAILURE.getCode(), MessageBuilder.failureMessage());
    }

    @Accessors(chain = true)
    public static class Builder {
        private int code;
        private String message;
        private Object data;

        protected Builder(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> Result<T> build() {
            return new Result<T>(code, message, (T) data);
        }

    }

}
