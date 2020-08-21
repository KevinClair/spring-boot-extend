package com.extend.mongodb.utils;

import com.extend.common.constant.EnvironmentManager;
import com.extend.common.utils.ConfigurationLoadUtil;
import com.extend.mongodb.properties.MongoDbProperties;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0
 * @ClassName MongodbConfigLoadUtil
 * @Description mongodb数据源加载配置工具类
 * @Author mingj
 * @Date 2019/4/14 17:03
 **/
public class MongoDbConfigurationLoadUtil {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbConfigurationLoadUtil.class);

    public static MongoDbProperties loadSingleMongodbConfigFrom(Environment env) {
        return AppConfigLoadUtil.loadMongoDbConfig(env);
    }

    public static List<MongoDbProperties> loadMultipleMongodbConfigFrom(Environment env) {
        HashMap<String, MongoDbProperties> configsMap = new HashMap<>();
        int index = 0;
        while (true) {
            String configName = ConfigurationLoadUtil.getProperty(env, String.format(EnvironmentManager.MONGODB_CONFIGS_NAME, index));
            if (!StringUtils.isEmpty(configName)) {
                //如果配置名相同,则只有第一个有效
                if (!configsMap.containsKey(configName)) {
                    MongoDbProperties mongoDbProperties = AppConfigLoadUtil.loadMongoDbConfig(env, index);
                    configsMap.put(configName, mongoDbProperties);
                }
            } else {
                break;
            }
            index++;
        }
        return new ArrayList<>(configsMap.values());
    }


    public static MongoClientURI convertConfig(MongoDbProperties config) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.minConnectionsPerHost(config.getMinConnectionsPerHost());
        builder.connectionsPerHost(config.getConnectionsPerHost());
        builder.connectTimeout(config.getConnectTimeout());
        builder.threadsAllowedToBlockForConnectionMultiplier(config.getThreadsAllowedToBlockForConnectionMultiplier());
        builder.maxConnectionIdleTime(config.getMaxConnectionIdleTime());
        builder.socketTimeout(config.getSocketTimeout());
        builder.maxWaitTime(config.getMaxWaitTime());
        builder.socketKeepAlive(config.isSocketKeepAlive());
        builder.serverSelectionTimeout(config.getServerSelectionTimeout());
        return new MongoClientURI(config.getMongodbUri(), builder);

    }
}
