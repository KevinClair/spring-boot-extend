package com.extend.mq.template;

import com.alibaba.fastjson.JSON;
import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @version 1.0
 * @ClassName RocketMQTransactionTemokate
 * @Description MQ事务消息模板
 * @Author mingj
 * @Date 2020/7/12 20:27
 **/
public class RocketMQTransactionTemplate implements DisposableBean {

    private TransactionMQProducer producer;

    public RocketMQTransactionTemplate(TransactionMQProducer producer) {
        this.producer = producer;
    }

    public TransactionSendResult send(String topic, Object obj, Object arg, TransactionListener transactionListener) {
        return sendMessage(topic, "", "", obj, arg, transactionListener);
    }

    public TransactionSendResult send(String topic, String tags, Object obj, Object arg, TransactionListener transactionListener) {
        return sendMessage(topic, tags, "", obj, arg, transactionListener);
    }

    /**
     * @Description 发送事务消息方法
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param transactionListener executeLocalTransaction执行本地事务的方法，需要在方法执行结束后确认是提交消息还是回滚消息！
     * @param arg transactionListener的实现方法executeLocalTransaction执行本地事务的方法的第二个参数，具体使用方法根据当前本地事务执行器决定；可以选择和发送消息的obj为同一个，也可以不选择；
     * @param transactionListener 事务消息监听器，方法checkLocalTransaction返回Unkhown或者因为网络等问题broker节点无法获取到事务消息的状态，将会调用此方法回查事务消息状态，通常是通过DB反查本地事务是否正常提交了；
     * @Param [topic, tags, keys, obj, tranExecuter, arg, transactionCheckListener]
     * @Author mingj
     * @Date 2020/7/13 0:32
     * @Return org.apache.rocketmq.client.producer.TransactionSendResult
     **/
    public TransactionSendResult sendMessage(String topic, String tags, String keys, Object obj, Object arg, TransactionListener transactionListener) {
        if (StringUtils.isEmpty(topic) || Objects.isNull(obj) || Objects.isNull(transactionListener)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        TransactionSendResult sendResult = null;
        try {
            producer.setTransactionListener(transactionListener);
            sendResult = producer.sendMessageInTransaction(new Message(topic, tags, keys, getBytes(obj)), arg);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        if (Objects.isNull(sendResult)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        return sendResult;
    }

    private byte[] getBytes(Object obj) {
        byte[] body = null;
        if (obj instanceof String) {
            body = obj.toString().getBytes(StandardCharsets.UTF_8);
        } else {
            body = JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
        }
        return body;
    }

    @Override
    public void destroy() throws Exception {
        if (producer != null) {
            producer.shutdown();
        }
    }
}
