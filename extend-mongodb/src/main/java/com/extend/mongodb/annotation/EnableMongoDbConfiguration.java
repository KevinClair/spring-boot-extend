package com.extend.mongodb.annotation;

import com.extend.mongodb.config.EnableMongoDbImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @ClassName EnableMongo
 * @Description 开启mongodb
 * @Author mingj
 * @Date 2019/4/14 16:44
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableMongoDbImportSelector.class})
public @interface EnableMongoDbConfiguration {
}
