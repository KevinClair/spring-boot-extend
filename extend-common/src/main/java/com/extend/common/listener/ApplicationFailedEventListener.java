package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @version 1.0
 * @ClassName ApplicationFailedEventListener
 * @Description 应用加载失败监听
 * @Author mingj
 * @Date 2019/12/30 17:37
 **/
public class ApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationFailedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        logger.info("The application failed to load. Please check the status of the application");
    }
}