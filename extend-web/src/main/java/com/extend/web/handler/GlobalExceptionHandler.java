package com.extend.web.handler;

import com.extend.common.base.Result;
import com.extend.common.base.ResultEnum;
import com.extend.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * 异常处理器
 *
 * @author KevinClair
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Result<Void> exceptionHandler(Exception e) {
        Result<Void> result = new Result<Void>();
        result.setStatus(false);
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            result.setStatusCode(baseException.getCode());
            result.setMessage(baseException.getMessage());
        } else if (e instanceof AccessDeniedException) {
            result.setStatusCode(ResultEnum.ACCESS_DENIED.getCode());
            result.setMessage(ResultEnum.ACCESS_DENIED.getMessage());
        }else if(e instanceof ValidationException){
            result.setStatusCode(ResultEnum.PARAM_ERROR.getCode());
            result.setMessage(ResultEnum.PARAM_ERROR.getMessage());
        }else if(e instanceof BindException){
            StringBuilder strBuilder = new StringBuilder();
            BindException  bindException= (BindException) e;
            BindingResult bindingResult = bindException.getBindingResult();
            List<ObjectError> list = bindingResult.getAllErrors();
            for (ObjectError error : list) {
                strBuilder.append(error.getDefaultMessage() + "\n");
            }
            result.setStatusCode(ResultEnum.PARAM_BIND_ERROR.getCode());
            result.setMessage(strBuilder.toString());
            return result;
        } else {
            result.setStatusCode(ResultEnum.SYSTEM_ERROR.getCode());
            result.setMessage(ResultEnum.SYSTEM_ERROR.getMessage());
            log.error("未知异常：{}", ExceptionUtils.getStackTrace(e));
        }
        return result;
    }
}
