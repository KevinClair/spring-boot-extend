package com.extend.mq.model;

import lombok.Data;

/**
 * @version 1.0
 * @ClassName RocketMQInfo
 * @Description MQ信息
 * @Author mingj
 * @Date 2020/1/31 22:57
 **/
@Data
public class RocketMQInfo {

    private String consumerGroup;

    private String tags;

    private String consumeThreadMin;

    private String consumeThreadMax;

    public RocketMQInfo(String consumerGroup, String tags, String consumeThreadMin, String consumeThreadMax) {
        this.consumerGroup = consumerGroup;
        this.tags = tags;
        this.consumeThreadMin = consumeThreadMin;
        this.consumeThreadMax = consumeThreadMax;
    }

    public RocketMQInfo() {
    }
}
