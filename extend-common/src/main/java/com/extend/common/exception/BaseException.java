package com.extend.common.exception;

import java.text.MessageFormat;

/**
 * BaseException。
 *
 * @author KevinClair
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -8391393062479120200L;

    private String code;

    private String msg;

    private Boolean status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public BaseException(String code, String msg, Boolean status) {
        this.code = code;
        this.msg = msg;
        this.status = status;
    }

    public BaseException() {
    }

    public BaseException(Throwable cause, String code, String msg, Boolean status) {
        super(cause);
        this.code = code;
        this.msg = msg;
        this.status = status;
    }

    public static BaseException getInstance(Throwable e, String pattern, BaseExceotionEnum exceotionEnum, Object... args){
        String message = MessageFormat.format(pattern, args);
        return new BaseException(e, exceotionEnum.getCode(), message, exceotionEnum.getStatus());
    }

    public static BaseException getInstance(Throwable e, BaseExceotionEnum exceotionEnum){
        return new BaseException(e, exceotionEnum.getCode(), exceotionEnum.getMessage(), exceotionEnum.getStatus());
    }

    public static BaseException getInstance(BaseExceotionEnum exceotionEnum){
        return getInstance(null, exceotionEnum);
    }

    /**
     * 判断异常是否为BaseException
     *
     * @param t 异常
     * @return boolean
     */
    public static boolean isBaseException(Throwable t){
        return t == null ? false : BaseException.class.isAssignableFrom(t.getClass());
    }
}
