package com.extend.common.utils;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

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
    *@Description 前置校验
    *@Param []
    *@Author mingj
    *@Date 2020/7/4 21:35
    *@Return void
    **/
    protected void doBefore(Object proxy, Method method, Object[] params, MethodProxy methodProxy) {

    }

    /**
    *@Description 后置校验
    *@Param []
    *@Author mingj
    *@Date 2020/7/4 21:35
    *@Return void
    **/
    protected void doAfter(Object result) {

    }
}
