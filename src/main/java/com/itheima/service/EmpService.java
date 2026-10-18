package com.itheima.service;


import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;

import java.time.LocalDate;

public interface EmpService {

    /**
     * 分页查询员工数据(支持条件查询)
     * @param page 页码(从1开始)
     * @param pageSize 每页记录数
     * @param name 员工姓名，模糊查询，可为空
     * @param gender 性别(1:男 2:女)，精确查询，可为空
     * @param begin 入职日期的开始时间，可为空
     * @param end 入职日期的结束时间，可为空
     * @return 总记录数 + 当前页的员工数据列表
     */
    PageResult<Emp> page(Integer page, Integer pageSize,
                         String name, Integer gender,
                         LocalDate begin, LocalDate end);
}
