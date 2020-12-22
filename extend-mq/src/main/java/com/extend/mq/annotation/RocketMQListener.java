package com.extend.mq.annotation;

import java.lang.annotation.*;

/**
 * RocketMQListener，消息监听。
 *
 * @author KevinClair
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RocketMQListener {

    String consumerGroup() default "";

    String tags() default "*";

    String topic() default "";
}
