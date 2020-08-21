package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @version 1.0
 * @ClassName ApplicationStartedEventListener
 * @Description TODO描述
 * @Author mingj
 * @Date 2019/12/30 17:39
 **/
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartedEventListener.class);


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("ApplicationStartedEvent...");
    }
}
