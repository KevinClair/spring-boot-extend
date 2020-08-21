package com.extend.common.utils;

import javax.validation.*;
import java.util.Set;

/**
 * @version 1.0
 * @ClassName ValidationUtils
 * @Description 参数校验工具类
 * @Author mingj
 * @Date 2019/12/19 10:10
 **/

public class ValidationUtils {
    private static final ValidatorFactory VFACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VFACTORY.getValidator();

    /**
    *@Description 实体类校验
    *@Param [t]
    *@Author mingj
    *@Date 2019/12/22 18:06
    *@Return void
    **/
    public static <T> void validate(T t) throws ValidationException {
        
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(t);
        StringBuilder validateError = new StringBuilder();
        if (set.size() > 0) {
            for (ConstraintViolation<T> p : set) {
                validateError.append(p.getMessage());
                validateError.append(",");
            }
            throw new ValidationException(validateError.toString().substring(0,validateError.length()-1));
        }
    }
}
