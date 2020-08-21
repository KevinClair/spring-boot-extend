package com.extend.mq.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.mq.annotation.EnableRocketMQTransaction;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @version 1.0
 * @ClassName EnableRocketMQImportSelector
 * @Description MQ选择器
 * @Author mingj
 * @Date 2020/1/31 22:11
 **/
public class EnableRocketMQTransactionImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRocketMQTransaction.class.getName()));

        RocketMQTransactionAutoConfiguration.nameServerAddress = annotationAttributes.getString("nameServerAddress");

        RocketMQTransactionAutoConfiguration.checkThreadPoolMinSize = annotationAttributes.getNumber("checkThreadPoolMinSize");
        RocketMQTransactionAutoConfiguration.checkThreadPoolMaxSize = annotationAttributes.getNumber("checkThreadPoolMaxSize");

        RocketMQTransactionAutoConfiguration.sendMsgTimeOut = annotationAttributes.getNumber("sendMsgTimeOut");
        RocketMQTransactionAutoConfiguration.retryTimesWhenSendFailed = annotationAttributes.getNumber("retryTimesWhenSendFailed");
        RocketMQTransactionAutoConfiguration.retryTimesWhenSendAsyncFailed = annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed");
        RocketMQTransactionAutoConfiguration.maxMessageSize = annotationAttributes.getNumber("maxMessageSize");
        RocketMQTransactionAutoConfiguration.compressMsgBodyOverHowmuch = annotationAttributes.getNumber("compressMsgBodyOverHowmuch");
        RocketMQTransactionAutoConfiguration.retryAnotherBrokerWhenNotStoreOK = annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK");

        return new String[]{RocketMQTransactionAutoConfiguration.class.getName()};
    }
}
