package com.extend.common.base;

/**
 * @version 1.0
 * @ClassName ResultEnum
 * @Description 返回结果枚举
 * @Author mingj
 * @Date 2019/12/22 18:43
 **/
public enum ResultEnum {

    REQUEST_SUCCESS("200", "请求或操作成功", true),
    REQUEST_SUCCESS_WITHOUTDATA("201", "请求成功但是查无数据", true)
    ;

    private String code;

    private String message;

    private boolean status;

    ResultEnum(String code, String message, boolean status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
