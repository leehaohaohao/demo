package com.lihao.demo.current_limiting.token_bucket;

import com.lihao.demo.current_limiting.base.CurrentLimitingDTO;
import com.lihao.demo.context.exception.GlobalException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 令牌桶DTO
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class TokenBucketDto extends CurrentLimitingDTO {
    private final long capacity; // 令牌桶容量
    private final double refillRate; // 每秒补充的令牌数
    private double tokens; // 当前令牌数量
    private long lastRefillTime; // 上次补充令牌的时间
    private final ReentrantLock lock = new ReentrantLock();//锁 为令牌桶发送、增添令牌加锁
    public TokenBucketDto(long capacity, double refillRate) throws GlobalException {
        if(capacity <= 0 || refillRate <= 0){
            throw new GlobalException("令牌桶参数错误");
        }
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    /**
     * 获取令牌
     * @return true代码获得令牌失败 false为成功
     */
    @Override
    public boolean tryAcquire(int permits) {
        lock.lock();
        try {
            refillTokens();
            log.info("当前令牌数量为：{}",tokens);
            return tokens > permits;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void deductionToken(int permits) {
        lock.lock();
        try {
            tokens -= permits;
        } finally {
            lock.unlock();
        }
    }
    @Override
    protected void refillTokens() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTime;
        double refillAmount = elapsedTime * refillRate / 1000;
        log.info("当前添加的令牌数为：{}",refillAmount);
        tokens = Math.min(capacity, tokens + refillAmount);
        log.info("添加后的令牌数量为：{}",tokens);
        lastRefillTime = currentTime;
    }
}
