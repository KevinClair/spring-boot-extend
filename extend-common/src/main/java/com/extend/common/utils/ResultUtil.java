package com.extend.common.utils;

import com.extend.common.base.Result;
import com.extend.common.base.ResultEnum;
import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;

import javax.validation.ValidationException;
import java.sql.SQLException;

public class ResultUtil {

    /**
     *@Description 异常返回实体类信息
     *@Param [t]
     *@Author mingj
     *@Date 2019/1/16 16:23
     *@Return com.extend.common.reponse.base.ResultResponse<T>
     **/
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
    *@Description 无数据时返回信息
    *@Param [t]
    *@Author mingj
    *@Date 2019/4/2 14:24
    *@Return com.extend.facade.response.base.ResultResponse<T>
    **/
    public static <T> Result<T> withoutData(T t){
        return new Result<>(ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getMessage() , t , ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getCode() , ResultEnum.REQUEST_SUCCESS_WITHOUTDATA.getStatus());
    }

    /**
    *@Description 操作成功时返回信息
    *@Param [t]
    *@Author mingj
    *@Date 2019/4/2 14:24
    *@Return com.extend.facade.response.base.ResultResponse<T>
    **/
    public static <T> Result<T> success(T t){
        return new Result<>(ResultEnum.REQUEST_SUCCESS.getMessage(), t, ResultEnum.REQUEST_SUCCESS.getCode(), ResultEnum.REQUEST_SUCCESS.getStatus());
    }
}
