package com.extend.common.utils;

import javax.validation.*;
import java.util.Set;

/**
 * ValidationUtils。
 *
 * @author KevinClair
 */
public class ValidationUtils {
    private static final ValidatorFactory VFACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VFACTORY.getValidator();

    /**
     * 校验器
     *
     * @param t   需要校验的类
     * @param <T> 泛型
     * @throws ValidationException
     */
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
