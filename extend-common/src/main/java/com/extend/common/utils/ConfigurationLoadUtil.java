package com.extend.common.utils;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @version 1.0
 * @ClassName ApplicationConfigurationLoadUtil
 * @Description 应用配置加载工具类，优先读取本地配置文件，后读取apollo配置中心属性
 * @Author mingj
 * @Date 2019/11/30 17:02
 **/
public class ConfigurationLoadUtil {

//    private static Config config;
//
//    static {
//        config = ConfigService.getAppConfig();
//    }

    /**
    *@Description 获取属性
    *@Param [env, key]
    *@Author mingj
    *@Date 2019/11/30 22:00
    *@Return java.lang.String
    **/
    public static String getProperty(Environment env, String key){
        String property = env.getProperty(key);
        if (StringUtils.isEmpty(env.getProperty(key))){
            return "";
        }
        return property;
    }


    /**
     *@Description 获取属性
     *@Param [env, key]
     *@Author mingj
     *@Date 2019/11/30 22:00
     *@Return java.lang.String
     **/
    public static String getProperty(Environment env, String keyPlaceholder, int index){
        String key = String.format(keyPlaceholder, index);
        return getProperty(env, key);
    }
}
