package com.extend.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @ClassName Result
 * @Description 返回结果
 * @Author mingj
 * @Date 2019/12/22 17:57
 **/
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5528832160156012220L;

    private boolean status;

    private String message;

    private String statusCode;

    private T result;

    public Result() {
    }

    public Result(String message, T result, String statusCode, boolean status) {
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
        this.result = result;
    }

}
