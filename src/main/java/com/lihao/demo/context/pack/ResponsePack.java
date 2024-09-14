package com.lihao.demo.context.pack;

import lombok.Data;

import java.io.Serializable;
/**
 * 响应包装体
 * @author lihao
 * &#064;date  2024/9/12
 * @since 1.0
 */
@Data
public class ResponsePack<T> implements Serializable {
    /**
     * 响应是否成功
     */
    private Boolean success;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 响应错误提示消息
     */
    private String error;
    /**
     * 响应错误码
     */
    private Integer code;
}
