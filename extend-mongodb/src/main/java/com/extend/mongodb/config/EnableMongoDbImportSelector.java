package com.extend.mongodb.config;

import com.extend.common.config.ConfigurationImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @version 1.0
 * @ClassName EnableMongoDbImportSelector
 * @Description TODO描述
 * @Author mingj
 * @Date 2019/10/31 14:31
 **/
public class EnableMongoDbImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        return new String[]{MongoDbDataSourceConfig.class.getName()};
    }
}
