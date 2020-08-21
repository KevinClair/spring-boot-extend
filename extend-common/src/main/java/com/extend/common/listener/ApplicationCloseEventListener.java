package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @version 1.0
 * @ClassName ApplicationCloseEventListener
 * @Description TODO描述
 * @Author mingj
 * @Date 2019/12/31 15:58
 **/
public class ApplicationCloseEventListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationCloseEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Application has been closed");
    }
}
