package com.extend.log.annotation;

import com.extend.log.config.EnableLogImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启动态日志
 *
 * @author mingj
 * @date 2020/8/11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableLogImportSelector.class})
public @interface EnableLog {
}
