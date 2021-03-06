package com.extend.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * ApplicationCloseEventListener。
 *
 * @author KevinClair
 */
public class ApplicationCloseEventListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationCloseEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {
        log.info("Application has been closed");
    }
}
