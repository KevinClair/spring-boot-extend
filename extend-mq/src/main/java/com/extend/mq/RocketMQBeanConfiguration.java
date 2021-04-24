package com.extend.mq;

import com.extend.common.utils.ExtendThreadFactory;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RocketMQ的Bean管理注入
 *
 * @author KevinClair
 */
@Slf4j
@ConditionalOnClass(value = {DefaultMQProducer.class, DefaultMQPushConsumer.class})
public class RocketMQBeanConfiguration {

    private ConfigurableEnvironment environment;

    public RocketMQBeanConfiguration(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Bean
    @ConfigurationProperties(prefix = "extend.mq")
    @ConditionalOnProperty(value = "extend.mq.nameServerAddress")
    public RocketMQConfigurationProperty rocketMQConfigurationProperty() {
        return new RocketMQConfigurationProperty();
    }

    @Bean
    @ConditionalOnBean(value = {RocketMQConfigurationProperty.class})
    public RocketMQListenerInitialization rocketMQListenerInitialization(RocketMQConfigurationProperty property) {
        RocketMQListenerInitialization rocketMQListenerInitialization = new RocketMQListenerInitialization();
        rocketMQListenerInitialization.setNameServerAddress(property.getNameServerAddress());
        return rocketMQListenerInitialization;
    }

    @Bean
    @ConditionalOnBean(value = {RocketMQConfigurationProperty.class})
    public RocketMQTemplate rocketMQTemplate(RocketMQConfigurationProperty property) {
        DefaultMQProducer producer = null;
        if (StringUtils.isNotBlank(property.getAccessKey()) && StringUtils.isNotBlank(property.getSecretKey())) {
            producer = new DefaultMQProducer(new AclClientRPCHook(new SessionCredentials(property.getAccessKey(), property.getSecretKey())));
        } else {
            producer = new DefaultMQProducer();
        }
        producer.setProducerGroup(StringUtils.isBlank(property.getProducerGroup()) ? (EnvironmentManager.getAppId() + "_PRODUCERGROUP") : property.getProducerGroup());
        producer.setSendMsgTimeout(property.getSendMsgTimeOut());
        producer.setNamesrvAddr(property.getNameServerAddress());
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
    @ConditionalOnBean(value = {RocketMQConfigurationProperty.class})
    public RocketMQTransactionTemplate rocketMQTransactionTemplate(RocketMQConfigurationProperty property) {
        TransactionMQProducer producer = null;
        if (StringUtils.isNotBlank(property.getAccessKey()) && StringUtils.isNotBlank(property.getSecretKey())) {
            producer = new TransactionMQProducer(StringUtils.isBlank(property.getProducerGroup()) ? (EnvironmentManager.getAppId() + "_PRODUCERGROUP") : property.getProducerGroup(), new AclClientRPCHook(new SessionCredentials(property.getAccessKey(), property.getSecretKey())));
        } else {
            producer = new TransactionMQProducer(StringUtils.isBlank(property.getProducerGroup()) ? (EnvironmentManager.getAppId() + "_PRODUCERGROUP") : property.getProducerGroup());
        }
        producer.setSendMsgTimeout(property.getSendMsgTimeOut());
        producer.setNamesrvAddr(property.getNameServerAddress());
        producer.setRetryTimesWhenSendFailed(property.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(property.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(property.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(property.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(property.isRetryAnotherBrokerWhenNotStoreOK());

        // 设置事务消息回查线程
        ExecutorService executorService = new ThreadPoolExecutor(property.getCheckThreadPoolCoreSize(), property.getMaxMessageSize(), 3000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new ExtendThreadFactory("TransactionMQ"));
        producer.setExecutorService(executorService);

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
}
