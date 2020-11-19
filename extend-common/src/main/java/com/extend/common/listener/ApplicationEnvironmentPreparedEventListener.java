package com.extend.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Properties;

/**
 * @version 1.0
 * @ClassName ApplicationEnvironmentPreparedEventListener
 * @Description bannar信息
 * @Author mingj
 * @Date 2019/12/31 9:23
 **/
@Slf4j
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String PATH = "/META-INF/maven/com.extend/extend-common/pom.properties";
    private static final String BANNAR = "\n" +
            "███████╗██╗  ██╗████████╗███████╗███╗   ██╗██████╗ \n" +
            "██╔════╝╚██╗██╔╝╚══██╔══╝██╔════╝████╗  ██║██╔══██╗\n" +
            "█████╗   ╚███╔╝    ██║   █████╗  ██╔██╗ ██║██║  ██║\n" +
            "██╔══╝   ██╔██╗    ██║   ██╔══╝  ██║╚██╗██║██║  ██║\n" +
            "███████╗██╔╝ ██╗   ██║   ███████╗██║ ╚████║██████╔╝\n" +
            "╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═══╝╚═════╝ \n";

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        event.getSpringApplication().setBannerMode(Banner.Mode.OFF);
        log.info(buildBannerText());
        // 设置默认的环境属性
        ConfigurableEnvironment environment = event.getEnvironment();
        if (StringUtils.isEmpty(environment.getProperty("env"))) {
            System.setProperty("env", "dev");
            log.info("No system environment set, falling back to default environment: dev");
        }
    }

    /**
     * @Description 获取打印信息
     * @Param []
     * @Author mingj
     * @Date 2019/12/31 9:42
     * @Return java.lang.String
     **/
    private String buildBannerText() {
        StringBuilder bannerTextBuilder = new StringBuilder();
        bannerTextBuilder.append(LINE_SEPARATOR).append(BANNAR).append(" :: Extend-Parent ::         (v").append(getVersion()).append(")").append(LINE_SEPARATOR);
        return bannerTextBuilder.toString();
    }

    /**
     * @Description 获取版本号
     * @Param []
     * @Author mingj
     * @Date 2019/12/31 9:41
     * @Return java.lang.String
     **/
    private static String getVersion() {
        String version = null;
        try {
            Properties properties = new Properties();
            properties.load(ApplicationEnvironmentPreparedEventListener.class.getResourceAsStream(PATH));
            version = properties.getProperty("version");
        } catch (Exception e) {
        }
        return version;
    }
}
