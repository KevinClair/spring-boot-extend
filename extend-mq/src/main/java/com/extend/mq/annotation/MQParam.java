package com.extend.mq.annotation;

import com.extend.common.constant.RocketMQParamNameEnum;
import com.extend.common.constant.RocketMQParamSerializeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQParam，消息体属性映射。
 *
 * @author KevinClair
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MQParam {

    RocketMQParamNameEnum name() default RocketMQParamNameEnum.BODY ;

    RocketMQParamSerializeEnum serialize() default RocketMQParamSerializeEnum.STRING;

    Class serializeType() default String.class;
}
