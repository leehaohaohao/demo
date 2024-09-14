package com.lihao.demo.api_usage.base;

import com.lihao.demo.api_usage.entity.HandTremblingDto;

/**
 * 防抖接口
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
public interface HandTrembling<T extends HandTremblingDto> {
    /**
     * 新增防抖节点
     * @param t 防抖参数
     */
    void add(T t);
    /**
     * 占用位置
     * @return true成功 false失败
     */
    boolean tryAcquire(T t);
}
