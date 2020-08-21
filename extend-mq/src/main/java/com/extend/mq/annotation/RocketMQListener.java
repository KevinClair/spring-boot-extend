package com.extend.mq.annotation;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @ClassName RocketMQListener
 * @Description MQ消息监听
 * @Author mingj
 * @Date 2020/1/31 22:31
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RocketMQListener {

    String consumerGroup() default "";

    String tags() default "*";

    String topic() default "";
}
