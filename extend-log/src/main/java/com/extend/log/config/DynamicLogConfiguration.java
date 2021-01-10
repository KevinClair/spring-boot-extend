package com.extend.log.config;

import com.extend.log.cat.web.CatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * DynamicLogConfiguration.
 *
 * @author KevinClair
 */
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
     * cat的servlet拦截器
     *
     * @return {@link FilterRegistrationBean}
     */
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
