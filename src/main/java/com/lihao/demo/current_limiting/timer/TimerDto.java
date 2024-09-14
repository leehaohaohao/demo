package com.lihao.demo.current_limiting.timer;

import com.lihao.demo.current_limiting.base.CurrentLimitingDTO;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 计时器限流策略DTO
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
public class TimerDto extends CurrentLimitingDTO {
    private long lastRefillTime; // 上次补充令牌的时间
    private Integer tokens;
    private final ReentrantLock lock = new ReentrantLock();//锁 为令牌桶发送、增添令牌加锁
    public TimerDto(Integer count,long time,  TimeUnit unit){
        super(count,time,unit);
        this.lastRefillTime=System.currentTimeMillis();
        this.tokens=count;
    }
    @Override
    public boolean tryAcquire(int permits){
        lock.lock();
        try {
            return count>0;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void deductionToken(int permits){
        lock.lock();
        try {
            tokens-=permits;
        }finally {
            lock.unlock();
        }
    }

    @Override
    protected void refillTokens() {
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastRefillTime>=time){
            tokens=count;
            lastRefillTime=currentTime;
        }
    }
}
