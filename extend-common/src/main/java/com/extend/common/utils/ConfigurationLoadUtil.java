package com.extend.common.utils;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * ConfigurationLoadUtil 配置加载器。
 *
 * @author KevinClair
 */
public class ConfigurationLoadUtil {

//    private static Config config;
//
//    static {
//        config = ConfigService.getAppConfig();
//    }

    /**
     *  属性配置读取
     *
     * @param env 环境信息
     * @param key 属性key
     * @return {{@link String}}
     */
    public static String getProperty(Environment env, String key){
        String property = env.getProperty(key);
        if (StringUtils.isEmpty(env.getProperty(key))){
            return "";
        }
        return property;
    }


    /**
     * 属性配置读取
     *
     * @param env            环境信息
     * @param keyPlaceholder 属性key
     * @param index          标记
     * @return {{@link String}}
     */
    public static String getProperty(Environment env, String keyPlaceholder, int index){
        String key = String.format(keyPlaceholder, index);
        return getProperty(env, key);
    }
}
