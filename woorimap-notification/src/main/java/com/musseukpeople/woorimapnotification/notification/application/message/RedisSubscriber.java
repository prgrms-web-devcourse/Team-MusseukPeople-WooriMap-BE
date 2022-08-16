package com.musseukpeople.woorimapnotification.notification.application.message;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

public interface RedisSubscriber extends MessageListener {

    Topic getTopic();
}
