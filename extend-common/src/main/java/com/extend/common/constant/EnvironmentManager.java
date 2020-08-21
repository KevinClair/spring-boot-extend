package com.extend.common.constant;

import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @version 1.0
 * @ClassName EnvironmentManager
 * @Description 环境参数配置类
 * @Author mingj
 * @Date 2019/9/28 21:43
 **/
public class EnvironmentManager {

    //mybatis配置
    public static final String MYBATIS_CONFIG_NAME = "mybatis.config.name";
    public static final String MYBATIS_CONFIG_BASEPACKAGE = "mybatis.config.basePackage";
    public static final String MYBATIS_CONFIG_DRIVERCLASSNAME = "mybatis.config.driverClassName";
    public static final String MYBATIS_CONFIG_JDBCURL = "mybatis.config.jdbcUrl";
    public static final String MYBATIS_CONFIG_USERNAME = "mybatis.config.username";
    public static final String MYBATIS_CONFIG_PASSWORD = "mybatis.config.password";
    public static final String MYBATIS_CONFIG_READONLY = "mybatis.config.readOnly";
    public static final String MYBATIS_CONFIG_CONNECTIONTIMEOUT = "mybatis.config.connectionTimeout";
    public static final String MYBATIS_CONFIG_IDLETIMEOUT = "mybatis.config.idleTimeout";
    public static final String MYBATIS_CONFIG_MAXLIFETIME = "mybatis.config.maxLifetime";
    public static final String MYBATIS_CONFIG_MAXIMUMPOOLSIZE = "mybatis.config.maximumPoolSize";
    public static final String MYBATIS_CONFIG_MAPPERLOCATIONS = "mybatis.config.mapperLocations";
    public static final String MYBATIS_CONFIG_MAPPER_ANNOTATION_CLASS = "mybatis.config.mapperAnnotationClass";
    public static final String MYBATIS_CONFIG_TYPEALIASEPACKAGE = "mybatis.config.typeAliasesPackage";
    public static final String MYBATIS_CONFIG_MARKERINTERFACE = "mybatis.config.markerInterface";
    public static final String MYBATIS_CONFIG_DEFAULTSTATEMENTTIMEOUT = "mybatis.config.defaultStatementTimeoutInSecond";
    public static final String MYBATIS_CONFIG_CONFIGLOCATION = "mybatis.config.configLocation";
    public static final String MYBATIS_CONFIG_MAPUNDERSCORETOCAMELCASE = "mybatis.config.mapUnderscoreToCamelCase";
    public static final String MYBATIS_CONFIG_CONNECTIONINITSQL = "mybatis.configs.connectionInitSql";
    //mybatis多库配置
    public static final String MYBATIS_CONFIGS_NAME = "mybatis.configs[%s].name";
    public static final String MYBATIS_CONFIGS_BASEPACKAGE = "mybatis.configs[%s].basePackage";
    public static final String MYBATIS_CONFIGS_DRIVERCLASSNAME = "mybatis.configs[%s].driverClassName";
    public static final String MYBATIS_CONFIGS_JDBCURL = "mybatis.configs[%s].jdbcUrl";
    public static final String MYBATIS_CONFIGS_USERNAME = "mybatis.configs[%s].username";
    public static final String MYBATIS_CONFIGS_PASSWORD = "mybatis.configs[%s].password";
    public static final String MYBATIS_CONFIGS_READONLY = "mybatis.configs[%s].readOnly";
    public static final String MYBATIS_CONFIGS_CONNECTIONTIMEOUT = "mybatis.configs[%s].connectionTimeout";
    public static final String MYBATIS_CONFIGS_IDLETIMEOUT = "mybatis.configs[%s].idleTimeout";
    public static final String MYBATIS_CONFIGS_MAXLIFETIME = "mybatis.configs[%s].maxLifetime";
    public static final String MYBATIS_CONFIGS_MAXIMUMPOOLSIZE = "mybatis.configs[%s].maximumPoolSize";
    public static final String MYBATIS_CONFIGS_MAPPERLOCATIONS = "mybatis.configs[%s].mapperLocations";
    public static final String MYBATIS_CONFIGS_TYPEALIASEPACKAGE = "mybatis.configs[%s].typeAliasesPackage";
    public static final String MYBATIS_CONFIGS_MARKERINTERFACE = "mybatis.configs[%s].markerInterface";
    public static final String MYBATIS_CONFIGS_DEFAULTSTATEMENTTIMEOUT = "mybatis.config[%s].defaultStatementTimeoutInSecond";
    public static final String MYBATIS_CONFIGS_CONFIGLOCATION = "mybatis.configs[%s].configLocation";
    public static final String MYBATIS_CONFIGS_MAPUNDERSCORETOCAMELCASE = "mybatis.configs[%s].mapUnderscoreToCamelCase";
    public static final String MYBATIS_CONFIGS_CONNECTIONINITSQL = "mybatis.configs[%s].connectionInitSql";

