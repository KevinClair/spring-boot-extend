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

    /**
     * MQ消息类型 {@link RocketMQParamNameEnum}
     * @return
     */
    RocketMQParamNameEnum name() default RocketMQParamNameEnum.BODY ;

    /**
     * 序列化类型 {@link RocketMQParamSerializeEnum}
     * @return
     */
    RocketMQParamSerializeEnum serialize() default RocketMQParamSerializeEnum.STRING;

    /**
     * 需要反序列化成的对象信息
     * @return
     */
    Class serializeType() default String.class;
}
