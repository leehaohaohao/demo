package com.lihao.demo.current_limiting.slide_window;

import com.lihao.demo.current_limiting.base.CurrentLimitingDTO;

import java.util.concurrent.TimeUnit;

/**
 * 滑动窗口限流DTO
 * @author lihao
 * &#064;date  2024/9/15--14:28
 * @since 1.0
 */
public class SlideWindowDto extends CurrentLimitingDTO {
    private long lastRefillTime;
    private final int bucketCount;
    private final int[] buckets;
    private final long realTime;
    public SlideWindowDto(Integer count, long time, TimeUnit unit,int bucketCount) {
        super(count, time, unit);
        this.lastRefillTime = System.currentTimeMillis();
        this.bucketCount = bucketCount;
        this.buckets = new int[bucketCount];
        this.realTime = unit.toMillis(time);
    }
    @Override
    public boolean tryAcquire(int permits) {
        refill();
        return permits + getTokens() <= count;
    }
    private int getTokens(){
        int result = 0;
        for(int i:buckets){
            result+=i;
        }
        return result;
    }
    @Override
    public void deduction(int permits) {
        long currentTime = System.currentTimeMillis();
        buckets[(int) ((currentTime / (realTime / bucketCount)) % bucketCount)] += permits;
    }

    @Override
    protected void refill() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTime;
        if(elapsedTime >= realTime){
            int bucketIndex = (int) (elapsedTime / (realTime/bucketCount));
            for(int i=0;i<bucketIndex && i<bucketCount;i++){
                buckets[i] = 0;
            }
            lastRefillTime = currentTime;
        }
    }
}
