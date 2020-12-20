package com.extend.common.constant;

/**
 * CatEventTypeEnum。
 *
 * @author KevinClair
 */
public enum CatEventTypeEnum {
    // RocketMQ消费端消息体
    ROCKETMQ_CONSUMER_MESSAGE("RocketMQ.consumer.message"),
    // RocketMQ消费端消费结果
    ROCKETMQ_CONSUMER_RESULT("RocketMQ.consumer.result"),
    // RocketMQ发送端消息体
    ROCKETMQ_PRODUCER_MESSAGE("RocketMQ.producer.message"),
    // RocketMQ发送端消息体
    ROCKETMQ_PRODUCER_RESULT("RocketMQ.producer.result"),
    // RocketMQ发送端消息体
    ROCKETMQ_TRANSACTION_PRODUCER_MESSAGE("RocketMQ.transactionproducer.message"),
    // RocketMQ发送端消息体
    ROCKETMQ_TRANSACTION_PRODUCER_RESULT("RocketMQ.transactionproducer.result"),
    // Mybatis请求方法
    MYBATIS_REQUEST_COMMANDTYPE("Mybatis.mapper.commandType"),
    // Mybatis请求数据库信息
    MYBATIS_REQUEST_DATABASE("Mybatis.mapper.Database"),
    // 数据源请求，DB类型
    DRUID_SQL_DBTYPE("Druid.sql.DbType"),
    // 数据源请求，数据库信息
    DRUID_SQL_DATABASE("Druid.sql.Database"),
    // 数据源请求，sql语句；
    DRUID_SQL_SQL("Druid.sql.sql"),
    // Dubbo客户端
    DUBBO_CLIENT_SERVER("Dubbo.client.server"),
    // Dubbo客户端
    DUBBO_CLIENT_APP("Dubbo.client.app"),
    // Dubbo客户端
    DUBBO_CLIENT_PORT("Dubbo.client.port"),
    // Dubbo服务端
    DUBBO_SERVER_CLIENT("Dubbo.server.client"),
    // Dubbo服务端
    DUBBO_SERVER_APP("Dubbo.server.app"),
    // Dubbo服务端
    DUBBO_SERVER_PORT("Dubbo.server.port");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    CatEventTypeEnum(String name) {
        this.name = name;
    }

    CatEventTypeEnum() {
    }
}
