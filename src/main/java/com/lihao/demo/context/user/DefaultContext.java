package com.lihao.demo.context.user;

import org.springframework.stereotype.Component;

/**
 * 默认用户上下文信息
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Component
public class DefaultContext implements UserContext<ContextInfo> {
    private static final ThreadLocal<ContextInfo> threadLocal = new ThreadLocal<>();
    @Override
    public ContextInfo get(){
        return threadLocal.get();
    }
    @Override
    public void set(ContextInfo contextInfo){
        threadLocal.set(contextInfo);
    }
    @Override
    public void remove(){
        threadLocal.remove();
    }

    @Override
    public String getUserId() {
        return threadLocal.get().getUserId();
    }

    @Override
    public String getIp() {
        return threadLocal.get().getIp();
    }
}
