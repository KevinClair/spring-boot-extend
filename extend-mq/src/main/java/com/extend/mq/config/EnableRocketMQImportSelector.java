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
        RocketMQAutoConfiguration configuration = new RocketMQAutoConfiguration(null,
                annotationAttributes.getString("nameServerAddress"),
                annotationAttributes.getNumber("sendMsgTimeOut"),
                annotationAttributes.getNumber("retryTimesWhenSendFailed"),
                annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed"),
                annotationAttributes.getNumber("maxMessageSize"),
                annotationAttributes.getNumber("compressMsgBodyOverHowmuch"),
                annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK"));
        return new String[]{configuration.getClass().getName()};
    }
}
