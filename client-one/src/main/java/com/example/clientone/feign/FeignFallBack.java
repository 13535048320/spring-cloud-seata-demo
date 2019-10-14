package com.example.clientone.feign;

import org.springframework.stereotype.Component;

@Component
public class FeignFallBack implements FeignService {
    @Override
    public String test() {
        return "error";
    }

    @Override
    public String testFeignInsert() {
        return "error";
    }
}
