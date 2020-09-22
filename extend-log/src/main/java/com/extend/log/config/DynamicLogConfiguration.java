package com.extend.log.config;

import com.extend.log.cat.web.CatFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 动态日志/cat过滤器配置类
 *
 * @author mingj
 * @date 2020/8/14
 */
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class DynamicLogConfiguration {

    /**
     * @Description 动态日志级别注入
     * @Author mingj
     * @Date 2020/9/4 0:03
     * @param
     * @return com.extend.log.config.DynamicLogChangeEventListener
     **/
//    @Bean
//    public DynamicLogChangeEventListener dynamicLogChangeEventListener(){
//        return new DynamicLogChangeEventListener();
//    }

    /**
     * @Description cat的servlet拦截器
     * @Author mingj
     * @Date 2020/9/4 0:03
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     **/
    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatFilter filter = new CatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }

}
