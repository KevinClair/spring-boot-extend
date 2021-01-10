package com.extend.mybatis.utils;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.ConfigurationLoadUtil;
import com.extend.mybatis.properties.MyBatisConfigurationProperties;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyBatisConfigurationLoadUtil.
 *
 * @author KevinClair
 */
public class MyBatisConfigurationLoadUtil {

    /**
    *@Description 读取单数据源配置
    *@Param [env]
    *@Author mingj
    *@Date 2019/9/29 12:59
    *@Return com.example.mybatis.properties.MyBatisConfigurationProperties
    **/
    public static MyBatisConfigurationProperties loadSingleMyBatisConfiguration(Environment env){
        String name = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_NAME);
        //数据源配置，如果读取不到，直接返回Null
        if (StringUtils.isEmpty(name)){
            return null;
        }
        MyBatisConfigurationProperties config = new MyBatisConfigurationProperties();
        //配置名称，用于后续实现多数据源事务
        config.setName(name);
        config.setBasePackage(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_BASEPACKAGE));
        config.setJdbcUrl(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_JDBCURL));
        config.setDriverClassName(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_DRIVERCLASSNAME));
        config.setUsername(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_USERNAME));
        config.setPassword(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_PASSWORD));
        String readOnly = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_READONLY);
        if (!StringUtils.isEmpty(readOnly)){
            config.setReadOnly(Boolean.parseBoolean(readOnly.trim()));
        }
        String connectionTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_CONNECTIONTIMEOUT);
        if (!StringUtils.isEmpty(connectionTimeoutStr)) {
            config.setConnectionTimeout(Long.parseLong(connectionTimeoutStr));
        }
        String idleTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_IDLETIMEOUT);
        if (!StringUtils.isEmpty(idleTimeoutStr)) {
            config.setIdleTimeout(Long.parseLong(idleTimeoutStr));
        }
        String maxLifetimeStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MAXLIFETIME);
        if (!StringUtils.isEmpty(maxLifetimeStr)) {
            config.setMaxLifetime(Long.parseLong(maxLifetimeStr));
        }
        String maxImumpoolSizeStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MAXIMUMPOOLSIZE);
        if (!StringUtils.isEmpty(maxImumpoolSizeStr)) {
            config.setMaximumPoolSize(Integer.parseInt(maxImumpoolSizeStr));
        }
        String mapperAnnotationClass = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MAPPER_ANNOTATION_CLASS);
        if (!StringUtils.isEmpty(mapperAnnotationClass)) {
            config.setMapperAnnotationClass(mapperAnnotationClass);
        }
        config.setMapperLocations(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MAPPERLOCATIONS));
        config.setTypeAliasesPackage(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_TYPEALIASEPACKAGE));
        String markerInterface = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MARKERINTERFACE);
        if (!StringUtils.isEmpty(markerInterface)) {
            config.setMarkerInterface(markerInterface);
        }
        String defaultStatementTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_DEFAULTSTATEMENTTIMEOUT);
        if (!StringUtils.isEmpty(defaultStatementTimeoutStr)) {
            config.setDefaultStatementTimeout(Integer.parseInt(defaultStatementTimeoutStr));
        }
        String configLocation = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_CONFIGLOCATION);
        if (!StringUtils.isEmpty(configLocation)){
            config.setConfigLocation(configLocation);
        }
        String mapUnderscoreToCamelCase = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_MAPUNDERSCORETOCAMELCASE);
        if (!StringUtils.isEmpty(mapUnderscoreToCamelCase)){
            config.setMapUnderscoreToCamelCase(Boolean.valueOf(mapUnderscoreToCamelCase));
        }
        String connectionInitSql = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIG_CONNECTIONINITSQL);
        if (!StringUtils.isEmpty(connectionInitSql)){
            config.setConnectionInitSql(connectionInitSql);
        }
        return config;
    }

    /**
    *@Description 读取多数据源配置
    *@Param [env]
    *@Author mingj
    *@Date 2019/9/29 12:59
    *@Return java.util.List<com.example.mybatis.properties.MyBatisConfigurationProperties>
    **/
    public static List<MyBatisConfigurationProperties> loadMultipleMyBatisConfiguration(Environment env){
        Map<String, MyBatisConfigurationProperties> configsMap = new HashMap<>();
        int index = 0;
        while (true) {
            String configName = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_NAME, index);
            if (!StringUtils.isEmpty(configName)) {
                //如果配置名相同,则只有第一个有效
                if (!configsMap.containsKey(configName)) {
                    //读取配置参数
                    MyBatisConfigurationProperties config = new MyBatisConfigurationProperties();
                    config.setName(configName);
                    config.setBasePackage(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_BASEPACKAGE, index));
                    config.setDriverClassName(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_DRIVERCLASSNAME, index));
                    config.setJdbcUrl(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_JDBCURL, index));
                    config.setUsername(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_USERNAME, index));
                    config.setPassword(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_PASSWORD, index));
                    String readOnlyStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_READONLY, index);
                    if (!StringUtils.isEmpty(readOnlyStr)) {
                        config.setReadOnly(Boolean.parseBoolean(readOnlyStr.trim()));
                    }
                    String markerInterface = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_MARKERINTERFACE, index);
                    if (!StringUtils.isEmpty(markerInterface)) {
                        config.setMarkerInterface(markerInterface);
                    }
                    String connectionTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_CONNECTIONTIMEOUT, index);
                    if (!StringUtils.isEmpty(connectionTimeoutStr)) {
                        config.setConnectionTimeout(Long.parseLong(connectionTimeoutStr));
                    }
                    String idleTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_IDLETIMEOUT, index);
                    if (!StringUtils.isEmpty(idleTimeoutStr)) {
                        config.setIdleTimeout(Long.parseLong(idleTimeoutStr));
                    }
                    String maxLifetimeStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_MAXLIFETIME, index);
                    if (!StringUtils.isEmpty(maxLifetimeStr)) {
                        config.setMaxLifetime(Long.parseLong(maxLifetimeStr));
                    }
                    String maxImumpoolSizeStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_MAXIMUMPOOLSIZE, index);
                    if (!StringUtils.isEmpty(maxImumpoolSizeStr)) {
                        config.setMaximumPoolSize(Integer.parseInt(maxImumpoolSizeStr));
                    }
                    config.setMapperLocations(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_MAPPERLOCATIONS, index));
                    config.setTypeAliasesPackage(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_TYPEALIASEPACKAGE, index));
                    String defaultStatementTimeoutStr = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_DEFAULTSTATEMENTTIMEOUT, index);
                    if (!StringUtils.isEmpty(defaultStatementTimeoutStr)) {
                        config.setDefaultStatementTimeout(Integer.parseInt(defaultStatementTimeoutStr));
                    }
                    String configLocation = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_CONFIGLOCATION, index);
                    if (!StringUtils.isEmpty(configLocation)){
                        config.setConfigLocation(configLocation);
                    }
                    String mapUnderscoreToCamelCase = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_MAPUNDERSCORETOCAMELCASE, index);
                    if (!StringUtils.isEmpty(mapUnderscoreToCamelCase)){
                        config.setMapUnderscoreToCamelCase(Boolean.valueOf(mapUnderscoreToCamelCase));
                    }
                    String connectionInitSql = ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MYBATIS_CONFIGS_CONNECTIONINITSQL, index);
                    if (!StringUtils.isEmpty(connectionInitSql)){
                        config.setConnectionInitSql(connectionInitSql);
                    }
                    configsMap.put(configName, config);
                }
            } else {
                break;
            }
            index++;
        }
        return new ArrayList<>(configsMap.values());

    }

    /**
    *@Description 创建kikari数据源
    *@Param [config]
    *@Author mingj
    *@Date 2019/9/29 15:34
    *@Return com.zaxxer.hikari.HikariConfig
    **/
    public static HikariConfig createDataSource(MyBatisConfigurationProperties config) {
        HikariConfig hikariConfig = new HikariConfig();
        if (!StringUtils.isEmpty(config.getName())) {
            hikariConfig.setPoolName(config.getName());
        }
        if (config.getMaximumPoolSize() != null) {
            hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
        }
        if (!StringUtils.isEmpty(config.getDataSourceClassName())) {
            hikariConfig.setDataSourceClassName(config.getDataSourceClassName());
        }
        if (!StringUtils.isEmpty(config.getDriverClassName())) {
            hikariConfig.setDriverClassName(config.getDriverClassName());
        }
        if (config.getMinimumIdle() != null) {
            hikariConfig.setMinimumIdle(config.getMinimumIdle());
        }
        if (config.getConnectionTimeout() != null) {
            hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        }
        if (config.getIdleTimeout() != null) {
            hikariConfig.setIdleTimeout(config.getIdleTimeout());
        }
        if (config.getValidationQuery() != null) {
            hikariConfig.setConnectionTestQuery(config.getValidationQuery());
        }
        if (config.getMaxLifetime() != null) {
            hikariConfig.setMaxLifetime(config.getMaxLifetime());
        }
        if (!StringUtils.isEmpty(config.getJdbcUrl())) {
            hikariConfig.setJdbcUrl(config.getJdbcUrl());
        }
        if (!StringUtils.isEmpty(config.getUsername())) {
            hikariConfig.setUsername(config.getUsername());
        }
        if (!StringUtils.isEmpty(config.getPassword())) {
            hikariConfig.setPassword(config.getPassword());
        }
        if (!StringUtils.isEmpty(config.getConnectionInitSql())){
            hikariConfig.setConnectionInitSql(config.getConnectionInitSql());
        }
        return hikariConfig;
    }
}
