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

        RocketMQTransactionAutoConfiguration configuration = new RocketMQTransactionAutoConfiguration(null,
                annotationAttributes.getString("nameServerAddress"),
                annotationAttributes.getNumber("checkThreadPoolMinSize"),
                annotationAttributes.getNumber("checkThreadPoolMaxSize"),
                annotationAttributes.getNumber("sendMsgTimeOut"),
                annotationAttributes.getNumber("retryTimesWhenSendFailed"),
                annotationAttributes.getNumber("retryTimesWhenSendAsyncFailed"),
                annotationAttributes.getNumber("maxMessageSize") ,
                annotationAttributes.getNumber("compressMsgBodyOverHowmuch"),
                annotationAttributes.getBoolean("retryAnotherBrokerWhenNotStoreOK"));
        return new String[]{configuration.getClass().getName()};
    }
}
