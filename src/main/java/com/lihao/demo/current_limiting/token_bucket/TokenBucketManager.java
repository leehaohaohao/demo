package com.lihao.demo.current_limiting.token_bucket;

import com.lihao.demo.context.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 令牌桶管理
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
@Slf4j
public class TokenBucketManager {
    private final ConcurrentHashMap<String, TokenBucketDto> tokenBucketMap = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    public void createTokenBucket(String key, long capacity, double refillRate) throws GlobalException {
        lock.lock();
        try {
            if (!tokenBucketMap.containsKey(key)) {
                TokenBucketDto tokenBucket = new TokenBucketDto(capacity, refillRate);
                tokenBucketMap.put(key, tokenBucket);
            }
        } finally {
            lock.unlock();
        }
    }
    public void removeTokenBucket(String key) {
        lock.lock();
        try {
            tokenBucketMap.remove(key);
        } finally {
            lock.unlock();
        }
    }
    public boolean tryAcquire(String key, int permits) {
        TokenBucketDto tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket != null) {
            return tokenBucket.tryAcquire(permits);
        }
        return false;
    }

    public void deductionToken(String key, int permits) {
        TokenBucketDto tokenBucket = tokenBucketMap.get(key);
        if (tokenBucket != null) {
            tokenBucket.deductionToken(permits);
        }
    }
}
