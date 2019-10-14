package com.example.clientone.mapper;

import com.example.clientone.model.Test;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TestMapper extends Mapper<Test> {
    void insertTest(@Param("name") String name);
}