    //mongodb的配置key
    public static final String MONGODB_CONFIG_NAME = "mongodb.config.name";
    public static final String MONGODB_CONFIG_REPOSITORIESENABLED = "mongodb.config.repositoriesEnabled";
    public static final String MONGODB_CONFIG_REPOSITORIESLOCATIONS = "mongodb.config.repositoriesLocations";
    public static final String MONGODB_CONFIG_REPOSITORIESAUTHENTICATIONDATABASE = "mongodb.config.repositoriesAuthenticationDatabase";
    public static final String MONGODB_CONFIG_MONGODB_URI = "mongodb.config.mongodbUri";
    public static final String MONGODB_CONFIG_MINCONNECTIONSPERHOST = "mongodb.config.minConnectionsPerHost";
    public static final String MONGODB_CONFIG_CONNECTIONSPERHOST = "mongodb.config.connectionsPerHost";
    public static final String MONGODB_CONFIG_CONNECTTIMEOUT = "mongodb.config.connectTimeout";
    public static final String MONGODB_CONFIG_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER = "mongodb.config.threadsAllowedToBlockForConnectionMultiplier";
    public static final String MONGODB_CONFIG_MAXCONNECTIONIDLETIME = "mongodb.config.maxConnectionIdleTime";
    public static final String MONGODB_CONFIG_MAXCONNECTIONLIFETIME = "mongodb.config.maxConnectionLifeTime";
    public static final String MONGODB_CONFIG_SOCKETTIMEOUT = "mongodb.config.socketTimeout";
    public static final String MONGODB_CONFIG_MAXWAITTIME = "mongodb.config.maxWaitTime";
    public static final String MONGODB_CONFIG_SOCKETKEEPALIVE = "mongodb.config.socketKeepAlive";
    public static final String MONGODB_CONFIG_SERVERSELECTIONTIMEOUT = "mongodb.config.serverSelectionTimeout";

    //mongodb的配置key(多库配置)
    public static final String MONGODB_CONFIGS_NAME = "mongodb.configs[%s].name";
    public static final String MONGODB_CONFIGS_REPOSITORIESENABLED = "mongodb.configs[%s].repositoriesEnabled";
    public static final String MONGODB_CONFIGS_REPOSITORIESLOCATIONS = "mongodb.configs[%s].repositoriesLocations";
    public static final String MONGODB_CONFIGS_REPOSITORIESAUTHENTICATIONDATABASE = "mongodb.configs[%s].repositoriesAuthenticationDatabase";
    public static final String MONGODB_CONFIGS_MONGODB_URI = "mongodb.configs[%s].mongodbUri";
    public static final String MONGODB_CONFIGS_MINCONNECTIONSPERHOST = "mongodb.configs[%s].minConnectionsPerHost";
    public static final String MONGODB_CONFIGS_CONNECTIONSPERHOST = "mongodb.configs[%s].connectionsPerHost";
    public static final String MONGODB_CONFIGS_CONNECTTIMEOUT = "mongodb.configs[%s].connectTimeout";
    public static final String MONGODB_CONFIGS_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER = "mongodb.configs[%s].threadsAllowedToBlockForConnectionMultiplier";
    public static final String MONGODB_CONFIGS_MAXCONNECTIONIDLETIME = "mongodb.configs[%s].maxConnectionIdleTime";
    public static final String MONGODB_CONFIGS_MAXCONNECTIONLIFETIME = "mongodb.configs[%s].maxConnectionLifeTime";
    public static final String MONGODB_CONFIGS_SOCKETTIMEOUT = "mongodb.configs[%s].socketTimeout";
    public static final String MONGODB_CONFIGS_MAXWAITTIME = "mongodb.configs[%s].maxWaitTime";
    public static final String MONGODB_CONFIGS_SOCKETKEEPALIVE = "mongodb.configs[%s].socketKeepAlive";
    public static final String MONGODB_CONFIGS_SERVERSELECTIONTIMEOUT = "mongodb.configs[%s].serverSelectionTimeout";

    //mongodb的默认配置
    public static final Integer MONGODB_DEFAULT_CONFIG_MINCONNECTIONSPERHOST = 30;
    public static final Integer MONGODB_DEFAULT_CONFIG_CONNECTIONSPERHOST = 50;
    public static final Integer MONGODB_DEFAULT_CONFIG_CONNECTTIMEOUT = 10 * 1000;
    public static final Integer MONGODB_DEFAULT_CONFIG_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER = 10;
    public static final Integer MONGODB_DEFAULT_CONFIG_MAXCONNECTIONIDLETIME = 60 * 60 * 1000;
    public static final Integer MONGODB_DEFAULT_CONFIG_MAXCONNECTIONLIFETIME = 6 * 60 * 60 * 1000;
    public static final Integer MONGODB_DEFAULT_CONFIG_SOCKETTIMEOUT = 10 * 1000;
    public static final Integer MONGODB_DEFAULT_CONFIG_MAXWAITTIME = 10 * 1000;
    public static final Boolean MONGODB_DEFAULT_CONFIG_SOCKETKEEPALIVE = true;
    public static final Integer MONGODB_DEFAULT_CONFIG_SERVERSELECTIONTIMEOUT = 1000 * 300;
    public static final Boolean MONGODB_DEFAULT_CONFIG_REPOSITORIESENABLED = true;

