package com.extend.core.config;

import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息资源管理器
 *
 * @author KevinClair
 */
@Slf4j
public class MessageResourcesManage {

    private static final String MESSAGE_PROPERTIES = "classpath*:/i18n/messages_*.properties";
    private static final String MESSAGE_PROPERTIES_PREFIX = ".properties";
    private static final Pattern regex = Pattern.compile("\\{([^}]*)\\}");

    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();

    static {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            //获取资源
            Resource[] resources = patternResolver.getResources(MESSAGE_PROPERTIES);
            if (Objects.nonNull(resources) && resources.length > 0) {
                for (Resource resource : resources) {
                    Properties properties = new Properties();
                    properties.load(resource.getInputStream());
                    PROPERTIES_MAP.put(getLanguageFileName(resource), properties);
                }
            }
        } catch (IOException e) {
            log.error("Message resources load error, exception message:{}", ExceptionUtils.getStackTrace(e));
            throw new BaseException(e, BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getCode(), BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getMessage(), BaseExceotionEnum.SYSTEM_INIT_PROFILES_ERROR.getStatus());
        }
    }

    /**
     * 获取对应语言的message信息.
     *
     * @param  languageType
     * @param  messageKey
     * @return {{@link String}}
     */
    public static String getMessage(String languageType, String messageKey) {
        return PROPERTIES_MAP.get(languageType).getProperty(getContextInfo(messageKey));
    }

    private static String getLanguageFileName(Resource resource) {
        return resource.getFilename().substring(9, resource.getFilename().indexOf(MESSAGE_PROPERTIES_PREFIX));
    }

    private static String getContextInfo(String messageKey) {
        Matcher matcher = regex.matcher(messageKey);
        return matcher.find() ? matcher.group(1) : null;
    }
}
