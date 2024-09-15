package com.lihao.demo.current_limiting.token_bucket;

import com.lihao.demo.current_limiting.base.CurrentLimitingManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 令牌桶管理
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
@Slf4j
public class TokenBucketManager implements CurrentLimitingManager<String, TokenBucketDto> {
    private final ConcurrentHashMap<String, TokenBucketDto> tokenBucketMap = new ConcurrentHashMap<>();
    @Override
    public void remove(String key) {
        tokenBucketMap.remove(key);
    }
    @Override
    public boolean tryAcquire(String key, int permits) {
        TokenBucketDto tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket != null) {
            return tokenBucket.tryAcquire(permits);
        }
        return false;
    }

    @Override
    public void create(String key, TokenBucketDto tokenBucketDto){
        tokenBucketMap.computeIfAbsent(key, k -> tokenBucketDto);
    }
    @Override
    public void deductionToken(String key, int permits) {
        TokenBucketDto tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket != null) {
            tokenBucket.deduction(permits);
        }
    }
}
