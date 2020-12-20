package com.extend.common.utils;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Interceptor。
 *
 * @author KevinClair
 */
public class Interceptor implements MethodInterceptor {

    private MethodInterceptor methodInterceptor;

    public Interceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        doBefore(proxy, method, params, methodProxy);
        if (methodInterceptor == null) {
            result = methodProxy.invokeSuper(proxy, params);
        } else {
            result = methodInterceptor.intercept(proxy, method, params, methodProxy);
        }
        doAfter(result);
        return result;
    }

    /**
     * 拦截前置校验
     *
     * @param proxy       代理对象
     * @param method      代理的方法
     * @param params      方法参数
     * @param methodProxy 方法代理
     */
    protected void doBefore(Object proxy, Method method, Object[] params, MethodProxy methodProxy) {

    }

    /**
     * 拦截后置校验
     *
     * @param result 方法执行返回的结果
     */
    protected void doAfter(Object result) {

    }
}
