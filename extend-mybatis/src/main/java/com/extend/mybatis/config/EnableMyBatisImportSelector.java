package com.extend.mybatis.config;

import com.extend.common.config.ConfigurationImportSelector;
import com.extend.mybatis.aop.TransactionalManagementInterceptor;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @version 1.0
 * @ClassName EnableMyBatisImportSelector
 * @Description 开启功能模块选择器
 * @Author mingj
 * @Date 2019/9/22 22:55
 **/
public class EnableMyBatisImportSelector extends ConfigurationImportSelector {

    @Override
    public String[] importSelect(AnnotationMetadata importingClassMetadata) {
        return new String[]{MyBatisAutoConfiguration.class.getName(), TransactionalManagementInterceptor.class.getName()};
    }
}
