package com.extend.mq.annotation;

import com.extend.mq.config.EnableRocketMQImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableRocketMQ，功能开启注解.
 *
 * @author KevinClair
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableRocketMQImportSelector.class})
public @interface EnableRocketMQ {

    /**
     * nameserver地址，多个地址请用";"号分隔
     */
    String nameServerAddress() default "";

    /**
     * 发送消息超时时间
     */
    int sendMsgTimeOut() default 3000;

    /**
     * 发送消息时失败重试次数
     */
    int retryTimesWhenSendFailed() default 2;

    /**
     * 异步发送消息时失败重试次数
     */
    int retryTimesWhenSendAsyncFailed() default 2;

    /**
     * 发送消息的最大消息体
     */
    int maxMessageSize() default 4194304;

    /**
     * 消息body需要压缩的阈值
     */
    int compressMsgBodyOverHowmuch() default 4096;

    /**
     * 发送的结果如果不是SEND_OK状态，是否当作失败处理而尝试重发，只对同步发送有效
     */
    boolean retryAnotherBrokerWhenNotStoreOK() default false;
}
