package com.extend.log.config;

import com.extend.core.config.ConfigurationImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Des
 *
 * @author mingj
 * @date 2020/8/14
 */
public class EnableLogImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        return new String[]{DynamicLogConfiguration.class.getName()};
    }
}
