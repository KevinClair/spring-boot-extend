package com.extend.log.cat.mq;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.extend.common.constant.CatTypeEnum;
import com.extend.common.utils.Interceptor;
import com.extend.log.cat.utils.CatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * RocketMQ的消费拦截器，cat记录
 *
 * @author mingj
 * @date 2020/8/14
 */
@Slf4j
public class CatRocketMQConsumerInterceptor extends Interceptor {
    public CatRocketMQConsumerInterceptor(MethodInterceptor methodInterceptor) {
        super(methodInterceptor);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        if ("consumeMessage".equals(method.getName())) {
            String topic = "";
            if (params != null && params.length > 1) {
                ConsumeConcurrentlyContext contexts = (ConsumeConcurrentlyContext) params[1];
                topic = contexts.getMessageQueue().getTopic();
            }
            com.dianping.cat.message.Transaction transaction = Cat.newTransaction(CatTypeEnum.ROCKETMQ_CONSUMER.getName(), topic);
            try {
                Object result = super.intercept(proxy, method, params, methodProxy);
                transaction.setStatus(Message.SUCCESS);
                return result;
            } catch (Exception e) {
                CatUtil.processException(e, transaction);
                // 这里为了防止重复记录异常信息，就不再做log.error日志，直接抛出，返回消息发送失败，客户端重试
                throw e;
            } finally {
                transaction.complete();
            }
        }
        return super.intercept(proxy, method, params, methodProxy);
    }
}
