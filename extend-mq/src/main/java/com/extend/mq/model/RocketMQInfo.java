package com.extend.mq.model;

import lombok.Data;

/**
 * RocketMQInfo.
 *
 * @author KevinClair
 */
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
