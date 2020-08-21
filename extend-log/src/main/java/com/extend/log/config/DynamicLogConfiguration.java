package com.extend.log.config;

import org.springframework.context.annotation.Bean;

/**
 * 动态日志配置类
 *
 * @author MingJ
 * @date 2020/8/14
 */
public class DynamicLogConfiguration {

    @Bean
    public DynamicLogChangeEventListener dynamicLogChangeEventListener(){
        return new DynamicLogChangeEventListener();
    }
}
