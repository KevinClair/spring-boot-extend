package com.extend.core.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * ConfigurationImportSelector.
 *
 * @author KevinClair
 */
public abstract class ConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        EnableType enableType = EnableType.getTypeByValue(this.getClass().getSimpleName());
        if (enableType != null && isEnable(enableType)) {
            return importSelect(annotationMetadata);
        }
        return new String[]{};
    }

    public abstract String[] importSelect(AnnotationMetadata importingClassMetadata);


    /**
     * 注解类型
     */
    enum EnableType {
        ENABLE_MONGODB_CONFIGURATION("EnableMongoDbImportSelector","EnableMongoDbConfiguration"),
        ENABLE_ROCKETMQ_CONFIGURATION("EnableRocketMQImportSelector", "EnableRocketMQ"),
        ENABLE_ROCKETMQ_TRANSACTION_CONFIGURATION("EnableRocketMQTransactionImportSelector", "EnableRocketMQTransaction"),
        ENABLE_LOG_CONFIGURATION("EnableLogImportSelector", "EnableLog");

        private final String value;

        private final String param;


        EnableType(String value, String param) {
            this.value = value;
            this.param = param;
        }

        public String getValue() {
            return value;
        }

        public String getParam() {
            return param;
        }

        public static  EnableType getTypeByValue(String value){
            EnableType[] types = values();
            for (EnableType type : types) {
                if (type.getValue().equals(value)){
                    return type;
                }
            }
            return null;
        }
    }


    /**
     *  判断选择的注解类型是否被选中
     *
     * @param enableType 开启的注解类型
     * @return boolean
     */
    private boolean isEnable(EnableType enableType) {
        try {
            return StringUtils.isEmpty(System.getProperty(enableType.getParam())) || Boolean.parseBoolean(System.getProperty(enableType.getParam()));
        } catch (Exception e) {
            return true;
        }

    }
}
