package com.extend.mq.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.mq.annotation.EnableRocketMQ;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @version 1.0
 * @ClassName EnableRocketMQImportSelector
 * @Description MQ选择器
 * @Author mingj
 * @Date 2020/1/31 22:11
 **/
public class EnableRocketMQImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRocketMQ.class.getName()));

        RocketMQAutoConfiguration.nameServerAddress = annotationAttributes.getString("nameServerAddress");

        RocketMQAutoConfiguration.sendMsgTimeOut = annotationAttributes.getNumber("sendMsgTimeOut");
        RocketMQAutoConfiguration.retryTimesWhenSendFailed = annotationAttributes.getNumber("retryTimesWhenSendFailed");
        RocketMQAutoConfiguration.retryTimesWhenSendAsyncFailed = annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed");
        RocketMQAutoConfiguration.maxMessageSize = annotationAttributes.getNumber("maxMessageSize");
        RocketMQAutoConfiguration.compressMsgBodyOverHowmuch = annotationAttributes.getNumber("compressMsgBodyOverHowmuch");
        RocketMQAutoConfiguration.retryAnotherBrokerWhenNotStoreOK = annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK");

        RocketMQAutoConfiguration.threadMin = annotationAttributes.getNumber("consumeThreadMin");
        RocketMQAutoConfiguration.threadMax = annotationAttributes.getNumber("consumeThreadMax");
        RocketMQAutoConfiguration.consumeTimeOut = annotationAttributes.getNumber("consumeTimeout");
        return new String[]{RocketMQAutoConfiguration.class.getName()};
    }
}
