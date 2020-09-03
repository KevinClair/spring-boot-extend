package com.extend.log.cat.mq;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.extend.common.constant.CatTypeEnum;
import com.extend.common.utils.Interceptor;
import com.extend.log.cat.utils.CatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
/**
 * RocketMQ发送端拦截器
 *
 * @author mingj
 * @date 2020/8/14
 */
@Slf4j
public class CatRocketMQProducerInterceptor extends Interceptor {

    public CatRocketMQProducerInterceptor(MethodInterceptor methodInterceptor) {
        super(methodInterceptor);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        // TODO 只处理send方法
        if ("sendSyncMessage".equals(method.getName())) {
            com.dianping.cat.message.Transaction transaction = Cat.newTransaction(CatTypeEnum.ROCKETMQ_PRODUCER.getName(), params[0].toString());
            try {
                result = super.intercept(proxy, method, params, methodProxy);
                transaction.setStatus(Message.SUCCESS);
            } catch (Exception e) {
                CatUtil.processException(e, transaction);
                // 这里为了防止重复记录异常信息，就不再做log.error日志，直接抛出，让消息回滚
                throw e;
            } finally {
                transaction.complete();
            }
        } else {
            result = super.intercept(proxy, method, params, methodProxy);
        }
        return result;
    }
}
