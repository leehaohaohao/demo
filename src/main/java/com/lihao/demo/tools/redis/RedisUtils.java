package com.lihao.demo.tools.redis;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component("redisUtils")
public class RedisUtils<V> {
    @Resource
    private RedisTemplate<String,V> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    /**
     * 删除缓存
     * @param key 一个或多个
     */
    public void delete(String... key){
        if(key !=null && key.length >0){
            if(key.length ==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }
    public V get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存存入
     * @param key 键
     * @param value 值
     * @return true成功 or false失败
     */
    public boolean set(String key,V value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,value);
            return false;
        }
    }

    /**
     *
     * @param key 键
     * @param value 值
     * @param time 有效时间
     * @return true成功 or false失败
     */
    public boolean setex(String key,V value , long time){
        try{
            if(time>0){
                redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
            }else{
                set(key,value);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,value);
            return false;
        }
    }

    public boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key,time,TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<V> getQueueList(String key){
        return redisTemplate.opsForList().range(key,0,-1);
    }

    public boolean lpush(String key,V value,long time){
        try{
            redisTemplate.opsForList().leftPush(key,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,value,e);
            return false;
        }
    }
    public long remove(String key,Object value){
        try{
            Long remove = redisTemplate.opsForList().remove(key,1,value);
            return remove;
        }catch (Exception e){
            logger.error("移除redisKey:{},value:{}失败",key,value,e);
            return 0;
        }
    }
    public boolean lpushAll(String key , List<V> values,long time){
        try{
            redisTemplate.opsForList().leftPushAll(key,values);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,values,e);
            return false;
        }
    }
    public boolean rpushAll(String key , List<V> values,long time){
        try{
            redisTemplate.opsForList().rightPushAll(key,values);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},value:{}失败",key,values,e);
            return false;
        }
    }
    public boolean sadd(String key, Set<V> values, long time){
        try {
            values.forEach(value ->redisTemplate.opsForSet().add(key,value));
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            logger.error("设置redisKey:{},values:{}失败",key,values,e);
            return false;
        }
    }

    /**
     * 删除包含某个字符串的部分key
     * @param pattern 包含某字符串
     * @param limit 删除数量
     */
    public void deleteKeysWithPatternAndLimit(String pattern, long limit) {
        Set<String> keys = redisTemplate.keys(pattern+"*");
        logger.error(keys.toString());
        if (!CollectionUtils.isEmpty(keys)) {
            keys.stream().limit(limit).forEach(redisTemplate::delete);
        }
    }
    /**
     * 使用HashMap存储统计数据
     * @param key 键
     * @param hashKey 哈希键
     * @param value 值
     * @return true成功 or false失败
     */
    public boolean hset(String key, String hashKey, V value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},hashKey:{},value:{}失败", key, hashKey, value, e);
            return false;
        }
    }

    /**
     * 获取HashMap中的值
     * @param key 键
     * @param hashKey 哈希键
     * @return 值
     */
    public V hget(String key, String hashKey) {
        return (V) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取HashMap中的所有键值对
     * @param key 键
     * @return 键值对
     */
    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除HashMap中的某个键值对
     * @param key 键
     * @param hashKey 哈希键
     * @return true成功 or false失败
     */
    public boolean hdel(String key, String... hashKey) {
        try {
            redisTemplate.opsForHash().delete(key, (Object[]) hashKey);
            return true;
        } catch (Exception e) {
            logger.error("删除redisKey:{},hashKey:{}失败", key, hashKey, e);
            return false;
        }
    }

}