    //dubbo服务配置
    public static final String DUBBO_PROTOCOL_NAME = "dubbo.protocol.name";
    public static final String DUBBO__PROTOCOL_PORT = "dubbo.protocol.port";
    public static final String DUBBO_APPLICATION_LOGGER = "dubbo.application.logger";
    public static final String DUBBO_PROVIDER_TIMEOUT = "dubbo.provider.timeout";
    public static final String DUBBO_PROVIDER_RETRIES = "dubbo.provider.retries";
    public static final String DUBBO_RPOVIDER_DELAY = "dubbo.provider.delay";
    public static final String DUBBO_CONSUMER_TIMEOUT = "dubbo.consumer.timeout";
    public static final String DUBBO_CONSUMER_RETRIES = "dubbo.consumer.retries";
    public static final String DUBBO_REGISTRY_PROTOCOL = "dubbo.registry.protocol";
    public static final String DUBBO_REGISTRY_ADDRESS = "dubbo.registry.address";
    public static final String DUBBO_SCAN_PACKNAME = "dubbo.scanPackName";

    //应用相关信息配置
    private static final String APP_PROPERTIES_CLASSPATH = "/META-INF/app.properties";
    private static final String APP_PROPERTIES_KEY = "app.id";
    private static final String APP_PROPERTIES_ENV_PATH = "/META-INF/extend/env-%s.properties";

    private static Properties properties;
    private static String appid;

    //加载环境配置参数
    static {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            properties = new Properties();
            Resource resource = resolver.getResource(String.format(APP_PROPERTIES_ENV_PATH, getEnv()));
            properties.load(resource.getInputStream());
        } catch (IOException e) {
            throw new BaseException(e, BaseExceotionEnum.RESOURCE_LOAD_ERROR.getCode(), BaseExceotionEnum.RESOURCE_LOAD_ERROR.getMessage(), BaseExceotionEnum.RESOURCE_LOAD_ERROR.getStatus());
        }

    }

    //加载appid
    static {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource resource = resolver.getResource(APP_PROPERTIES_CLASSPATH);
            properties.load(resource.getInputStream());
            appid = properties.getProperty(APP_PROPERTIES_KEY);
            if (StringUtils.isEmpty(appid)){
                throw new IllegalArgumentException("Configuration parameter app.id not detected,Please check app.properties!");
            }
        } catch (IOException e) {
            throw new BaseException(e, BaseExceotionEnum.RESOURCE_LOAD_ERROR.getCode(), BaseExceotionEnum.RESOURCE_LOAD_ERROR.getMessage(), BaseExceotionEnum.RESOURCE_LOAD_ERROR.getStatus());
        }
    }

    /**
    *@Description 设置属性
    *@Param [key, value]
    *@Author mingj
    *@Date 2019/12/16 23:25
    *@Return void
    **/
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
    *@Description 获取属性
    *@Param [key]
    *@Author mingj
    *@Date 2019/12/18 23:07
    *@Return java.lang.String
    **/
    public static String getProperty(ConfigurableEnvironment env, String key) {
        String property = env.getProperty(key);
        if (!StringUtils.isEmpty(property)){
            return property;
        }
        return properties.getProperty(key);
    }

    /**
    *@Description 获取带默认值的属性
    *@Param [key, defaultValue]
    *@Author mingj
    *@Date 2019/12/18 23:22
    *@Return java.lang.String
    **/
    public static String getProperty(ConfigurableEnvironment env, String key, String defaultValue) {
        String value = getProperty(env, key);
        if (StringUtils.isEmpty(value)){
            return defaultValue;
        }
        return value;
    }

    /**
    *@Description 获取环境变量
    *@Param []
    *@Author mingj
    *@Date 2019/12/16 23:25
    *@Return java.lang.String
    **/
    private static String getEnv() {
        final String env = System.getProperty("env");
        return env == null ? "dev" : env;
    }

    /**
    *@Description 获取appid信息
    *@Param []
    *@Author mingj
    *@Date 2019/12/18 22:11
    *@Return java.lang.String
    **/
    public static String getAppid() {
        String property = System.getProperty(APP_PROPERTIES_KEY);
        return !StringUtils.isEmpty(property)?property:appid;
    }
}
