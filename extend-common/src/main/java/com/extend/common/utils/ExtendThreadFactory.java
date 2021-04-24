package com.extend.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工厂
 *
 * @author KevinClair
 */
public class ExtendThreadFactory implements ThreadFactory {


    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;


    public ExtendThreadFactory(String name) {

        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        if (StringUtils.isBlank(name)) {
            name = "pool";
        }
        namePrefix = "extend-works-" + poolNumber.getAndIncrement() + "-thread-" + name + "-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
