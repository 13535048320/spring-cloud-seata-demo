package com.example.clientone.web;

import com.example.clientone.feign.FeignService;
import com.example.clientone.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private FeignService feignService;

    @Autowired
    private ITestService testService;

    @Value("${client.text}")
    private String text;

    @GetMapping("/test")
    public String test() {
        return this.text;
    }

    @GetMapping("testInsert")
    public String testInsert() {
        this.testService.insertTest("zzc");
        int a = 1 / 0;
        return "OK";
    }

    @GetMapping("/testFeign")
    public String testFeign() {
        return this.feignService.test();
    }

    @GetMapping("/testFeignInsert")
    @GlobalTransactional
    public String testFeignInsert() {
        this.feignService.testFeignInsert();
        int zero = 1 / 0;
        return "OK";
    }
}
