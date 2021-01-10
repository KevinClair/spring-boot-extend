package com.extend.mongodb.config;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.ConfigurationLoadUtil;
import com.extend.mongodb.properties.MongoDbProperties;
import com.extend.mongodb.utils.MongoDbConfigurationLoadUtil;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @version 1.0
 * @ClassName MongodbDataSourceConfig
 * @Description mongodb数据源加载配置
 * @Author mingj
 * @Date 2019/4/14 16:53
 **/
public class MongoDbDataSourceConfig implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static Logger logger = LoggerFactory.getLogger(MongoDbDataSourceConfig.class);

    private ConfigurableEnvironment env;
    private List<MongoDbProperties> configs;
    private MongoDbProperties config;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
        initConfig();
        if (config != null) {
            registerBean(beanFactory, config, true);
        } else if (configs != null) {
            for (MongoDbProperties config : configs) {
                registerBean(beanFactory,config,false);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }

    private void initConfig(){
        if (!StringUtils.isEmpty(ConfigurationLoadUtil.getProperty(env, EnvironmentManager.MONGODB_CONFIG_NAME))) {
            config = MongoDbConfigurationLoadUtil.loadSingleMongodbConfigFrom(env);
        } else {
            configs = MongoDbConfigurationLoadUtil.loadMultipleMongodbConfigFrom(env);
        }
    }

    /**
    *@Description 注册Bean
    *@Param [beanFactory, config]
    *@Author mingj
    *@Date 2019/4/14 19:13
    *@Return void
    **/
    private void registerBean(BeanDefinitionRegistry beanFactory, MongoDbProperties config, Boolean flag) {
        MongoClientURI mongoClientURI = MongoDbConfigurationLoadUtil.convertConfig(config);

        String factorySourceName = config.getName() + "MongoFactory";
        String templateSourceName = flag?"mongoTemplate":config.getName() + "MongoTemplate";
        registerMongodbFactoryBeanDefinitionBuilder(beanFactory, mongoClientURI, factorySourceName);
        registerMongodbTemplateBeanDefinitionBuilder(beanFactory, factorySourceName, templateSourceName);
    }

    /**
    *@Description 注册mongodbTemplate
    *@Param [beanFactory, factorySourceName, templateSourceName]
    *@Author mingj
    *@Date 2019/4/16 14:18
    *@Return void
    **/
    private void registerMongodbTemplateBeanDefinitionBuilder(BeanDefinitionRegistry beanFactory, String factorySourceName, String templateSourceName) {
        //注册MongodbTemplate模板
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(factorySourceName);
        BeanDefinitionBuilder mongoDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoTemplate.class);
        mongoDefinitionBuilder.addConstructorArgValue(beanDefinition);
        beanFactory.registerBeanDefinition(templateSourceName, mongoDefinitionBuilder.getRawBeanDefinition());
    }

    /**
    *@Description 注册mongodbFactory
    *@Param [beanFactory, mongoClientURI, factorySourceName]
    *@Author mingj
    *@Date 2019/4/16 14:16
    *@Return void
    **/
    private void registerMongodbFactoryBeanDefinitionBuilder(BeanDefinitionRegistry beanFactory, MongoClientURI mongoClientURI, String factorySourceName) {
        //注册Mongodb工厂
        BeanDefinitionBuilder dataSourceDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMongoClientDbFactory.class);
        dataSourceDefinitionBuilder.addConstructorArgValue(mongoClientURI.getURI());
        beanFactory.registerBeanDefinition(factorySourceName, dataSourceDefinitionBuilder.getRawBeanDefinition());
    }
}
