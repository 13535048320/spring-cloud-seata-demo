package com.example.clientone.service;

import com.example.clientone.model.Test;

public interface ITestService extends IBaseService<Test> {
    void insertTest(String name);
}
