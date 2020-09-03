package com.extend.common.constant;

/**
 * Cat的Type类型枚举
 *
 * @author mingj
 * @date 2020/9/4 0:17
 **/
public enum CatTypeEnum {
    // RocketMQ消费端
    ROCKETMQ_CONSUMER("RocketMQ_consumer"),
    // RocketMQ发送端
    ROCKETMQ_PRODUCER("RocketMQ_producer");

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
