package com.lihao.demo.context.user;
/**
 * 用户上下文信息接口
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
public interface UserContext<T extends ContextInfo> {
    T get();
    void set(T info);
    void remove();
    String getUserId();
    String getIp();
}
