package com.extend.common.utils;

import com.extend.common.config.PluginConfigManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;

/**
 * @version 1.0
 * @ClassName 生成拦截类
 * @Description TODO描述
 * @Author mingj
 * @Date 2020/7/4 21:25
 **/
public class InterceptorUtils {

    private static final String INTERCEPTOR_FILE_NAME = "com.extend.plugin.Interceptor";


    /**
     * @Description 有参构造函数生成代理对象
     * @Param [cla, argumentTypes, arguments, interceptorKeyName]
     * @Author mingj
     * @Date 2020/7/4 21:26
     * @Return T
     **/
    public static <T> T getProxyClass(Class<T> cla, Class[] argumentTypes, Object[] arguments, String interceptorKeyName) throws Exception {
        Interceptor interceptor = getInterceptor(interceptorKeyName);
        if (interceptor == null){
            Constructor<T> constructor = cla.getConstructor(argumentTypes);
            return constructor.newInstance(arguments);
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cla);
        enhancer.setCallback(interceptor);
        enhancer.setClassLoader(cla.getClassLoader());
        return (T) enhancer.create(argumentTypes, arguments);
    }

    /**
     * @Description 无参构造函数生成代理对象
     * @Param [cla, typePrefix]
     * @Author mingj
     * @Date 2020/7/4 21:28
     * @Return T
     **/
    public static <T> T getProxyClass(Class<T> cla, String interceptorKeyName) throws Exception {
        Interceptor interceptor = getInterceptor(interceptorKeyName);
        if (interceptor == null){
            Constructor<T> constructor = cla.getConstructor();
            return constructor.newInstance();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cla);
        enhancer.setCallback(interceptor);
        enhancer.setClassLoader(cla.getClassLoader());
        return (T) enhancer.create();
    }

    /**
     * @Description 通过配置文件信息获取配置好的代理类
     * @Param [typePrefix]
     * @Author mingj
     * @Date 2020/7/4 21:29
     * @Return Interceptor
     **/
    private static Interceptor getInterceptor(String interceptorKeyName) throws Exception {
        Interceptor interceptor = new Interceptor(null);
        String className = PluginConfigManager.getProperty(INTERCEPTOR_FILE_NAME, interceptorKeyName);
        if (StringUtils.isEmpty(className)){
            return null;
        }
        Class<Interceptor> aClass = (Class<Interceptor>) Class.forName(className);
        Constructor<Interceptor> constructor = aClass.getConstructor(MethodInterceptor.class);
        Interceptor temp = constructor.newInstance(interceptor);
        interceptor = new Interceptor(temp);
        return interceptor;

    }
}
