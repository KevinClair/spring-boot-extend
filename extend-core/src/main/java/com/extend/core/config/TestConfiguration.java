package com.extend.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * TODO
 *
 * @author KevinClair
 **/
@PropertySource(value = {"classpath:test.properties"})
@ConfigurationProperties(prefix = "test")
@Configuration
public class TestConfiguration {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
