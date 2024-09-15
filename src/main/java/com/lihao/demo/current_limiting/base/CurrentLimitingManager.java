package com.lihao.demo.current_limiting.base;

/**
 * 限流管理器接口
 *
 * @author lihao
 * &#064;date  2024/9/15--15:25
 * @since 1.0
 */
public interface CurrentLimitingManager<K,T> {
    void create(K key,T dto);
    void remove(K key);
    boolean tryAcquire(K key, int permits);
    void deductionToken(K key, int permits);
}
