package com.extend.common.exception;

/**
 * BaseExceotionEnum。
 *
 * @author KevinClair
 */
public enum BaseExceotionEnum {

    RESOURCE_LOAD_ERROR("ERR_001", "资源文件加载失败", false),
    INITIALIZE_FILTER_ERROR("ERR_002", "Dubbo过滤器初始化失败", false),
    REQUEST_PARAM_ERROR("ERR_003", "请求参数错误", false),
    SYSTEM_ERROR("ERR_004", "系统内部错误", false),
    REMOTE_METHOD_ERROR("ERR_005", "远程方法错误", false),
    DATABASE_SQL_ERROR("ERR_006", "数据库SQL执行错误", false),
    BASE_BUSINESS_ERROR("ERR_007", "内部业务错误", false),
    ROCKET_MQ_INIT_ERROR("ERR_008", "RocketMQ初始化错误", false),
    SYSTEM_INIT_PROFILES_ERROR("ERR_009", "系统初始化文件错误", false),
    MYBATIS_INIT_MAPPERLOCATIONS_ERROR("ERR_010", "Mybatis初始化MapperLocations文件错误", false),
    ROCKET_MQ_SEND_MESSAGE_ERROR("ERR_011", "RocketMQ消息发送异常", false),
    ROCKET_MQ_SERIALIZE_ERROR("ERR_012", "RocketMQ序列化类型错误", false),
    ;

    private String code;

    private String message;

    private boolean status;

    BaseExceotionEnum(String code, String message, boolean status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
