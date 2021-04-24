package com.extend.cache.redis;

import com.extend.cache.serializer.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

/**
 * Redis配置注入
 *
 * @author KevinClair
 **/
@Configuration
@EnableCaching
@AutoConfigureBefore(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class RedisAutoConfiguration {

    private ConfigurableEnvironment environment;

    public RedisAutoConfiguration(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    /**
     * 注册RedisTemplate
     *
     * @param redisConnectionFactory redis连接工厂
     * @return {@link RedisTemplate}
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        FastJsonRedisSerializer<Object> redisSerializer = new FastJsonRedisSerializer<>(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer);

        template.afterPropertiesSet();
        // 关闭事务
        template.setEnableTransactionSupport(false);
        return template;
    }

    /**
     * 注册StringRedisTemplate
     *
     * @param redisConnectionFactory redis连接工厂
     * @return {@link StringRedisTemplate}
     */
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        stringRedisTemplate.setEnableTransactionSupport(false);
        return stringRedisTemplate;
    }

    /**
     * 缓存管理器
     *
     * @param factory redis连接工厂
     * @return {@link CacheManager}
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)).computePrefixWith(cacheKeyPrefix());
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory)).cacheDefaults(config).build();
    }

    /**
     * 缓存key生成器
     *
     * @return {@link KeyGenerator}
     */
    @Bean
    public KeyGenerator keyGenerator(){
        return  (o, method, objects) -> {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(o.getClass().getName()).append("#").append(method.getName()).append("@");
            for (Object object : objects) {
                stringBuilder.append(object.toString());
            }
            return stringBuilder.toString();
        };
    }

    /**
     * 缓存Key，重置生成redis缓存时的目录结构
     *
     * @return
     */
    @Bean
    public CacheKeyPrefix cacheKeyPrefix(){
        return cacheName -> {
          StringBuilder cachekey = new StringBuilder();
          return cachekey.append(environment.getProperty("spring.application.name")).append(":").append(cacheName).append(":").toString();
        };
    }
}
