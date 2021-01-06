package com.extend.mq.annotation;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

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

    /**
     * consumerGroup
     */
    String consumerGroup() default "";

    /**
     * tags
     */
    String tags() default "*";

    /**
     * topoc
     */
    String topic() default "";

    /**
     * 最小消费线程数.
     */
    int consumeThreadMin() default 20;

    /**
     * 最大消费线程数.
     */
    int consumeThreadMax() default 64;

    /**
     * 最大消费超时时间
     */
    long consumeTimeout() default 30000L;

    /**
     * Control message mode, if you want all subscribers receive message all message, broadcasting is a good choice.
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;
}
