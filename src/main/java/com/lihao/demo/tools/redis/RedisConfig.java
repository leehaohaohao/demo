package com.lihao.demo.tools.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig<V> {
    @Bean("redisTemplate")
    public RedisTemplate<String,V> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //设置Key的序列化格式
        template.setKeySerializer(RedisSerializer.string());
        //设置value的序列化格式
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }
}
