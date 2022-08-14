package com.musseukpeople.woorimapnotification.common.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.musseukpeople.woorimapnotification.notification.application.message.RedisMessageSubscriber;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    private static final ChannelTopic POST_CHANNEL = ChannelTopic.of("post");

    private final String host;
    private final String password;
    private final int port;

    public RedisConfig(
        @Value("${spring.redis.host}") String host,
        @Value("${spring.redis.password}") String password,
        @Value("${spring.redis.port}") int port
    ) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       RedisMessageSubscriber redisMessageSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisMessageSubscriber, POST_CHANNEL);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageStringListener(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }
}
