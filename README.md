# spring-boot-extend
![star](https://img.shields.io/github/stars/KevinClair/spring-boot-extend)
![issues](https://img.shields.io/github/issues/KevinClair/spring-boot-extend)
![forks](https://img.shields.io/github/forks/KevinClair/spring-boot-extend)
![liscense](https://img.shields.io/github/license/KevinClair/spring-boot-extend)
![RocketMQ](https://img.shields.io/badge/MQ-RocketMQ-9cf)
![Dubbo](https://img.shields.io/badge/Rpc-Dubbo-green)
![Apollo](https://img.shields.io/badge/Config-Apollo-yellow)
![Spring](https://img.shields.io/badge/Framework-Spring-yellowgreen)
![Mybatis](https://img.shields.io/badge/ORM-Mybatis-blue)
![Cat](https://img.shields.io/badge/APM-cat-red)

## 背景
个人开发在项目中因为比较多的使用了多个数据库数据源，以往的做法是在spring-boot的基础上通过新建配置类来实现多数据源的操作，而且在事务操作上不是很友好，并且如果多个项目都使用到的话，每个项目都得写一套，很繁琐，因此自己做了一个可以快速集成多数据源的工具。后面又集成了apollo配置中心，还可以动态部署配置参数，非常简便快捷，同时集成了Dubbo，也可以实现分布式服务。

## spring-boot-extend是什么?
一个能快速继承多数据源的mysql数据库/多数据源mongidb数据库/apollo配置中心/zookeeper注册中心/dubbo服务/RocketMQ消息队列的工具

## spring-boot-extend有哪些功能？

* 快速`集成Mysql数据源`
    *  直接依赖相关组件，在启动类中添加注解，并且在项目的配置文件apollo配置中心中配置相关数据源属性即可使用
    *  支持多数据源的事务
* 快速`集成MongoDB数据源`
    *  直接依赖相关组件，在启动类中添加注解，并且在项目的配置文件apollo配置中心中配置相关数据源属性即可使用
* 集成`apollo配置中心`
    *  详情请查看apollo配置中心文档[apollo](https://github.com/ctripcorp/apollo)
    *  实现日志级别的动态变更
* 引入`Dubbo`功能
    *  使用`Zookeeper`作为注册中心
    *  Dubbo官方文档[Dubbo](http://dubbo.apache.org/)
    *  Dubbo SPI扩展
 * 引入`RocketMQ`功能
    *  方法级别的消息消费功能
    *  RocketMQTemplate模板多种方法发送消息
    *  RocketMQTransactionTemplate模板发送事务消息，基本实现弱分布式事务；
    *  RocketMQ官方文档[RocketMQ](http://rocketmq.apache.org/)
 * 引入`Canal`
    * 监听Mysql数据库变化并将消息投递至RocketMQ;   
    * Canal官方文档[Canal](https://github.com/alibaba/canal)
 * 引入`Cat`
    * 引入了美团点评的分布式监控系统Cat；
    * 在Cat提供的插件基础上做出了扩展，新增了对RocketMQ的监控拦截； 
    * 未来还将支持Redis，MongoDB的插件支持；
    * Cat官方文档[Cat](https://github.com/dianping/cat)

## 有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流
* Email: mingjay0601@163.com
* QQ: 704714211

## 感激
感谢以下的项目，在个人学习过程中，给我起到了很大的帮助

* [Dubbo](http://dubbo.apache.org/zh-cn/) 
* [Zookeeper](https://zookeeper.apache.org/)
* [apollo](https://github.com/ctripcorp/apollo)
* [Mybatis](https://github.com/mybatis/mybatis-3)
* [Mybatis-Spring](https://github.com/mybatis/spring)
* [Spring](https://github.com/spring-projects/spring-framework)
* [YunaiV](https://github.com/YunaiV/Blog)
* [RocketMQ](https://github.com/apache/rocketmq)
* [Canal](https://github.com/alibaba/canal)
* [Cat](https://github.com/dianping/cat)

## 关于作者
94年萌新一枚，目前还是在持续学习阶段，欢迎大佬们多多指教。

## 最近计划
最近准备开始着手做一次项目的重构和升级，调整一下项目的模块以及工程架构；
* Redis模块对接，CacheManager对接;
* web模块支持；
* 接入zookeeper组件；

## 未来计划
* 动态日志变更  **(Done)**
* DUBBO的SPI扩展 **(已扩展实现Filter过滤器，新增Validation验证器扩展)**
* RocketMQ模块 **(Done)**
* 日志模块 **(Done)**
* 监控模块Cat **(Done)**
* Redis模块
* canal消息订阅集成 **(Done)**
* web模块的支持
* 工具类扩展

