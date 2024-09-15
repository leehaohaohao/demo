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
    @RequestMapping("/tokenBucket")
    @CurrentLimiting(keyType = CurrentLimiting.KeyType.ALL,capacity = 5,rate = 2)
    @MonitorApiUsage(enableHandTrembling = false)
    public ResponsePack<String> tokenBucket(){
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("success");
        responsePack.setSuccess(true);
        return responsePack;
    }
    @RequestMapping("/fixedWindow")
    @CurrentLimiting(keyType = CurrentLimiting.KeyType.ALL
            ,strategyName = "FixedWindowStrategy")
    @MonitorApiUsage(enableHandTrembling = false)
    public ResponsePack<String> fixedWindow(){
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("success");
        responsePack.setSuccess(true);
        return responsePack;
    }
    @RequestMapping("/slideWindow")
    @CurrentLimiting(keyType = CurrentLimiting.KeyType.ALL
            ,strategyName = "SlideWindowStrategy")
    @MonitorApiUsage(enableHandTrembling = false)
    public ResponsePack<String> slideWindow(){
        ResponsePack<String> responsePack = new ResponsePack<>();
        responsePack.setData("success");
        responsePack.setSuccess(true);
        return responsePack;
    }
    @RequestMapping("/leakyBucket")
    @CurrentLimiting(keyType = CurrentLimiting.KeyType.ALL
            ,strategyName = "LeakyBucketStrategy")
    @MonitorApiUsage(enableHandTrembling = false)
    public ResponsePack<String> leakyBucket(){
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
