package com.extend.mybatis.properties;

import lombok.Data;

/**
 * MyBatisConfigurationProperties.
 *
 * @author KevinClair
 */
@Data
public class MyBatisConfigurationProperties {

    //名称
    private String name;
    //
    private String basePackage;
    //数据库驱动类
    private String driverClassName;
    //数据库连接地址
    private String jdbcUrl;
    //数据库用户名
    private String username;
    //数据库用户密码
    private String password;
    //
    private String slaveJdbcUrl;
    private String slaveUserName;
    private String slavePassword;
    private Boolean slaveReadOnly;
    private Boolean readOnly;
    private Long connectionTimeout;
    private Long idleTimeout;
    private Long maxLifetime;
    private Long leakDetectionThreshold;
    private Integer maximumPoolSize;
    private String mapperLocations;
    private String typeAliasesPackage;
    private String configLocation;
    private Integer minimumIdle;
    private String dataSourceClassName;
    private String validationQuery;
    private String mapperAnnotationClass;
    private String markerInterface;
    private Integer defaultStatementTimeout;
    /** 是否开启驼峰命名 */
    private Boolean mapUnderscoreToCamelCase;
    /** 连接初始化sql */
    private String connectionInitSql;
}
