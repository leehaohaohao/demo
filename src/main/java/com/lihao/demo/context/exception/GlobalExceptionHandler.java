package com.lihao.demo.context.exception;

import com.lihao.demo.context.pack.ResponsePack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponsePack<String> clasp(Exception e){
        log.error(String.valueOf(e));
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setSuccess(false);
        responsePack.setError(e.getMessage());
        return responsePack;
    }
}
