package com.extend.mq.config;

import com.alibaba.fastjson.JSON;
import com.extend.common.constant.RocketMQParamNameEnum;
import com.extend.common.constant.RocketMQParamSerializeEnum;
import com.extend.common.exception.BaseExceotionEnum;
import com.extend.common.exception.BaseException;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MessageListenerConcurrentlyImpl，消息监听。
 *
 * @author KevinClair
 */
public class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

    private Map<String, RocketMQConfiguration> listenerConf = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(MessageListenerConcurrently.class);

    public MessageListenerConcurrentlyImpl(Map<String, RocketMQConfiguration> listenerConf) {
        this.listenerConf = listenerConf;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt msg : list) {
                String topic = msg.getTopic();
                String tags = msg.getTags();
                RocketMQConfiguration conf = listenerConf.get(topic + "_" + tags);
                if (conf == null) {
                    conf = listenerConf.get(topic + "_*");
                }
                if (conf == null) {
                    throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_INIT_ERROR);
                }
                Object[] params = new Object[conf.getParams().size()];

                for (int i = 0; i < conf.getParams().size(); i++) {
                    Map<String, Object> e = conf.getParams().get(i);
                    Object param = null;
                    switch ((RocketMQParamNameEnum) e.get("name")) {
                        case BODY:
                            param = new String(msg.getBody(), "UTF-8");
                            break;
                        case MSGID:
                            param = msg.getMsgId();
                            break;
                        case QUEUE_ID:
                            param = Integer.toString(msg.getQueueId());
                            break;
                        case RECONSUMETIMES:
                            param = Integer.toString(msg.getReconsumeTimes());
                            break;
                        default:
                            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SERIALIZE_ERROR);
                    }
                    switch ((RocketMQParamSerializeEnum) e.get("serialize")) {
                        case STRING:
                            params[i] = param;
                            break;
                        case JSON:
                            params[i] = JSON.parseObject((String) param, (Class<?>) e.get("serializeType"));
                            break;
                        case JSON_ARRAY:
                            params[i] = JSON.parseArray((String) param, (Class<?>) e.get("serializeType"));
                            break;
                        default:
                            throw BaseException.getInstance(BaseExceotionEnum.ROCKET_MQ_SERIALIZE_ERROR);
                    }
                }
                conf.getMethod().invoke(conf.getObj(), params);
            }
        } catch (Exception e) {
            log.error("MQ消费消息失败,msg{}，等待重试...", list, e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    public MessageListenerConcurrentlyImpl() {
    }
}
