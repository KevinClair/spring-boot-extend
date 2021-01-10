package com.extend.mq.interceptor;

import com.extend.core.utils.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * RocketMqConsumerInterceptor，示范拦截器
 *
 * @author KevinClair
 */
@Slf4j
public class RocketMqConsumerInterceptor extends Interceptor {

    public RocketMqConsumerInterceptor(MethodInterceptor methodInterceptor) {
        super(methodInterceptor);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        if (method.getName().equals("consumeMessage")) {
            result = super.intercept(proxy, method, params, methodProxy);
        }
        return result;
    }

    @Override
    public void doBefore(Object proxy, Method method, Object[] params, MethodProxy methodProxy){
        log.info("【RocketMQ消费逻辑开始------------------------------------------------------------------------------------------------------------------------------------】");
    }

    @Override
    public void doAfter(Object result){
        log.info("【RocketMQ消费逻辑结束------------------------------------------------------------------------------------------------------------------------------------】");
    }
}
