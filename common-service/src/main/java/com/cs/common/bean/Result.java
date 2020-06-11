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
 * @Description: 基础返回参数
 */
@Getter
@ApiModel
@AllArgsConstructor
public class Result<T> {

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;

    @ApiModelProperty(value = "返回码", required = true)
    private int code;

    @ApiModelProperty(value = "提示消息", required = true)
    private String message;

    @ApiModelProperty(value = "返回值", required = true)
    private T data;

    public static Builder success() {
        return new Builder(true, ResultCode.SUCCESS.getCode(), MessageBuilder.successMessage());
    }

    public static Builder failure() {
        return new Builder(false, ResultCode.FAILURE.getCode(), MessageBuilder.failureMessage());
    }

    public static Builder illegal() {
        return new Builder(false, ResultCode.ILLEGAL.getCode(), MessageBuilder.failureMessage());
    }

    @Accessors(chain = true)
    public static class Builder {
        private boolean success;
        private int code;
        private String message;
        private Object data;

        protected Builder(boolean success, int code, String message) {
            this.success = success;
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
            return new Result<T>(success, code, message, (T) data);
        }

    }

}
