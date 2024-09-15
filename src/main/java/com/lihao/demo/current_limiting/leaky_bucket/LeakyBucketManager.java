package com.lihao.demo.current_limiting.leaky_bucket;

import com.lihao.demo.current_limiting.base.CurrentLimitingManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/15--16:00
 * @since 1.0
 */
@Component
public class LeakyBucketManager implements CurrentLimitingManager<String, LeakyBucketDto> {
    private final ConcurrentHashMap<String, LeakyBucketDto> leakyBucketMap = new ConcurrentHashMap<>();
    @Override
    public void create(String key, LeakyBucketDto dto) {
        leakyBucketMap.computeIfAbsent(key, k -> dto);
    }
    @Override
    public void remove(String key) {
        leakyBucketMap.remove(key);
    }
    @Override
    public boolean tryAcquire(String key, int permits) {
        if(leakyBucketMap.containsKey(key)){
            return leakyBucketMap.get(key).tryAcquire(permits);
        }
        return false;
    }
    @Override
    public void deductionToken(String key, int permits) {
        if (leakyBucketMap.containsKey(key)){
            leakyBucketMap.get(key).deduction(permits);
        }
    }
}
