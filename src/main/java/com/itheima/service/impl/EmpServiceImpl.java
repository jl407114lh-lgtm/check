package com.itheima.service.impl;


import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageResult<Emp> page(Integer page, Integer pageSize) {
//        //0.参数兜底：页码从1开始，每页条数默认10，避免出现负数起始索引
//        if (page == null || page < 1) {
//            page = 1;
//        }
//        if (pageSize == null || pageSize < 1) {
//            pageSize = 10;
//        }

        //1.调用Mapper接口，查询总记录数
        Long total = empMapper.count();

        //2.调用Mapper接口，查询结果列表
        Integer start = (page - 1) * pageSize; //起始索引 = (页码 - 1) * 每页记录数
        List<Emp> rows = empMapper.list(start, pageSize);

        //3.封装结果PageResult
        return new PageResult<Emp>(total, rows);
    }
}
