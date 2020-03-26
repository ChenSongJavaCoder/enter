package com.tencent.wework;

/**
 * @Author: CS
 * @Date: 2020/3/24 5:03 下午
 * @Description:
 */
public class ProcessResult {
    private boolean success = false;
    private String errorMessage;
    private String outputMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

}
