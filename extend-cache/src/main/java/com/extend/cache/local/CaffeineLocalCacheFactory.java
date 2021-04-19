package com.extend.cache.local;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * 本地缓存工厂
 *
 * @author KevinClair
 */
public class CaffeineLocalCacheFactory {

    // 本地缓存
    private final LoadingCache<String, Object> cache = Caffeine.newBuilder()
            .initialCapacity(10)
            .maximumSize(50000).build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    return null;
                }
            });

    private CaffeineLocalCacheFactory() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static CaffeineLocalCacheFactory getInstance(){
        return CaffeineLocalCacheFactoryInstance.INSTANCE;
    }


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public Object getCache(String key) {
        return cache.get(key);
    }

    /**
     * 放置缓存
     *
     * @param key
     * @param object
     */
    public void putCache(String key, Object object){
        cache.put(key, object);
    }

    /**
     * get instance
     */
    static class CaffeineLocalCacheFactoryInstance{
        static final CaffeineLocalCacheFactory INSTANCE= new CaffeineLocalCacheFactory();
    }
}
