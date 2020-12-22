package com.extend.mq.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.mq.annotation.EnableRocketMQ;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * EnableRocketMQImportSelector。
 *
 * @author KevinClair
 */
public class EnableRocketMQImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableRocketMQ.class.getName()));
        RocketMQAutoConfiguration configuration = RocketMQAutoConfiguration.builder()
                .nameServerAddress(annotationAttributes.getString("nameServerAddress"))
                .sendMsgTimeOut(annotationAttributes.getNumber("sendMsgTimeOut"))
                .retryTimesWhenSendFailed(annotationAttributes.getNumber("retryTimesWhenSendFailed"))
                .retryTimesWhenSendAsyncFailed(annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed"))
                .maxMessageSize(annotationAttributes.getNumber("maxMessageSize"))
                .compressMsgBodyOverHowmuch(annotationAttributes.getNumber("compressMsgBodyOverHowmuch"))
                .retryAnotherBrokerWhenNotStoreOK(annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK"))
                .threadMin(annotationAttributes.getNumber("consumeThreadMin"))
                .threadMax(annotationAttributes.getNumber("consumeThreadMax"))
                .consumeTimeOut(annotationAttributes.getNumber("consumeTimeout"))
                .build();
        return new String[]{configuration.getClass().getName()};
    }
}
