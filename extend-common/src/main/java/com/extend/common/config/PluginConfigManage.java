package com.extend.common.config;

import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

/**
 * 插件管理器。
 *
 * @author KevinClair
 */
public class PluginConfigManage {

    private static final String PLUGIN_PROPERTIES_CLASSPATH = "classpath*:META-INF/extend/**";
    private static final Map<String, Properties> PLUGIN_PROPERTIES_MAP = new HashMap<>();

    static {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            //获取资源
            Resource[] resources = patternResolver.getResources(PLUGIN_PROPERTIES_CLASSPATH);
            for (Resource resource : resources) {
                Properties props = new Properties();
                //加载资源
                props.load(resource.getInputStream());
                String fileName = resource.getFilename();
                if (PLUGIN_PROPERTIES_MAP.containsKey(fileName)) {
                    Properties properties = PLUGIN_PROPERTIES_MAP.get(fileName);
                    for (Map.Entry<Object, Object> entry : props.entrySet()) {
                        properties.setProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                } else {
                    //装载资源
                    PLUGIN_PROPERTIES_MAP.put(fileName, props);
                }
            }
        } catch (IOException e) {
            throw new BaseException(e, BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getCode(),BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getMessage(), BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getStatus());
        }
    }

    /**
     * 获取资源配置文件value集合
     *
     * @param  fileName 文件名称
     * @return {{@link Set<String>}} value的集合
     */
    public static Set<String> getPropertyValueSet(String fileName) {
        Set<String> setStr = new HashSet<>();
        Properties property = getProperty(fileName);
        if (property != null) {
            Set<String> strings = property.stringPropertyNames();
            for (String key : strings) {
                setStr.add(property.get(key).toString());
            }
        }
        return setStr;
    }

    /**
     * 获取资源配置文件key集合
     *
     * @param filName 文件名
     * @return {{@link Set<String>}} key的集合
     */
    public static Set<String> getPropertyKeySet(String filName) {
        Properties property = getProperty(filName);
        return property != null ? property.stringPropertyNames() : new HashSet<>();
    }

    /**
     * 获取属性配置
     *
     * @param filName 文件名
     * @return  {{@link Properties}}
     */
    private static Properties getProperty(String filName) {
        return PLUGIN_PROPERTIES_MAP.get(filName);
    }

    /**
     *  获取拦截器的属性配置
     *
     * @param fileName            文件名
     * @param interceptorKeyName  拦截器key
     * @return {{@link String}}
     */
    public static String getProperty(String fileName, String interceptorKeyName) {
        Properties properties = getProperty(fileName);
        if (properties != null) {
            return properties.get(interceptorKeyName) != null ? properties.get(interceptorKeyName).toString() : null;
        }
        return null;
    }
}
