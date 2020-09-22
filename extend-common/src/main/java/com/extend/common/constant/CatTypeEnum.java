package com.extend.common.constant;

/**
 * Cat的Transaction类型枚举
 *
 * @author mingj
 * @date 2020/9/4 0:17
 **/
public enum CatTypeEnum {
    // RocketMQ消费端
    ROCKETMQ_CONSUMER("RocketMQ.consumer"),
    // RocketMQ发送端
    ROCKETMQ_PRODUCER("RocketMQ.producer"),
    // RocketMQ事务消息发送端
    ROCKETMQ_TRANSACTION_PRODUCER("RocketMQ.transactionproducer"),
    // URLforward请求
    URL_FORWARD("URL.forward"),
    // URL请求
    URL("URL"),
    // Mybatis请求
    MYBATIS_REQUEST("Mybatis.mapper"),
    // 数据源请求
    DRUID_SQL("Druid.sql"),
    // Dubbo 客户端
    DUBBO_CLIENT("Dubbo.client"),
    // Dubbo 服务端
    DUBBO_SERVER("Dubbo.server");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CatTypeEnum(String name) {
        this.name = name;
    }

    CatTypeEnum() {
    }
}
