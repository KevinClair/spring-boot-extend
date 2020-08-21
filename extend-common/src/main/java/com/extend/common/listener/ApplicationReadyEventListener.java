package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @version 1.0
 * @ClassName ApplicationReadyEventListener
 * @Description 应用加载完成监听器
 * @Author mingj
 * @Date 2019/12/31 14:29
 **/
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Application loaded successfully");
    }
}
