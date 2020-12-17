package com.extend.dubbo.validation;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

/**
 * ExtendDubboClientValidation.
 *
 * @author KevinClair
 */
public class ExtendDubboClientValidation implements Validation {
    @Override
    public Validator getValidator(URL url) {
        return new ExtendDubboClientValidator(url);
    }
}
