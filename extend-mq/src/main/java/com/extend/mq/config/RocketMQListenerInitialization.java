package com.extend.mq.config;

import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import com.extend.core.utils.InterceptorUtils;
import com.extend.mq.annotation.MQParam;
import com.extend.mq.annotation.RocketMQListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
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
 * RocketMQListenerInitialization，初始化消息监听。
 *
 * @author KevinClair
 */
@Slf4j
@Data
public class RocketMQListenerInitialization implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {

    private String nameServerAddress;
    private String accessKey;
    private String secretKey;
    private Map<String, RocketMQConfiguration> config = new HashMap<>();

    /**
     * 获取所有订阅的Topic并打印
     *
     * @param event {{@link ApplicationReadyEvent}}
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (config.size() > 0) {
            for (Map.Entry<String, RocketMQConfiguration> entry : config.entrySet()) {
                RocketMQConfiguration rocketMQConfiguration = entry.getValue();
                Map<String, Object> mqInfos = new HashMap<>();
                mqInfos.put("Topic", rocketMQConfiguration.getTopic());
                mqInfos.put("Tag", rocketMQConfiguration.getTag());
                mqInfos.put("Consumer", rocketMQConfiguration.getConsumer());
                mqInfos.put("MessageModel", rocketMQConfiguration.getMessageModel());
                log.info("RocketMQ消费消息topic列表[{}]", mqInfos);
                try {
                    DefaultMQPushConsumer consumer = null;
                    if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)) {
                        consumer = new DefaultMQPushConsumer(null, rocketMQConfiguration.getConsumer(), new AclClientRPCHook(new SessionCredentials(accessKey, secretKey)));
                    } else {
                        consumer = new DefaultMQPushConsumer(rocketMQConfiguration.getConsumer());
                    }
                    consumer.setNamesrvAddr(nameServerAddress);
                    consumer.setConsumeThreadMax(rocketMQConfiguration.getConsumeThreadMax());
                    consumer.setConsumeThreadMin(rocketMQConfiguration.getConsumeThreadMin());
                    consumer.setConsumeTimeout(rocketMQConfiguration.getConsumeTimeOut());
                    consumer.setMessageModel(rocketMQConfiguration.getMessageModel());
                    consumer.subscribe(rocketMQConfiguration.getTopic(), rocketMQConfiguration.getTag());
                    MessageListenerConcurrentlyImpl mqlistener = InterceptorUtils.getProxyClass(MessageListenerConcurrentlyImpl.class, new Class[]{Map.class}, new Object[]{config}, "RocketMQ.consumer");
                    consumer.setMessageListener(mqlistener);
                    // 启动
                    consumer.start();
                    log.info("RocketMQ消费端启动成功，消息订阅组信息:{}", consumer.getConsumerGroup());
                } catch (MQClientException ex) {
                    log.error("RocketMQ订阅消息发生异常，异常信息：{}", ExceptionUtils.getStackTrace(ex));
                } catch (Exception ex) {
                    log.error("RocketMQ启动异常，异常信息：{}", ExceptionUtils.getStackTrace(ex));
                }
            }
        }
    }

    /**
     * 在Bean初始化之前，扫描被@RocketMQListener扫描的方法，获取方法的属性；
     *
     * @param bean     bean类型
     * @param beanName bean名称
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RocketMQListener.class)) {
                RocketMQListener listener = method.getAnnotation(RocketMQListener.class);
                String topic = listener.topic();
                String tags = listener.tags();
                String consumerGroup = listener.consumerGroup();
                MessageModel messageModel = listener.messageModel();
                int consumeThreadMax = listener.consumeThreadMax();
                int consumeThreadMin = listener.consumeThreadMin();
                long consumeTimeout = listener.consumeTimeout();
                if (tags.equals("*")) {
                    config.put(topic + "_" + tags, getConfig(bean, method, topic, tags, consumerGroup, messageModel, consumeThreadMax, consumeThreadMin, consumeTimeout));
                } else {
                    String[] tagArray = tags.split("\\|\\|");
                    for (String tag : tagArray) {
                        config.put(topic + "_" + tag, getConfig(bean, method, topic, tag, consumerGroup, messageModel, consumeThreadMax, consumeThreadMin, consumeTimeout));
                    }
                }
            }
        }
        return bean;
    }

    /**
     * 读取MQ相关配置信息
     *
     * @param bean          bean
     * @param method        方法
     * @param topic         订阅的topic信息
     * @param tags          消息的tag
     * @param consumerGroup 消费组
     * @return {{@link RocketMQConfiguration}}
     */
    private RocketMQConfiguration getConfig(Object bean, Method method, String topic, String tags, String consumerGroup, MessageModel messageModel, int consumeThreadMax, int consumeThreadMin, long consumeTimeout) {
        RocketMQConfiguration configuration = new RocketMQConfiguration();
        configuration.setTopic(topic);
        configuration.setMethod(method);
        configuration.setObj(bean);
        configuration.setTag(tags);
        configuration.setConsumer(consumerGroup);
        configuration.setMessageModel(messageModel);
        configuration.setConsumeThreadMax(consumeThreadMax);
        configuration.setConsumeThreadMin(consumeThreadMin);
        configuration.setConsumeTimeOut(consumeTimeout);
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
                map.put("serializeType", String.class);
            } else {
                map.put("name", param.name());
                map.put("serialize", param.serialize());
                map.put("serializeType", param.serializeType());
            }
            params.add(map);
        }
        configuration.setParams(params);
        return configuration;
    }
}
