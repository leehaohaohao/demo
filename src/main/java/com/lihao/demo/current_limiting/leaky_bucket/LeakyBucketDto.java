package com.lihao.demo.current_limiting.leaky_bucket;

import com.lihao.demo.current_limiting.base.CurrentLimitingDTO;


/**
 * classname
 *
 * @author lihao
 * &#064;date  2024/9/15--15:59
 * @since 1.0
 */
public class LeakyBucketDto extends CurrentLimitingDTO {
    private final long capacity;
    private final double leakRate;
    private double water;
    private long lastRefillTime;
    public LeakyBucketDto(long capacity, double leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.water = 0;
        lastRefillTime=System.currentTimeMillis();
    }
    @Override
    public boolean tryAcquire(int permits) {
        refill();
        return water+permits<=capacity;
    }

    @Override
    public void deduction(int permits) {
        if(water+permits<=capacity) water+=permits;
    }

    @Override
    protected void refill() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTime;
        double leakedWater = (int) (elapsedTime * leakRate / 1000);
        if(leakedWater>0){
            water = Math.max(water - leakedWater, 0);
            lastRefillTime = currentTime;
        }
    }
}
