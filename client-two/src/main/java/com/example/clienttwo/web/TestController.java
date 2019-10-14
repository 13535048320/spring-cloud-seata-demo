package com.example.clienttwo.web;

import com.example.clienttwo.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ITestService testService;

    @GetMapping("/test")
    public String test(){
        return "test-client-two";
    }

    @GetMapping("/testFeignInsert")
//    @GlobalTransactional
    public String testFeignInsert(){
        testService.insertTest("zzc-1");
//        int zero = 1 / 0;
        return "OK";
    }
}
