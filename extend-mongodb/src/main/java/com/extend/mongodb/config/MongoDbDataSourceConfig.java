package com.extend.mongodb.config;

import com.extend.core.config.EnvironmentManager;
import com.extend.core.utils.ConfigurationLoadUtil;
import com.extend.mongodb.properties.MongoDbProperties;
import com.extend.mongodb.utils.MongoDbConfigurationLoadUtil;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
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
 * MongoDbDataSourceConfig.
 *
 * @author KevinClair
 */
@Slf4j
public class MongoDbDataSourceConfig implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

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
     * 注册mongodb工厂
     *
     * @param beanFactory
     * @param config       mongodb配置属性 {@link MongoDbProperties}
     * @param flag         MongoDB模板名称
     */
    private void registerBean(BeanDefinitionRegistry beanFactory, MongoDbProperties config, Boolean flag) {
        MongoClientURI mongoClientURI = MongoDbConfigurationLoadUtil.convertConfig(config);

        String factorySourceName = config.getName() + "MongoFactory";
        String templateSourceName = flag?"mongoTemplate":config.getName() + "MongoTemplate";
        registerMongodbFactoryBeanDefinitionBuilder(beanFactory, mongoClientURI, factorySourceName);
        registerMongodbTemplateBeanDefinitionBuilder(beanFactory, factorySourceName, templateSourceName);
    }

    /**
     * 注册模板
     *
     * @param beanFactory
     * @param factorySourceName  工厂名
     * @param templateSourceName 模板资源名
     */
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
    /**
     * 注册mongodbFactory
     *
     * @param beanFactory
     * @param mongoClientURI    客户端url
     * @param factorySourceName 工厂资源名
     */
    private void registerMongodbFactoryBeanDefinitionBuilder(BeanDefinitionRegistry beanFactory, MongoClientURI mongoClientURI, String factorySourceName) {
        //注册Mongodb工厂
        BeanDefinitionBuilder dataSourceDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMongoClientDbFactory.class);
        dataSourceDefinitionBuilder.addConstructorArgValue(mongoClientURI.getURI());
        beanFactory.registerBeanDefinition(factorySourceName, dataSourceDefinitionBuilder.getRawBeanDefinition());
    }
}
