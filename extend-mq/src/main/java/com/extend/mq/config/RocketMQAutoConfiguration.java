package com.extend.mq.config;

import com.extend.common.constant.EnvironmentManager;
import com.extend.common.utils.InterceptorUtils;
import com.extend.mq.template.RocketMQTemplate;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * RocketMQAutoConfiguration，配置类。
 *
 * @author KevinClair
 */
@Slf4j
@Builder
public class RocketMQAutoConfiguration implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    private ConfigurableEnvironment env;
    private String nameServerAddress;
    private int sendMsgTimeOut;
    private int retryTimesWhenSendFailed;
    private int retryTimesWhenSendAsyncFailed;
    private int maxMessageSize;
    private int compressMsgBodyOverHowmuch;
    private boolean retryAnotherBrokerWhenNotStoreOK;
    private int threadMin;
    private int threadMax;
    private long consumeTimeOut;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 注册RocketMQListenerInitialization
        registerRocketMQListenerInitialization(registry);
    }

    /**
     * 注册RocketMQListenerInitialization
     *
     * @param registry {{@link BeanDefinitionRegistry}}
     */
    private void registerRocketMQListenerInitialization(BeanDefinitionRegistry registry) {
        String consumerGroup = EnvironmentManager.getProperty(env, "rocketmq.consumer.consumerGroup", EnvironmentManager.getAppid()+"ConsumerGroup");
        String nameServerAddress = EnvironmentManager.getProperty(env, "rocketmq.nameServerAddress");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RocketMQListenerInitialization.class);
        builder.addPropertyValue("nameServerAddress", nameServerAddress);
        builder.addPropertyValue("consumerGroup", consumerGroup);
        builder.addPropertyValue("threadMax", threadMax);
        builder.addPropertyValue("threadMin", threadMin);
        builder.addPropertyValue("consumeTimeOut", consumeTimeOut);
        registry.registerBeanDefinition("rocketMQListenerInitialization", builder.getRawBeanDefinition());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(EnvironmentManager.getProperty(env, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid()+"_PRODUCERGROUP"));
        producer.setSendMsgTimeout(sendMsgTimeOut);
        producer.setNamesrvAddr(StringUtils.isEmpty(nameServerAddress)?EnvironmentManager.getProperty(env, "rocketmq.nameServerAddress"):nameServerAddress);
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendAsyncFailed);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setCompressMsgBodyOverHowmuch(compressMsgBodyOverHowmuch);
        producer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStoreOK);
        producer.setVipChannelEnabled(false);
        //begin the producer
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("RocketMQ发送端启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        RocketMQTemplate proxyClass = null;
        try {
            proxyClass = InterceptorUtils.getProxyClass(RocketMQTemplate.class, new Class[]{DefaultMQProducer.class}, new Object[]{producer},"RocketMQ.producer");
        } catch (Exception e) {
            log.error("RocketMQ发送端Template模板启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        // 注册消息模板RocketMQTemplate
        beanFactory.registerSingleton("rocketmqTemplate", proxyClass);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }
}
