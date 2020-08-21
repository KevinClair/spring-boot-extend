package com.extend.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Properties;

/**
 * @version 1.0
 * @ClassName ApplicationEnvironmentPreparedEventListener
 * @Description bannar信息
 * @Author mingj
 * @Date 2019/12/31 9:23
 **/
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationEnvironmentPreparedEventListener.class);

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
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        event.getSpringApplication().setBannerMode(Banner.Mode.OFF);
        logger.info(buildBannerText());
    }

    /**
    *@Description 获取打印信息
    *@Param []
    *@Author mingj
    *@Date 2019/12/31 9:42
    *@Return java.lang.String
    **/
    private String buildBannerText() {
        StringBuilder bannerTextBuilder = new StringBuilder();
        bannerTextBuilder.append(LINE_SEPARATOR).append(BANNAR).append(" :: Extend-Parent ::         (v").append(getVersion()).append(")").append(LINE_SEPARATOR);
        return bannerTextBuilder.toString();
    }

    /**
    *@Description 获取版本号
    *@Param []
    *@Author mingj
    *@Date 2019/12/31 9:41
    *@Return java.lang.String
    **/
    private static String getVersion(){
        String version= null;
        try {
            Properties properties = new Properties();
            properties.load(ApplicationEnvironmentPreparedEventListener.class.getResourceAsStream(PATH));
            version = properties.getProperty("version");
        }catch (Exception e){
        }
        return version;
    }
}
