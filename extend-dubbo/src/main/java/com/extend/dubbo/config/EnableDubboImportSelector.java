package com.extend.dubbo.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.common.constant.EnvironmentManager;
import com.extend.dubbo.annotation.EnableDubboConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * @version 1.0
 * @ClassName EnableDubboConfigurationImportSelector
 * @Description dubbo服务配置
 * @Author mingj
 * @Date 2019/12/16 23:00
 **/
public class EnableDubboImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableDubboConfiguration.class.getName()));
        String port = attributes.getString("port");
        String protocol = attributes.getString("protocol");
        String packageName = attributes.getString("scanPackageName");
        if(!StringUtils.isEmpty(port)) {
            EnvironmentManager.setProperty(EnvironmentManager.DUBBO__PROTOCOL_PORT, port);
        }
        if(!StringUtils.isEmpty(protocol)) {
            EnvironmentManager.setProperty(EnvironmentManager.DUBBO_PROTOCOL_NAME, protocol);
        }
        if (!StringUtils.isEmpty(packageName)){
            EnvironmentManager.setProperty(EnvironmentManager.DUBBO_SCAN_PACKNAME, packageName);
        }
        return new String[]{DubboAutoConfiguration.class.getName()};
    }
}
