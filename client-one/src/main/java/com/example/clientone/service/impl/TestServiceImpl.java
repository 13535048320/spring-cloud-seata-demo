package com.example.clientone.service.impl;

import com.example.clientone.mapper.TestMapper;
import com.example.clientone.model.Test;
import com.example.clientone.service.ITestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

@Service
public class TestServiceImpl extends BaseServiceImpl<Test> implements ITestService {

    private static final String BASE_MESSAGE = "";

    private static final Logger LOGGER = LogManager.getLogger("TestServiceImpl");

    @Resource
    private TestMapper testMapper;

    @Override
    protected Mapper<Test> getBaseMapper() {
        return testMapper;
    }

    @Override
    protected String getBaseMessage() {
        return BASE_MESSAGE;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void insertTest(String name){
       testMapper.insertTest(name);
        int a = 1/0;
    }
}
