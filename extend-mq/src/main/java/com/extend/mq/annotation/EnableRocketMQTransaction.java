package com.extend.mq.annotation;

import com.extend.mq.config.EnableRocketMQTransactionImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableRocketMQTransaction，事务消息开启注解。
 *
 * @author KevinClair
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableRocketMQTransactionImportSelector.class})
public @interface EnableRocketMQTransaction {

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
     *
     */
    int compressMsgBodyOverHowmuch() default 4096;

    /**
     *
     */
    boolean retryAnotherBrokerWhenNotStoreOK() default false;

    /**
     * 事务回查监听器最小线程数
     */
    int checkThreadPoolMinSize() default 1;

    /**
     * 事务回查监听器最大线程数
     */
    int checkThreadPoolMaxSize() default 1;
}
