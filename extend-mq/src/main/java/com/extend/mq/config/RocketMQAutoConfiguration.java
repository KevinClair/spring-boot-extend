package com.extend.mq.config;

import com.extend.common.constant.EnvironmentManager;
import com.extend.common.utils.InterceptorUtils;
import com.extend.mq.template.RocketMQTemplate;
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
 * @version 1.0
 * @ClassName RocketMQAutoConfiguration
 * @Description MQ自动配置
 * @Author mingj
 * @Date 2020/1/31 22:13
 **/
@Slf4j
public class RocketMQAutoConfiguration implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    private ConfigurableEnvironment env;
    public static String nameServerAddress;
    public static int sendMsgTimeOut;
    public static int retryTimesWhenSendFailed;
    public static int retryTimesWhenSendAsyncFailed;
    public static int maxMessageSize;
    public static int compressMsgBodyOverHowmuch;
    public static boolean retryAnotherBrokerWhenNotStoreOK;
    public static int threadMin;
    public static int threadMax;
    public static long consumeTimeOut;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 注册RocketMQListenerInitialization
        registerRocketMQListenerInitialization(registry);
    }

    /**
    *@Description 注册RocketMQListenerInitialization
    *@Param [registry]
    *@Author mingj
    *@Date 2020/1/31 22:27
    *@Return void
    **/
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
