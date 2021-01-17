package com.extend.mongodb.utils;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.ConfigurationLoadUtil;
import com.extend.mongodb.properties.MongoDbProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * AppConfigLoadUtil.
 *
 * @author KevinClair
 */
public class AppConfigLoadUtil {

    /**
     * 加载数据源配置信息
     *
     * @param env 环境信息
     * @return {@link MongoDbProperties}
     */
    public static MongoDbProperties loadMongoDbConfig(Environment env) {
        String name = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_NAME);
        String authenticationDatabase = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_REPOSITORIESAUTHENTICATIONDATABASE);
        String mongodbUri = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_MONGODB_URI);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(authenticationDatabase) || StringUtils.isEmpty(mongodbUri)) {
            return null;
        }
        MongoDbProperties mongoDBProperties = new MongoDbProperties();
        mongoDBProperties.setName(name);
        mongoDBProperties.setRepositoriesAuthenticationDatabase(authenticationDatabase);
        mongoDBProperties.setMongodbUri(mongodbUri);

        String repositoriesEnabled = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_REPOSITORIESENABLED);
        mongoDBProperties.setRepositoriesEnabled(!StringUtils.isEmpty(repositoriesEnabled) ? Boolean.valueOf(repositoriesEnabled) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_REPOSITORIESENABLED);

        String repositoriesLocations = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_REPOSITORIESLOCATIONS);
        Optional.ofNullable(repositoriesLocations).ifPresent(location -> mongoDBProperties.setRepositoriesLocations(repositoriesLocations));

        String connectionPreHost = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_CONNECTIONSPERHOST);
        mongoDBProperties.setConnectionsPerHost(!StringUtils.isEmpty(connectionPreHost) ? Integer.parseInt(connectionPreHost) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_CONNECTIONSPERHOST);

        String connectionTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_CONNECTTIMEOUT);
        mongoDBProperties.setConnectTimeout(!StringUtils.isEmpty(connectionTime) ? Integer.parseInt(connectionTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_CONNECTTIMEOUT);

        String maxConnectionIdleTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_MAXCONNECTIONIDLETIME);
        mongoDBProperties.setMaxConnectionIdleTime(!StringUtils.isEmpty(maxConnectionIdleTime) ? Integer.parseInt(maxConnectionIdleTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXCONNECTIONIDLETIME);

        String maxConnectionLifeTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_MAXCONNECTIONLIFETIME);
        mongoDBProperties.setMaxConnectionLifeTime(!StringUtils.isEmpty(maxConnectionLifeTime) ? Integer.parseInt(maxConnectionLifeTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXCONNECTIONLIFETIME);

        String maxnWaitTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_MAXWAITTIME);
        mongoDBProperties.setMaxWaitTime(!StringUtils.isEmpty(maxnWaitTime) ? Integer.parseInt(maxnWaitTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXWAITTIME);

        String minConnectionsProHost = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_MINCONNECTIONSPERHOST);
        mongoDBProperties.setMinConnectionsPerHost(!StringUtils.isEmpty(minConnectionsProHost) ? Integer.parseInt(minConnectionsProHost) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MINCONNECTIONSPERHOST);

        String serverSlectionTimeout = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_SERVERSELECTIONTIMEOUT);
        mongoDBProperties.setServerSelectionTimeout(!StringUtils.isEmpty(serverSlectionTimeout) ? Integer.parseInt(serverSlectionTimeout) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SERVERSELECTIONTIMEOUT);

        String socketKeepAlive = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_SOCKETKEEPALIVE);
        mongoDBProperties.setSocketKeepAlive(!StringUtils.isEmpty(socketKeepAlive) ? Boolean.valueOf(socketKeepAlive) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SOCKETKEEPALIVE);

        String socketTimeout = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_SOCKETTIMEOUT);
        mongoDBProperties.setSocketTimeout(!StringUtils.isEmpty(socketTimeout) ? Integer.parseInt(socketTimeout) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SOCKETTIMEOUT);

        String threadsAllowedToBlockForConnectionMultiplier = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER);
        mongoDBProperties.setThreadsAllowedToBlockForConnectionMultiplier(!StringUtils.isEmpty(threadsAllowedToBlockForConnectionMultiplier) ? Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER);

        return mongoDBProperties;

    }

    /**
     * 加载数据源配置信息
     *
     * @param env   环境信息
     * @param index 多数据源index
     * @return {@link MongoDbProperties}
     */
    public static MongoDbProperties loadMongoDbConfig(Environment env, int index) {
        String name = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_NAME, index);
        String authenticationDatabase = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_REPOSITORIESAUTHENTICATIONDATABASE, index);
        String mongodbUri = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_MONGODB_URI, index);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(authenticationDatabase) || StringUtils.isEmpty(mongodbUri)) {
            return null;
        }
        MongoDbProperties mongoDBProperties = new MongoDbProperties();
        mongoDBProperties.setName(name);
        mongoDBProperties.setRepositoriesAuthenticationDatabase(authenticationDatabase);
        mongoDBProperties.setMongodbUri(mongodbUri);

        String repositoriesEnabled = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_REPOSITORIESENABLED, index);
        mongoDBProperties.setRepositoriesEnabled(!StringUtils.isEmpty(repositoriesEnabled) ? Boolean.valueOf(repositoriesEnabled) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_REPOSITORIESENABLED);

        String repositoriesLocations = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_REPOSITORIESLOCATIONS, index);
        Optional.ofNullable(repositoriesLocations).ifPresent(location -> mongoDBProperties.setRepositoriesLocations(repositoriesLocations));

        String connectionPreHost = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_CONNECTIONSPERHOST, index);
        mongoDBProperties.setConnectionsPerHost(!StringUtils.isEmpty(connectionPreHost) ? Integer.parseInt(connectionPreHost) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_CONNECTIONSPERHOST);

        String connectionTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_CONNECTTIMEOUT, index);
        mongoDBProperties.setConnectTimeout(!StringUtils.isEmpty(connectionTime) ? Integer.parseInt(connectionTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_CONNECTTIMEOUT);

        String maxConnectionIdleTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_MAXCONNECTIONIDLETIME, index);
        mongoDBProperties.setMaxConnectionIdleTime(!StringUtils.isEmpty(maxConnectionIdleTime) ? Integer.parseInt(maxConnectionIdleTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXCONNECTIONIDLETIME);

        String maxConnectionLifeTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_MAXCONNECTIONLIFETIME, index);
        mongoDBProperties.setMaxConnectionLifeTime(!StringUtils.isEmpty(maxConnectionLifeTime) ? Integer.parseInt(maxConnectionLifeTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXCONNECTIONLIFETIME);

        String maxnWaitTime = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_MAXWAITTIME, index);
        mongoDBProperties.setMaxWaitTime(!StringUtils.isEmpty(maxnWaitTime) ? Integer.parseInt(maxnWaitTime) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MAXWAITTIME);

        String minConnectionsProHost = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_MINCONNECTIONSPERHOST, index);
        mongoDBProperties.setMinConnectionsPerHost(!StringUtils.isEmpty(minConnectionsProHost) ? Integer.parseInt(minConnectionsProHost) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_MINCONNECTIONSPERHOST);

        String serverSlectionTimeout = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_SERVERSELECTIONTIMEOUT, index);
        mongoDBProperties.setServerSelectionTimeout(!StringUtils.isEmpty(serverSlectionTimeout) ? Integer.parseInt(serverSlectionTimeout) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SERVERSELECTIONTIMEOUT);

        String socketKeepAlive = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_SOCKETKEEPALIVE, index);
        mongoDBProperties.setSocketKeepAlive(!StringUtils.isEmpty(socketKeepAlive) ? Boolean.valueOf(socketKeepAlive) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SOCKETKEEPALIVE);

        String socketTimeout = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_SOCKETTIMEOUT, index);
        mongoDBProperties.setSocketTimeout(!StringUtils.isEmpty(socketTimeout) ? Integer.parseInt(socketTimeout) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_SOCKETTIMEOUT);

        String threadsAllowedToBlockForConnectionMultiplier = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIGS_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER, index);
        mongoDBProperties.setThreadsAllowedToBlockForConnectionMultiplier(!StringUtils.isEmpty(threadsAllowedToBlockForConnectionMultiplier) ? Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier) : EnvironmentManager.MONGODB_DEFAULT_CONFIG_THREADSALLOWEDTOBLOCKFORCONNECTIONMULTIPLIER);

        return mongoDBProperties;
    }
}
