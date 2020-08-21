package com.extend.mq.config;

import com.extend.common.constant.EnvironmentManager;
import com.extend.common.utils.InterceptorUtils;
import com.extend.mq.template.RocketMQTransactionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * @version 1.0
 * @ClassName RocketMQAutoConfiguration
 * @Description MQ事务消息自动配置
 * @Author mingj
 * @Date 2020/1/31 22:13
 **/
@Slf4j
public class RocketMQTransactionAutoConfiguration implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    private ConfigurableEnvironment env;
    public static String nameServerAddress;
    public static int checkThreadPoolMinSize;
    public static int checkThreadPoolMaxSize;
    public static int sendMsgTimeOut;
    public static int retryTimesWhenSendFailed;
    public static int retryTimesWhenSendAsyncFailed;
    public static int maxMessageSize;
    public static int compressMsgBodyOverHowmuch;
    public static boolean retryAnotherBrokerWhenNotStoreOK;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
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

        beanFactory.registerSingleton("rocketmqTransactionTemplate", proxyClass);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }
}
