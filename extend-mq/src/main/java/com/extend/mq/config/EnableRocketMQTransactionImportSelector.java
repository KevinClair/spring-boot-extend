package com.extend.mq.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.mq.annotation.EnableRocketMQTransaction;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * EnableRocketMQTransactionImportSelector。
 *
 * @author KevinClair
 */
public class EnableRocketMQTransactionImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRocketMQTransaction.class.getName()));

        RocketMQTransactionAutoConfiguration configuration = RocketMQTransactionAutoConfiguration.builder()
                .nameServerAddress(annotationAttributes.getString("nameServerAddress"))
                .sendMsgTimeOut(annotationAttributes.getNumber("sendMsgTimeOut"))
                .retryTimesWhenSendFailed(annotationAttributes.getNumber("retryTimesWhenSendFailed"))
                .retryTimesWhenSendAsyncFailed(annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed"))
                .maxMessageSize(annotationAttributes.getNumber("maxMessageSize"))
                .compressMsgBodyOverHowmuch(annotationAttributes.getNumber("compressMsgBodyOverHowmuch"))
                .retryAnotherBrokerWhenNotStoreOK(annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK"))
                .checkThreadPoolMaxSize(annotationAttributes.getNumber("checkThreadPoolMaxSize"))
                .checkThreadPoolMinSize(annotationAttributes.getNumber("checkThreadPoolMinSize"))
                .build();

        return new String[]{configuration.getClass().getName()};
    }
}
