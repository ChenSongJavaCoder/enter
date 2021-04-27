package com.cs.task.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @ClassName: Result
 * @Author: CS
 * @Date: 2019/11/5 14:55
 * @Description: 基础返回参数
 */
@Getter
@ApiModel
@AllArgsConstructor
public class AdaptorResult<T> {

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success;

    @ApiModelProperty(value = "是否同步返回 true：同步 false：异步", required = true)
    private boolean sync;

    @ApiModelProperty(value = "请求业务id", required = true)
    private String bizId;

    @ApiModelProperty(value = "使用策略", required = true)
    private Strategy strategy;

    @ApiModelProperty(value = "提示消息", required = true)
    private String message;

    @ApiModelProperty(value = "返回值", required = true)
    private T data;

    public static Builder success(Strategy strategy) {
        return new Builder(strategy, true, "success");
    }

    /**
     * 失败信息返回
     *
     * @return
     */
    public static Builder failure(Strategy strategy) {
        return new Builder(strategy, false, "failure");
    }

    /**
     * 参数非法
     *
     * @return
     */
    public static Builder illegal() {
        return new Builder(false, "params illegal! ");
    }


    /**
     * 暂未支持
     *
     * @return
     */
    public static Builder nonsupport(Strategy strategy) {
        return new Builder(strategy, false, "Do not support this operation！");
    }

    @Accessors(chain = true)
    public static class Builder {
        private boolean success;
        private boolean sync;
        private String bizId;
        private Strategy strategy;
        private String message;
        private Object data;

        protected Builder(boolean success, String message) {
            this.success = success;
            this.message = message;
            this.sync = true;
        }

        protected Builder(Strategy strategy, boolean success, String message) {
            this.strategy = strategy;
            this.success = success;
            this.message = message;
            this.sync = true;
        }

        public Builder sync(boolean sync) {
            this.sync = sync;
            return this;
        }

        public Builder bizId(String bizId) {
            this.bizId = bizId;
            return this;
        }

        public Builder strategy(Strategy strategy) {
            this.strategy = strategy;
            return this;
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
        public <T> AdaptorResult<T> build() {
            return new AdaptorResult<T>(success, sync, bizId, strategy, message, (T) data);
        }

    }

}
