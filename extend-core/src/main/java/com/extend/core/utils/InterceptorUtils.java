package com.extend.core.utils;

import com.extend.core.config.PluginConfigManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;

/**
 * InterceptorUtils。
 *
 * @author KevinClair
 */
public class InterceptorUtils {

    private static final String INTERCEPTOR_FILE_NAME = "com.extend.plugin.Interceptor";


    /**
     * 有参构造函数生成代理对象
     *
     * @param cla                被代理类的class
     * @param argumentTypes      参数类型
     * @param arguments          参数
     * @param interceptorKeyName 拦截器key
     * @param <T>                泛型
     * @return 被代理之后的类
     * @throws Exception
     */
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
     * 无参构造函数生成代理对象
     *
     * @param cla                被代理类的class
     * @param interceptorKeyName 拦截器key
     * @param <T>                泛型
     * @return
     * @throws Exception
     */
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
     * 从配置管理器中读取拦截器
     *
     * @param interceptorKeyName 拦截器key
     * @return {{@link Interceptor}}
     * @throws Exception
     */
    private static Interceptor getInterceptor(String interceptorKeyName) throws Exception {
        Interceptor interceptor = new Interceptor(null);
        String className = PluginConfigManage.getProperty(INTERCEPTOR_FILE_NAME, interceptorKeyName);
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
