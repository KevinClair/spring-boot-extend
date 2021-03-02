package com.extend.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工具类
 *
 * @author KevinClair
 */
public enum SingletonUtils {

    INST;

    private static final Map<String, Object> singletonMap = new ConcurrentHashMap<String, Object>();

    /**
     * create single object.
     *
     * @param clazz the clazz
     * @param o     the o
     */
    public void single(final Class clazz, final Object o) {
        singletonMap.put(clazz.getName(), o);
    }

    /**
     * Get object.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the t
     */
    public <T> T get(final Class<T> clazz) {
        return (T) singletonMap.get(clazz.getName());
    }
}
