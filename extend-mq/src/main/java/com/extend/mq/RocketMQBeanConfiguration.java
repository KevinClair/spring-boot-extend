package com.extend.mq;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.InterceptorUtils;
import com.extend.mq.config.RocketMQListenerInitialization;
import com.extend.mq.property.RocketMQConfigurationProperty;
import com.extend.mq.template.RocketMQTemplate;
import com.extend.mq.template.RocketMQTransactionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * RocketMQ的Bean管理注入
 *
 * @author KevinClair
 */
@Slf4j
@ConditionalOnClass(value = {DefaultMQProducer.class, DefaultMQPushConsumer.class})
public class RocketMQBeanConfiguration implements EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Bean
    @ConfigurationProperties(prefix = "extend.mq")
    public RocketMQConfigurationProperty rocketMQConfigurationProperty() {
        return new RocketMQConfigurationProperty();
    }

    @Bean
    public RocketMQListenerInitialization rocketMQListenerInitialization(RocketMQConfigurationProperty property) {
        String defaultNameServerAddress = EnvironmentManager.getProperty(environment, "rocketmq.nameServerAddress");

        RocketMQListenerInitialization rocketMQListenerInitialization = new RocketMQListenerInitialization();
        rocketMQListenerInitialization.setNameServerAddress(StringUtils.isBlank(property.getNameServerAddress()) ? defaultNameServerAddress : property.getNameServerAddress());
        return rocketMQListenerInitialization;
    }

    @Bean
    public RocketMQTemplate rocketMQTemplate(RocketMQConfigurationProperty property) {
        DefaultMQProducer producer = null;
        if (StringUtils.isNotBlank(property.getAccessKey()) && StringUtils.isNotBlank(property.getSecretKey())) {
            producer = new DefaultMQProducer(new AclClientRPCHook(new SessionCredentials(property.getAccessKey(), property.getSecretKey())));
        } else {
            producer = new DefaultMQProducer();
        }
        producer.setProducerGroup(EnvironmentManager.getProperty(environment, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid() + "_PRODUCERGROUP"));
        producer.setSendMsgTimeout(property.getSendMsgTimeOut());
        producer.setNamesrvAddr(StringUtils.isBlank(property.getNameServerAddress()) ? EnvironmentManager.getProperty(environment, "rocketmq.nameServerAddress") : property.getNameServerAddress());
        producer.setRetryTimesWhenSendFailed(property.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(property.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(property.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(property.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(property.isRetryAnotherBrokerWhenNotStoreOK());
        producer.setVipChannelEnabled(false);
        //begin the producer
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("RocketMQ发送端启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        RocketMQTemplate proxyClass = null;
        try {
            proxyClass = InterceptorUtils.getProxyClass(RocketMQTemplate.class, new Class[]{DefaultMQProducer.class}, new Object[]{producer}, "RocketMQ.producer");
        } catch (Exception e) {
            log.error("RocketMQ发送端Template模板启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        // 注册消息模板RocketMQTemplate
        return proxyClass;
    }

    @Bean
    public RocketMQTransactionTemplate rocketMQTransactionTemplate(RocketMQConfigurationProperty property) {
        TransactionMQProducer producer = null;
        if (StringUtils.isNotBlank(property.getAccessKey()) && StringUtils.isNotBlank(property.getSecretKey())) {
            producer = new TransactionMQProducer(EnvironmentManager.getProperty(environment, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid() + "_TRANSACTIONPRODUCERGROUP"), new AclClientRPCHook(new SessionCredentials(property.getAccessKey(), property.getSecretKey())));
        } else {
            producer = new TransactionMQProducer(EnvironmentManager.getProperty(environment, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid() + "_TRANSACTIONPRODUCERGROUP"));
        }
        producer.setCheckThreadPoolMinSize(property.getCheckThreadPoolMinSize());
        producer.setCheckThreadPoolMaxSize(property.getCheckThreadPoolMaxSize());
        producer.setSendMsgTimeout(property.getSendMsgTimeOut());
        producer.setNamesrvAddr(StringUtils.isEmpty(property.getNameServerAddress()) ? EnvironmentManager.getProperty(environment, "rocketmq.nameServerAddress") : property.getNameServerAddress());
        producer.setRetryTimesWhenSendFailed(property.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(property.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(property.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(property.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(property.isRetryAnotherBrokerWhenNotStoreOK());

        //begin the producer
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("RocketMQ发送端启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        RocketMQTransactionTemplate proxyClass = null;
        try {
            proxyClass = InterceptorUtils.getProxyClass(RocketMQTransactionTemplate.class, new Class[]{TransactionMQProducer.class}, new Object[]{producer}, "RocketMQ.transactionProduce");
        } catch (Exception e) {
            log.error("RocketMQ发送端Template模板启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }

        return proxyClass;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
