package com.extend.mq.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName RocketMQCnofiguration
 * @Description TODO描述
 * @Author mingj
 * @Date 2020/2/14 16:02
 **/
public class RocketMQConfiguration {
    private String topic;
    private String tag;
    private List<Map<String,Object>> params;
    private Object obj;
    private Method method;
    private String consumer;

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
}
