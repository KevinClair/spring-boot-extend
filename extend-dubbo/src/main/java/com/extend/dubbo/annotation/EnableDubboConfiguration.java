package com.extend.dubbo.annotation;

import com.extend.dubbo.config.EnableDubboImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @ClassName EnableDubboConfiguration
 * @Description dubbo服务开启注解
 * @Author mingj
 * @Date 2019/12/16 22:56
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableDubboImportSelector.class})
public @interface EnableDubboConfiguration {

    String port() default "20880";

    String protocol() default "dubbo";

    String scanPackageName() default "com.example";
}
