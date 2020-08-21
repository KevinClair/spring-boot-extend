package com.extend.mongodb.properties;

/**
 * @version 1.0
 * @ClassName MongoDBProperties
 * @Description mongodb数据源配置基础信息
 * @Author mingj
 * @Date 2019/4/14 17:00
 **/
public class MongoDbProperties {

    /** 连接资源名称*/
    private String name;
    /**是否开启mongodb存储，默认为true**/
    private Boolean repositoriesEnabled;
    /**mongodb数据仓库**/
    private String repositoriesLocations;
    /**指定Mongo database URI*/
    private String repositoriesAuthenticationDatabase;
    /** 指定登陆mongodb的用户名.*/
    private String mongodbUri;

    /**数据库名称**/
    private String mongoDBDataSourceName;
    /**线程池空闲时保持的最小连接数**/
    private Integer minConnectionsPerHost;
    /**线程池允许的最大连接数*/
    private Integer connectionsPerHost;
    /**线程池中连接的最大空闲时间*/
    private Integer connectTimeout;
    /**最大队列数*/
    private Integer threadsAllowedToBlockForConnectionMultiplier;
    /**线程池中连接的最大空闲时间*/
    private Integer maxConnectionIdleTime;
    /**线程池中连接的最长生存时间*/
    private Integer maxConnectionLifeTime;
    /**最长等待时间*/
    private Integer socketTimeout;
    /**线程等待连接变为可用的最长时间*/
    private Integer maxWaitTime;
    /**socket是否保活*/
    private boolean socketKeepAlive;
    /**设置服务器选择超时（以毫秒为单位），它定义驱动程序在抛出异常之前等待服务器选择成功的时间
     * 值为0表示如果没有可用的服务器，它将立即超时。 负值意味着无限期等待*/
    private Integer serverSelectionTimeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRepositoriesEnabled() {
        return repositoriesEnabled;
    }

    public void setRepositoriesEnabled(Boolean repositoriesEnabled) {
        this.repositoriesEnabled = repositoriesEnabled;
    }

    public String getRepositoriesAuthenticationDatabase() {
        return repositoriesAuthenticationDatabase;
    }

    public void setRepositoriesAuthenticationDatabase(String repositoriesAuthenticationDatabase) {
        this.repositoriesAuthenticationDatabase = repositoriesAuthenticationDatabase;
    }

    public String getMongodbUri() {
        return mongodbUri;
    }

    public void setMongodbUri(String mongodbUri) {
        this.mongodbUri = mongodbUri;
    }

    public String getMongoDBDataSourceName() {
        return mongoDBDataSourceName;
    }

    public void setMongoDBDataSourceName(String mongoDBDataSourceName) {
        this.mongoDBDataSourceName = mongoDBDataSourceName;
    }

    public Integer getMinConnectionsPerHost() {
        return minConnectionsPerHost;
    }

    public void setMinConnectionsPerHost(Integer minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    public Integer getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(Integer connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public Integer getMaxConnectionIdleTime() {
        return maxConnectionIdleTime;
    }

    public void setMaxConnectionIdleTime(Integer maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public Integer getMaxConnectionLifeTime() {
        return maxConnectionLifeTime;
    }

    public void setMaxConnectionLifeTime(Integer maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public Integer getServerSelectionTimeout() {
        return serverSelectionTimeout;
    }

    public void setServerSelectionTimeout(Integer serverSelectionTimeout) {
        this.serverSelectionTimeout = serverSelectionTimeout;
    }

    public String getRepositoriesLocations() {
        return repositoriesLocations;
    }

    public void setRepositoriesLocations(String repositoriesLocations) {
        this.repositoriesLocations = repositoriesLocations;
    }
}
