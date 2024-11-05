package com.lihao.demo.context.user;
/**
 * 用户上下文信息接口
 * @author lihao
 * &#064;date  2024/9/13
 * @since 1.0
 */
public interface UserContextProvider{
    String getUserId();
    String getIp();
}
