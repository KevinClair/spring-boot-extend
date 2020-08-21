package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

/**
 * @version 1.0
 * @ClassName ApplicationPreparedEventListener
 * @Description TODO描述
 * @Author mingj
 * @Date 2019/12/30 17:38
 **/
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationPreparedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        if (StringUtils.isEmpty(System.getProperty("env"))){
            System.setProperty("env", "dev");
            logger.info("No system environment set, falling back to default environment: dev");
        }
        if (StringUtils.isEmpty(System.getProperty("spring.profiles.active"))){
            System.setProperty("spring.profiles.active", "dev");
            logger.info("No profiles set, falling back to default profiles: dev");
        }
        logger.info("ApplicationPreparedEvent...");
    }
}