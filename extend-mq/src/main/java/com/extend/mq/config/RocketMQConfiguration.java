package com.extend.mq.config;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * RocketMQConfiguration，属性配置。
 *
 * @author KevinClair
 */
public class RocketMQConfiguration {
    private String topic;
    private String tag;
    private List<Map<String,Object>> params;
    private Object obj;
    private Method method;
    private String consumer;
    private MessageModel messageModel;
    private int consumeThreadMax;
    private int consumeThreadMin;
    private long consumeTimeOut;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, Object>> params) {
        this.params = params;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public int getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public int getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public long getConsumeTimeOut() {
        return consumeTimeOut;
    }

    public void setConsumeTimeOut(long consumeTimeOut) {
        this.consumeTimeOut = consumeTimeOut;
    }
}
