package com.lihao.demo;

import com.lihao.demo.api_usage.base.MonitorApiUsage;
import com.lihao.demo.current_limiting.base.CurrentLimiting;
import com.lihao.demo.context.pack.ResponsePack;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo
 */
@RestController
public class DemoController {
    @RequestMapping("/current/limit")
    @CurrentLimiting(keyType = CurrentLimiting.KeyType.ALL,capacity = 5,rate = 2)
    @MonitorApiUsage(enableHandTrembling = false)
    public ResponsePack<String> currentLimit(){
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("success");
        responsePack.setSuccess(true);
        return responsePack;
    }
    @RequestMapping("/monitor")
    @MonitorApiUsage(handType= MonitorApiUsage.HandType.ALL,handCount = 4)
    public ResponsePack<String> monitor(){
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("success");
        responsePack.setSuccess(true);
        return responsePack;
    }
}
