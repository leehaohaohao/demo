package com.lihao.demo.current_limiting.fixed_window;

import com.lihao.demo.current_limiting.base.CurrentLimitingDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 固定窗口限流策略DTO
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Slf4j
public class FixedWindowDto extends CurrentLimitingDTO {
    private long lastRefillTime; // 上次补充令牌的时间
    private Integer tokens;
    public FixedWindowDto(Integer count, long time, TimeUnit unit){
        super(count,time,unit);
        this.lastRefillTime=System.currentTimeMillis();
        this.tokens=count;
    }
    @Override
    public boolean tryAcquire(int permits){
        log.debug("当前时间：{}，上次补充令牌的时间：{}，当前令牌数：{}，需要补充的令牌数：{}",System.currentTimeMillis(),lastRefillTime,tokens,permits);
        refillTokens();
        log.debug("当前时间：{}，当前令牌数：{}",System.currentTimeMillis(),tokens);
        return tokens>=permits;
    }
    @Override
    public void deductionToken(int permits){
        tokens-=permits;
    }

    @Override
    protected void refillTokens() {
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastRefillTime>=unit.toMillis(time)){
            tokens=count;
            lastRefillTime=currentTime;
        }
    }
}
