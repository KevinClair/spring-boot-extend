package com.extend.mybatis.annotation;

import java.lang.annotation.*;

/**
 * TransactionalManagement.
 *
 * @author KevinClair
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TransactionalManagement {

    String[] value() default {};
}
