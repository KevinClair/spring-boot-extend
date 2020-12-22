package com.extend.mq.template;

import com.alibaba.fastjson.JSON;
import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * RocketMQTemplate.
 *
 * @author KevinClair
 */
public class RocketMQTemplate implements DisposableBean {

    private static final long TIME_OUT = 3000;

    private DefaultMQProducer producer;

    public RocketMQTemplate(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public SendResult sendSync(String topic, Object obj) {
        return sendSyncMessage(topic, "", "", obj, TIME_OUT);
    }

    public SendResult sendSync(String topic, Object obj, long timeout) {
        return sendSyncMessage(topic, "", "", obj, timeout);
    }

    public SendResult sendSync(String topic, String tags, Object obj) {
        return sendSyncMessage(topic, tags, "", obj, TIME_OUT);
    }

    public SendResult sendSync(String topic, String tags, Object obj, long timeout) {
        return sendSyncMessage(topic, tags, "", obj, timeout);
    }

    public SendResult sendSync(String topic, String tags, String keys, Object obj) {
        return sendSyncMessage(topic, tags, keys, obj, TIME_OUT);
    }

    /**
     * 同步发送消息方法，同步阻塞发送，返回发送结果，但存在消息丢失，取决于刷盘方式是异步刷盘还是同步刷盘；适用于保证数据尽量不丢失的场景
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param timeout 超时时间，默认是3000ms
     * @return {@link SendResult}
     */
    public SendResult sendSyncMessage(String topic, String tags, String keys, Object obj, long timeout) {
        if (StringUtils.isEmpty(topic) || Objects.isNull(obj)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        SendResult sendResult = null;
        try {
            sendResult = producer.send(new Message(topic, tags, keys, getBytes(obj)), timeout);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        if (Objects.isNull(sendResult)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        return sendResult;
    }



    public SendResult sendSyncQueueSelector(String topic, Object obj, MessageQueueSelector selector, Object arg) {
        return sendSyncQueueSelectorMessage(topic, "", "", obj, selector, arg, TIME_OUT);
    }

    public SendResult sendSyncQueueSelector(String topic, Object obj, MessageQueueSelector selector, Object arg, long timeout) {
        return sendSyncQueueSelectorMessage(topic, "", "", obj, selector, arg, timeout);
    }

    public SendResult sendSyncQueueSelector(String topic, Object obj, String tags, MessageQueueSelector selector, Object arg) {
        return sendSyncQueueSelectorMessage(topic, tags, "", obj, selector, arg, TIME_OUT);
    }

    public SendResult sendSyncQueueSelector(String topic, Object obj, String tags, MessageQueueSelector selector, Object arg, long timeout) {
        return sendSyncQueueSelectorMessage(topic, tags, "", obj, selector, arg, timeout);
    }

    public SendResult sendSyncQueueSelector(String topic, String tags, String keys, Object obj, MessageQueueSelector selector, Object arg) {
        return sendSyncQueueSelectorMessage(topic, tags, keys, obj, selector, arg, TIME_OUT);
    }

    /**
     * 同步发送消息方法，同步阻塞发送，返回发送结果，但存在消息丢失，取决于刷盘方式是异步刷盘还是同步刷盘。可以自实现MessageQueueSelector，自选队列发送消息；适用于保证数据尽量不丢失的场景
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param selector MessageQueueSelector消息队列，需要实现其中的方法，选择对应的队列发送消息，通常用于顺序消息；{@link SelectMessageQueueByHash 默认的消息队列选择器，请根据当前场景选择}
     * @param arg 默认的消息队列选择器场景下，会通过arg的hascode和队列数量取模，在实现自定义的消息队列选择器时，此参数可以填null
     * @param timeout 超时时间，默认是3000ms
     * @return {@link SendResult}
     */
    private SendResult sendSyncQueueSelectorMessage(String topic, String tags, String keys, Object obj, MessageQueueSelector selector, Object arg, long timeout) {
        SendResult sendResult = null;
        try {
            sendResult = producer.send(new Message(topic, tags, keys, getBytes(obj)), selector, arg, timeout);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        if (Objects.isNull(sendResult)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        return sendResult;
    }


    /**
     * 一次性发送消息方法，不会有返回结果且没有回调方法，具有消息丢失的异常情况，通常适用于日志记录等场景，不需要考虑消息丢失造成的影响；
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     */
    public void sendOneWay(String topic, String tags, String keys, Object obj) {
        Message message = new Message(topic, tags, keys, getBytes(obj));
        try {
            producer.sendOneway(message);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
    }

    /**
     * 一次性发送消息方法，不会有返回结果且没有回调方法，具有消息丢失的异常情况
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param selector MessageQueueSelector消息队列，需要实现其中的方法，选择对应的队列发送消息，通常用于顺序消息；{@link SelectMessageQueueByHash 默认的消息队列选择器，请根据当前场景选择}
     * @param arg 默认的消息队列选择器场景下，会通过arg的hascode和队列数量取模，在实现自定义的消息队列选择器时，此参数可以填null
     */
    public void sendOneWayQueueSelector(String topic, String tags, String keys, Object obj, MessageQueueSelector selector, Object arg) {
        Message message = new Message(topic, tags, keys, getBytes(obj));
        try {
            producer.sendOneway(message, selector, arg);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
    }



    public void sendAsync(String topic, Object obj, SendCallback sendCallback) {
        sendAsyncMessage(topic, "", "", obj, sendCallback, TIME_OUT);
    }

    public void sendAsync(String topic, Object obj, SendCallback sendCallback, long timeout) {
        sendAsyncMessage(topic, "", "", obj, sendCallback, timeout);
    }

    public void sendAsync(String topic, String tags, Object obj, SendCallback sendCallback) {
        sendAsyncMessage(topic, tags, "", obj, sendCallback, TIME_OUT);
    }

    public void sendAsync(String topic, String tags, Object obj, SendCallback sendCallback, long timeout) {
        sendAsyncMessage(topic, tags, "", obj, sendCallback, timeout);
    }

    public void sendAsync(String topic, String tags, String keys, Object obj, SendCallback sendCallback) {
        sendAsyncMessage(topic, tags, keys, obj, sendCallback, TIME_OUT);
    }

    /**
     * 异步发送消息方法，异步发送，不返回发送结果，同样存在消息丢失。适用于数据量大且需要保证消息尽量不丢失的场景，保证客户端不需要等待发送结果及时响应；
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param sendCallback 发送回调方法，消息发送结束之后会由异步线程调用回调方法
     * @param timeout 超时时间，默认是3000ms
     */
    public void sendAsyncMessage(String topic, String tags, String keys, Object obj, SendCallback sendCallback, long timeout) {
        if (StringUtils.isEmpty(topic) || Objects.isNull(obj)) {
            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
        try {
            producer.send(new Message(topic, tags, keys, getBytes(obj)), sendCallback, timeout);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
    }

    public void sendAsyncQueueSelector(String topic, Object obj, MessageQueueSelector selector, SendCallback sendCallback, Object arg) {
        sendAsyncQueueSelectorMessage(topic, "", "", obj, selector, arg, sendCallback, TIME_OUT);
    }

    public void sendAsyncQueueSelector(String topic, Object obj, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) {
        sendAsyncQueueSelectorMessage(topic, "", "", obj, selector, arg, sendCallback, timeout);
    }

    public void sendAsyncQueueSelector(String topic, Object obj, String tags, MessageQueueSelector selector, Object arg, SendCallback sendCallback) {
        sendAsyncQueueSelectorMessage(topic, tags, "", obj, selector, arg, sendCallback, TIME_OUT);
    }

    public void sendAsyncQueueSelector(String topic, Object obj, String tags, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) {
        sendAsyncQueueSelectorMessage(topic, tags, "", obj, selector, arg, sendCallback, timeout);
    }

    public void sendAsyncQueueSelector(String topic, String tags, String keys, Object obj, MessageQueueSelector selector, Object arg, SendCallback sendCallback) {
        sendAsyncQueueSelectorMessage(topic, tags, keys, obj, selector, arg, sendCallback, TIME_OUT);
    }

    /**
     * 异步发送消息方法，异步发送，不返回发送结果，同样存在消息丢失。可以自实现MessageQueueSelector，自选队列发送消息；适用于数据量大且需要保证消息尽量不丢失的场景，保证客户端不需要等待发送结果及时响应；
     *
     * @param topic TOPIC主题信息
     * @param tags 主题的tags信息，没有可以不填，空字符串即可或者*
     * @param keys key信息，没有可以不填，空字符串即可
     * @param obj 需要发送的对象信息
     * @param selector MessageQueueSelector消息队列，需要实现其中的方法，选择对应的队列发送消息，通常用于顺序消息；{@see SelectMessageQueueByHash 默认的消息队列选择器，请根据当前场景选择}
     * @param arg 默认的消息队列选择器场景下，会通过arg的hascode和队列数量取模，在实现自定义的消息队列选择器时，此参数可以填null
     * @param sendCallback 发送回调方法，消息发送结束之后会由异步线程调用回调方法
     * @param timeout 超时时间，默认是3000ms
     */
    private void sendAsyncQueueSelectorMessage(String topic, String tags, String keys, Object obj, MessageQueueSelector selector, Object arg, SendCallback sendCallback, long timeout) {
        try {
            producer.send(new Message(topic, tags, keys, getBytes(obj)), selector, arg, sendCallback, timeout);
        } catch (Exception e) {
            throw BaseException.getInstance(e, BaseExceotionEnum.ROCKET_MQ_SEND_MESSAGE_ERROR);
        }
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


    /**
     * 销毁producer.
     */
    @Override
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
        }
    }
}
