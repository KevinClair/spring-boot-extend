package com.extend.log.config;

import com.extend.common.config.ConfigurationImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Des
 *
 * @author MingJ
 * @date 2020/8/14
 */
public class EnableLogImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
//        return new String[]{DynamicLogConfiguration.class.getName()};
        return new String[]{};
    }
}
