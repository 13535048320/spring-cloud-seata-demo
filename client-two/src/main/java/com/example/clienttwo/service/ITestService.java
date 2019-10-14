package com.example.clienttwo.service;

import com.example.clienttwo.model.Test;

public interface ITestService extends IBaseService<Test> {
    void insertTest(String name);
}
