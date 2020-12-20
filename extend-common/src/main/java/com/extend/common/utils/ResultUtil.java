package com.extend.common.utils;

import com.extend.common.base.Result;
import com.extend.common.base.ResultEnum;
import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;

import javax.validation.ValidationException;
import java.sql.SQLException;

/**
 * InterceptorUtils。
 *
 * @author KevinClair
 */
public class ResultUtil {

    /**
     * 根据异常类型返回结果
     *
     * @param e Exception
     * @return {{@link Result}}
     */
    public static Result<String> response(Throwable e){
        if (e instanceof ValidationException){
            return new Result<>(e.getMessage(), e.getMessage(), BaseExceotionEnum.REQUEST_PARAM_ERROR.getCode(), BaseExceotionEnum.REQUEST_PARAM_ERROR.getStatus());
        }else if (e instanceof BaseException) {
            return new Result<>(((BaseException) e).getMsg(), BaseExceotionEnum.BASE_BUSINESS_ERROR.getMessage(), ((BaseException) e).getCode(), BaseExceotionEnum.BASE_BUSINESS_ERROR.getStatus());
        }else if (e instanceof SQLException){
            return new Result<>(BaseExceotionEnum.DATABASE_SQL_ERROR.getMessage(), BaseExceotionEnum.DATABASE_SQL_ERROR.getMessage(), BaseExceotionEnum.DATABASE_SQL_ERROR.getCode(), BaseExceotionEnum.DATABASE_SQL_ERROR.getStatus());
        }else{
            return new Result<>(BaseExceotionEnum.SYSTEM_ERROR.getMessage(), BaseExceotionEnum.SYSTEM_ERROR.getMessage(), BaseExceotionEnum.SYSTEM_ERROR.getCode(), BaseExceotionEnum.SYSTEM_ERROR.getStatus());
        }
    }

    /**
     * 查询结果无数据时返回
     *
     * @param t   结果
     * @param <T> 结果的类型
     * @return {{@link Result}}
     */
    public static <T> Result<T> withoutData(T t){
        return new Result<>(ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getMessage() , t , ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getCode() , ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getStatus());
    }

    /**
     * 操作成功后返回
     *
     * @param t   结果
     * @param <T> 结果的类型
     * @return {{@link Result}}
     */
    public static <T> Result<T> success(T t){
        return new Result<>(ResultEnum.REQUEST_SUCCESS.getMessage(), t, ResultEnum.REQUEST_SUCCESS.getCode(), ResultEnum.REQUEST_SUCCESS.getStatus());
    }
}
