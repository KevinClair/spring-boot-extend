package com.extend.mq.config;

import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import com.extend.common.utils.InterceptorUtils;
import com.extend.mq.annotation.MQParam;
import com.extend.mq.annotation.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName RocketMQListenerInitialization
 * @Description MQ消息监听初始化
 * @Author mingj
 * @Date 2020/1/31 22:21
 **/
@Slf4j
public class RocketMQListenerInitialization implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {

    private String nameServerAddress;
    private String consumerGroup;
    private int threadMax;
    private int threadMin;
    private long consumeTimeOut;
    private Map<String, RocketMQConfiguration> config = new HashMap<>();

    /**
    *@Description 获取所有订阅的Topic并打印
    *@Param [event]
    *@Author mingj
    *@Date 2020/1/31 22:27
    *@Return void
    **/
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (config.size() > 0) {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(nameServerAddress);
            consumer.setConsumeThreadMax(threadMax);
            consumer.setConsumeThreadMin(threadMin);
            consumer.setConsumeTimeout(consumeTimeOut);
            consumer.setMessageModel(MessageModel.CLUSTERING);
            Map<String, String> topicTags = new HashMap<>();
            for (Map.Entry<String, RocketMQConfiguration> entry:config.entrySet()){
                Map<String, String> mqInfos = new HashMap<>();
                if (topicTags.containsKey(entry.getValue().getTopic())) {
                    if ("*".equals(entry.getValue().getTag())) {
                        topicTags.put(entry.getValue().getTopic(), entry.getValue().getTag());
                    } else {
                        topicTags.put(entry.getValue().getTopic(), topicTags.get(entry.getValue().getTopic()) + "||" + entry.getValue().getTag());
                    }
                } else {
                    topicTags.put(entry.getValue().getTopic(), entry.getValue().getTag());
                }
                mqInfos.put("Topic", entry.getValue().getTopic());
                mqInfos.put("Tag", entry.getValue().getTag());
                mqInfos.put("Consumer", entry.getValue().getConsumer());

                log.info("RocketMQ消费消息topic列表[{}]", mqInfos);
            }
            for (Map.Entry<String, String> e : topicTags.entrySet()) {
                try {
                    consumer.subscribe(e.getKey(), e.getValue());
                } catch (MQClientException ex) {
                    log.error("RocketMQ订阅消息发生异常，异常信息：{}", ExceptionUtils.getStackTrace(ex));
                }
            }
            try {
                MessageListenerConcurrentlyImpl mqlistener = InterceptorUtils.getProxyClass(MessageListenerConcurrentlyImpl.class, new Class[]{Map.class}, new Object[]{config}, "RocketMQ.consumer");
                consumer.setMessageListener(mqlistener);
            } catch (Exception e) {
                log.error("RocketMQ监听器配置启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
            }
            try {
                // 启动
                consumer.start();
                log.info("RocketMQ消费端启动成功……");
            } catch (MQClientException e) {
                log.error("RocketMQ消费端启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /**
    *@Description 在Bean初始化之前，扫描被@RocketMQListener扫描的方法，获取方法的属性；
    *@Param [bean, beanName]
    *@Author mingj
    *@Date 2020/1/31 22:24
    *@Return java.lang.Object
    **/
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RocketMQListener.class)){
                RocketMQListener listener = method.getAnnotation(RocketMQListener.class);
                String topic = listener.topic();
                String tags = listener.tags();
                String consumerGroup = listener.consumerGroup();
                if (tags.equals("*")){
                    config.put(topic + "_" + tags, getConfig(bean, method, topic, tags, consumerGroup));
                } else {
                    String[] tagArray = tags.split("\\|\\|");
                    for (String tag : tagArray) {
                        config.put(topic + "_" + tag, getConfig(bean, method, topic, tag, consumerGroup));
                    }
                }
            }
        }
        return bean;
    }

    /**
    *@Description 读取MQ相关配置信息
    *@Param [bean, method, topic, tags]
    *@Author mingj
    *@Date 2020/2/14 16:28
    *@Return com.example.mq.config.RocketMQCnofiguration
    **/
    private RocketMQConfiguration getConfig(Object bean, Method method, String topic, String tags, String consumerGroup) {
        RocketMQConfiguration configuration = new RocketMQConfiguration();
        configuration.setTopic(topic);
        configuration.setMethod(method);
        configuration.setObj(bean);
        configuration.setTag(tags);
        configuration.setConsumer(consumerGroup);
        Parameter[] parameters = method.getParameters();
        if (parameters.length <= 0) {
            throw new BaseException(BaseExceotionEnum.ROCKET_MQ_INIT_ERROR.getCode(), BaseExceotionEnum.ROCKET_MQ_INIT_ERROR.getMessage(), BaseExceotionEnum.ROCKET_MQ_INIT_ERROR.getStatus());
        }
        List<Map<String, Object>> params = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            MQParam param = parameter.getAnnotation(MQParam.class);
            Map<String, Object> map = new HashMap<>();
            if (param == null) {
                map.put("name", parameter.getName());
                map.put("serialize", "String");
                map.put("serializeType",String.class);
            } else {
                map.put("name", param.name());
                map.put("serialize", param.serialize());
                map.put("serializeType",param.serializeType());
            }
            params.add(map);
        }
        configuration.setParams(params);
        return configuration;
    }

    public String getNameServerAddress() {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getThreadMax() {
        return threadMax;
    }

    public void setThreadMax(int threadMax) {
        this.threadMax = threadMax;
    }

    public int getThreadMin() {
        return threadMin;
    }

    public void setThreadMin(int threadMin) {
        this.threadMin = threadMin;
    }

    public long getConsumeTimeOut() {
        return consumeTimeOut;
    }

    public void setConsumeTimeOut(long consumeTimeOut) {
        this.consumeTimeOut = consumeTimeOut;
    }
}
