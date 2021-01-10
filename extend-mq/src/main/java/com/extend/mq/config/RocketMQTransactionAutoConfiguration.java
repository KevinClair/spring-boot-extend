package com.extend.mq.config;

import com.extend.common.constant.EnvironmentManager;
import com.extend.common.utils.InterceptorUtils;
import com.extend.mq.template.RocketMQTransactionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * RocketMQTransactionAutoConfiguration，事务消息配置类。
 *
 * @author KevinClair
 */
@Slf4j
public class RocketMQTransactionAutoConfiguration implements EnvironmentAware {

    private ConfigurableEnvironment env;
    private String nameServerAddress;
    private int checkThreadPoolMinSize;
    private int checkThreadPoolMaxSize;
    private int sendMsgTimeOut;
    private int retryTimesWhenSendFailed;
    private int retryTimesWhenSendAsyncFailed;
    private int maxMessageSize;
    private int compressMsgBodyOverHowmuch;
    private boolean retryAnotherBrokerWhenNotStoreOK;

    public RocketMQTransactionAutoConfiguration(ConfigurableEnvironment env, String nameServerAddress, int checkThreadPoolMinSize, int checkThreadPoolMaxSize, int sendMsgTimeOut, int retryTimesWhenSendFailed, int retryTimesWhenSendAsyncFailed, int maxMessageSize, int compressMsgBodyOverHowmuch, boolean retryAnotherBrokerWhenNotStoreOK) {
        this.nameServerAddress = nameServerAddress;
        this.checkThreadPoolMinSize = checkThreadPoolMinSize;
        this.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
        this.sendMsgTimeOut = sendMsgTimeOut;
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
        this.maxMessageSize = maxMessageSize;
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

    public RocketMQTransactionAutoConfiguration() {
    }

    @Bean
    public RocketMQTransactionTemplate rocketMQTransactionTemplate() {
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setProducerGroup(EnvironmentManager.getProperty(env, "rocketmq.producer.producerGroup", EnvironmentManager.getAppid()+"_TRANSACTIONPRODUCERGROUP"));
        producer.setCheckThreadPoolMinSize(checkThreadPoolMinSize);
        producer.setCheckThreadPoolMaxSize(checkThreadPoolMaxSize);
        producer.setSendMsgTimeout(sendMsgTimeOut);
        producer.setNamesrvAddr(StringUtils.isEmpty(nameServerAddress)?EnvironmentManager.getProperty(env, "rocketmq.nameServerAddress"):nameServerAddress);
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendAsyncFailed);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setCompressMsgBodyOverHowmuch(compressMsgBodyOverHowmuch);
        producer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStoreOK);

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
        this.env = (ConfigurableEnvironment) environment;
    }
}
