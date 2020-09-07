package com.extend.log.cat.dubbo;

/**
 * 定义部分静态变量
 *
 * @author mingj
 * @date 2020/9/4
 */
public class CatConstants {

    public  static final String CROSS_CONSUMER ="PigeonCall";

    public static final String CROSS_SERVER = "PigeonService";
    
    public static final String PROVIDER_APPLICATION_NAME="serverApplicationName";
    
    public static final String CONSUMER_CALL_SERVER="PigeonCall.server";
    
    public static final String CONSUMER_CALL_APP="PigeonCall.app";
    
    public static final String CONSUMER_CALL_PORT="PigeonCall.port";
    
    public static final String PROVIDER_CALL_SERVER="PigeonService.client";
    
    public static final String PROVIDER_CALL_APP="PigeonService.app";

    public static final String FORK_MESSAGE_ID="m_forkedMessageId";

    public static final String FORK_ROOT_MESSAGE_ID="m_rootMessageId";

    public static final String FORK_PARENT_MESSAGE_ID="m_parentMessageId";
    
}
