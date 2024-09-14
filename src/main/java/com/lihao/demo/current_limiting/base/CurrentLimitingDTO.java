package com.lihao.demo.current_limiting.base;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 限流DTO基类
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Data
public abstract class CurrentLimitingDTO {
    /**
     * 限流key
     */
    protected String key;
    /**
     * 限流次数
     */
    protected Integer count;
    /**
     * 限流时间 默认秒
     */
    protected long time;
    /**
     * 时间单位
     */
    protected TimeUnit unit;
    public CurrentLimitingDTO(Integer count,long time, TimeUnit unit){
        this.count=count;
        this.time=time;
        this.unit=unit;
    }
    public CurrentLimitingDTO(){

    }

    /**
     * 判断是否有位置
     * @param permits 要占用的位置数
     */
    public abstract boolean tryAcquire(int permits);

    /**
     * 占用permits个位置
     * @param permits 占用数量
     */
    public abstract void deductionToken(int permits);
    /**
     * 释放或添加位置
     */
    protected abstract void refillTokens();
}
