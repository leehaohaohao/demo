package com.lihao.demo.api_usage.entity;

import lombok.Data;

import java.util.concurrent.TimeUnit;
/**
 * 防抖信息
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
@Data
public class HandTremblingDto {
    private String prefix;
    private long time;
    private int count;
    private int token;
    private TimeUnit unit;
    private long lastTime;
    public HandTremblingDto(long time, int count,TimeUnit unit) {
        this.count=count;
        this.time=time;
        this.unit=unit;
    }
}
