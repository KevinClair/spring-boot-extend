package com.extend.mq.config;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.InterceptorUtils;
import com.extend.mq.template.RocketMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * RocketMQAutoConfiguration，配置类。
 *
 * @author KevinClair
 */
@Slf4j
public class RocketMQAutoConfiguration implements EnvironmentAware {

    private ConfigurableEnvironment env;
    private String nameServerAddress;
    private int sendMsgTimeOut;
    private int retryTimesWhenSendFailed;
    private int retryTimesWhenSendAsyncFailed;
    private int maxMessageSize;
    private int compressMsgBodyOverHowmuch;
    private boolean retryAnotherBrokerWhenNotStoreOK;

    public RocketMQAutoConfiguration(ConfigurableEnvironment env, String nameServerAddress, int sendMsgTimeOut, int retryTimesWhenSendFailed, int retryTimesWhenSendAsyncFailed, int maxMessageSize, int compressMsgBodyOverHowmuch, boolean retryAnotherBrokerWhenNotStoreOK) {
        this.nameServerAddress = nameServerAddress;
        this.sendMsgTimeOut = sendMsgTimeOut;
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
        this.maxMessageSize = maxMessageSize;
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

    public RocketMQAutoConfiguration() {
    }

    @Bean
    public RocketMQListenerInitialization rocketMQListenerInitialization(){
        String defaultNameServerAddress = EnvironmentManager.getProperty(env, "rocketmq.nameServerAddress");

        RocketMQListenerInitialization rocketMQListenerInitialization = new RocketMQListenerInitialization();
        rocketMQListenerInitialization.setNameServerAddress(StringUtils.isBlank(nameServerAddress) ? defaultNameServerAddress : nameServerAddress);
        return rocketMQListenerInitialization;
    }

    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(EnvironmentManager.getProperty(env, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid() + "_PRODUCERGROUP"));
        producer.setSendMsgTimeout(sendMsgTimeOut);
        producer.setNamesrvAddr(StringUtils.isBlank(nameServerAddress) ? EnvironmentManager.getProperty(env, "rocketmq.nameServerAddress") : nameServerAddress);
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
            proxyClass = InterceptorUtils.getProxyClass(RocketMQTemplate.class, new Class[]{DefaultMQProducer.class}, new Object[]{producer}, "RocketMQ.producer");
        } catch (Exception e) {
            log.error("RocketMQ发送端Template模板启动异常，异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
        // 注册消息模板RocketMQTemplate
        return proxyClass;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }
}
