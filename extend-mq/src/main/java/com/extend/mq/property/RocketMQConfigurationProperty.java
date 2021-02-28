package com.extend.mq.property;

import lombok.Data;

/**
 * RocketMQ配置类
 *
 * @author KevinClair
 */
@Data
public class RocketMQConfigurationProperty {

    /**
     * nameserver地址，多个地址请用";"号分隔
     */
    private String nameServerAddress;

    /**
     * 发送消息超时时间
     */
    private int sendMsgTimeOut = 3000;

    /**
     * 发送消息时失败重试次数
     */
    private int retryTimesWhenSendFailed = 2;

    /**
     * 异步发送消息时失败重试次数
     */
    private int retryTimesWhenSendAsyncFailed = 2;

    /**
     * 发送消息的最大消息体
     */
    private int maxMessageSize = 4194304;

    /**
     * 消息body需要压缩的阈值
     */
    private int compressMsgBodyOverHowmuch = 4096;

    /**
     * 发送的结果如果不是SEND_OK状态，是否当作失败处理而尝试重发，只对同步发送有效
     */
    private boolean retryAnotherBrokerWhenNotStoreOK = false;

    /**
     * 事务回查监听器最小线程数
     */
    private int checkThreadPoolMinSize = 1;

    /**
     * 事务回查监听器最大线程数
     */
    private int checkThreadPoolMaxSize = 1;
}
